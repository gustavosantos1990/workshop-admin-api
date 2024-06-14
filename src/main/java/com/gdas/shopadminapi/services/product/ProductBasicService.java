package com.gdas.shopadminapi.services.product;

import com.gdas.shopadminapi.entities.Product;
import com.gdas.shopadminapi.entities.ProductComponent;
import com.gdas.shopadminapi.entities.ProductComponentId;
import com.gdas.shopadminapi.enums.ProductStatus;
import com.gdas.shopadminapi.exceptions.BusinessException;
import com.gdas.shopadminapi.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.springframework.beans.BeanUtils.copyProperties;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ProductBasicService {

    private static final String[] PROPERTIES_TO_IGNORE_ON_UPDATE =
            {"id", "status", "createdAt", "deletedAt", "photoAddress", "components"};

    Logger logger = LoggerFactory.getLogger(ProductBasicService.class);

    private final ProductRepository productRepository;

    public ProductBasicService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product create(Product product) {
        if (product.getId() != null)
            return clone(product.getId());

        product.setStatus(ProductStatus.CREATED);
        return productRepository.save(product);
    }

    public Product clone(UUID productId) {
        Product existingProduct = findById(productId);

        Product clone = new Product();
        clone.setName(generateNameForClone(existingProduct));
        copyProperties(existingProduct, clone, "id", "createdAt", "name", "components");

        Product newProduct = productRepository.save(clone);

        List<ProductComponent> productComponents = existingProduct.getComponents()
                .stream()
                .map(pc -> {
                    ProductComponent newProductComponent = new ProductComponent();
                    newProductComponent.setProductComponentId(new ProductComponentId(newProduct.getId(), pc.getComponent().getId()));
                    newProductComponent.setProduct(newProduct);
                    copyProperties(pc, newProductComponent, "productComponentId", "product");
                    return newProductComponent;
                })
                .collect(Collectors.toList());

        newProduct.setComponents(productComponents);

        return productRepository.save(newProduct);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findOptionalById(UUID productId) {
        return productRepository.findById(productId);
    }

    public Product findById(UUID productId) {
        return findOptionalById(productId)
                .orElseThrow(() -> new BusinessException(NOT_FOUND, "product.id.invalid", productId));
    }

    public Product update(UUID productId, Product product) {
        Product existingProduct = findById(productId);
        copyProperties(product, existingProduct, PROPERTIES_TO_IGNORE_ON_UPDATE);
        return productRepository.save(existingProduct);
    }

        private String generateNameForClone(Product existingProduct) {
        return format("%s%s%s", "[Cópia] ", existingProduct.getName(), randomAlphanumeric(8));
    }
}
