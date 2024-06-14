package com.gdas.shopadminapi.services;

import com.gdas.shopadminapi.services.product.ProductBasicService;
import com.gdas.shopadminapi.entities.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.UUID;

import static java.lang.String.format;

@Service
public class StorageService {

    @Value("file://${files.base.path}")
    private String filesBasePath;

    Logger logger = LoggerFactory.getLogger(StorageService.class);

    private final ProductBasicService productBasicService;

    public StorageService(ProductBasicService productBasicService) {
        this.productBasicService = productBasicService;
    }

    public URI upload(UUID productId, MultipartFile file) {

        Product product = productBasicService.findById(productId);

        UUID fileId = UUID.randomUUID();

        try (InputStream inputStream = file.getInputStream()) {
            String extension = file.getOriginalFilename().split("\\.")[1];

            URI uri = mountURI(productId, fileId, extension);
            logger.info("uri={}", uri);

            Path targetFile = Path.of(uri);

            createParentIfNotExists(targetFile.getParent());

            upload(inputStream, targetFile);

            deletePreviousIfExists(productId, product.getPhotoAddress());

            savePhotoAddressOnProduct(product, fileId, extension);

            URI httpAddress = toHttpAddress(productId, fileId, extension);

            printData(productId, extension, httpAddress, targetFile);

            return httpAddress;
        } catch (IOException ex) {
            logger.error("error when saving file for product {}", productId);
            logger.error(ex.getMessage(), ex);
            throw new IllegalStateException(ex);
        }
    }

    private void deletePreviousIfExists(UUID productId, String previousPhotoAddress) {
        if (previousPhotoAddress == null || previousPhotoAddress.isEmpty()) return;
        URI previousURI = mountURI(productId, previousPhotoAddress);

        try {
            logger.info("trying to delete file with path {}", previousURI);
            Files.deleteIfExists(Path.of(previousURI));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new IllegalStateException(e);
        }
    }

    private URI mountURI(UUID productId, UUID fileId, String extension) {
        return URI.create(format("%s/%s/%s.%s", filesBasePath, productId, fileId, extension));
    }

    private URI mountURI(UUID productId, String photoAddress) {
        return URI.create(format("%s/%s/%s.%s",
                filesBasePath,
                productId,
                UUID.fromString(photoAddress.split("\\.")[0]),
                photoAddress.split("\\.")[1])
        );
    }

    private void savePhotoAddressOnProduct(Product product, UUID photoId, String extension) {
        String photoAddress = photoId.toString().concat(".").concat(extension);
        product.setPhotoAddress(photoAddress);
        productBasicService.update(product.getId(), product);
    }

    private void printData(
            UUID productId,
            String extension,
            URI httpAddress,
            Path targetFile) {
        logger.info("=====uploaded file=================");
        logger.info("product:\t\t\t|{}", productId);
        logger.info("absolute path:\t|{}", targetFile.getFileName().toAbsolutePath());
        logger.info("http address:\t\t|{}", httpAddress);
        logger.info("extension:\t\t|{}", extension);
        logger.info("file size:\t\t|{} bytes", targetFile.toFile().length());
        logger.info("===================================");
    }

    private void upload(InputStream inputStream, Path targetFile) throws IOException {
        Files.copy(inputStream, targetFile, StandardCopyOption.REPLACE_EXISTING);
    }

    private String getImageExtension(InputStream is) {
        try (ImageInputStream iis = ImageIO.createImageInputStream(is)) {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);

            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                return reader.getFormatName();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        throw new IllegalStateException("could not determine image type");
    }

    private void createParentIfNotExists(Path parent) {
        if (parent == null) {
            throw new IllegalStateException("parent folder is null");
        }

        try {
            if (!parent.toFile().exists()) {
                logger.info("about to create parent folder with path {}", parent);
                Files.createDirectories(parent);
            }
        } catch (IOException e) {
            logger.error("could not create parent folder: {}", parent.toFile());
            throw new IllegalStateException(e);
        }
    }

    private URI toHttpAddress(UUID productId, UUID imageId, String extension) {

        String[] pathSegments = new String[]{
                "static",
                "products",
                productId.toString(),
                imageId.toString().concat(".").concat(extension)
        };

        return UriComponentsBuilder
                .fromUri(ServletUriComponentsBuilder.fromCurrentContextPath().build().toUri())
                .pathSegment(pathSegments)
                .build()
                .toUri();
    }

}
