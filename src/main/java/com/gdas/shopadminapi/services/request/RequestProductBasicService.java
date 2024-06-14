package com.gdas.shopadminapi.services.request;

import com.gdas.shopadminapi.entities.*;
import com.gdas.shopadminapi.enums.RequestStatus;
import com.gdas.shopadminapi.exceptions.BusinessException;
import com.gdas.shopadminapi.repositories.RequestProductRepository;
import com.gdas.shopadminapi.services.product.ProductBasicService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.gdas.shopadminapi.enums.RequestStatus.UNDER_BILLING;
import static org.springframework.http.HttpStatus.*;

@Service
@Transactional
//TODO: consider splitting this class into specialized ones
public class RequestProductBasicService {

    Logger logger = LoggerFactory.getLogger(RequestProductBasicService.class);

    private final RequestProductRepository repository;
    private final RequestBasicService requestService;
    private final ProductBasicService productService;

    public RequestProductBasicService(RequestProductRepository repository, RequestBasicService requestService, ProductBasicService productService) {
        this.repository = repository;
        this.requestService = requestService;
        this.productService = productService;
    }

    public List<RequestProduct> findByRequest(Long requestId) {
        return repository.findByRequestId(requestId);
    }

    public RequestProduct save(RequestProduct requestProduct) {
        try {
            LoadAndFillRelationships(requestProduct);
            requestProduct.setCalculatedProductionCost(requestProduct.getProduct().productionCost());
            requestProduct.setProductDocument(RequestProductDocument.fromProduct(requestProduct.getProduct()));
            return repository.save(requestProduct);
        } catch (BusinessException be) {
            throw be;
        }
        catch (Throwable t) {
            logger.error(t.getMessage(), t);
            throw new IllegalStateException(t);
        }
    }

    public RequestProduct update(RequestProduct requestProduct) {
        try {
            RequestProduct existingRequestProduct = findById(requestProduct.getRequestProductId());

            validateRequestStatus(existingRequestProduct);

            copyProperties(existingRequestProduct, requestProduct);

            return repository.save(existingRequestProduct);
        } catch(Throwable t) {
            logger.error(t.getMessage(), t);
            throw new IllegalStateException(t);
        }
    }

    public Optional<RequestProduct> findOptionalById(RequestProductId requestProductId) {
        return repository.findById(requestProductId);
    }

    public RequestProduct findById(RequestProductId requestProductId) {
        return findOptionalById(requestProductId)
                .orElseThrow(
                        () -> new BusinessException(NOT_FOUND, "request-product.combination.invalid",
                                requestProductId.getProductId(),
                                requestProductId.getRequestId())
                );
    }

    public void delete(RequestProductId requestProductId) {
        try {
            Request request = requestService.findById(requestProductId.getRequestId());

            Optional<RequestProduct> optionalRequestProduct = request.getRequestProducts()
                    .stream()
                    .filter(rp -> rp.getProduct().getId().equals(requestProductId.getProductId()))
                    .findFirst();

            RequestProduct existingRequestProduct = optionalRequestProduct.orElseThrow(() ->
                    new BusinessException(NOT_FOUND, "request-product.combination.invalid",
                            requestProductId.getRequestId(), requestProductId.getProductId()));

            validateRequestStatusForDeletion(request, existingRequestProduct);

            repository.delete(existingRequestProduct);
        } catch(Throwable t) {
            logger.error(t.getMessage(), t);
            throw new IllegalStateException(t);
        }
    }

    //TODO: consider stop loading whole entities (only check if id exists?)
    private void LoadAndFillRelationships(RequestProduct requestProduct) throws ExecutionException, InterruptedException {

        Optional<RequestProduct> optionalRequestProduct = repository.findById(requestProduct.getRequestProductId());

        optionalRequestProduct.ifPresent(rp -> {
            throw new BusinessException(UNPROCESSABLE_ENTITY, "request-product.already.exists",
                    rp.getProduct().getName(), rp.getRequest().getId());
        });

        CompletableFuture<Optional<Request>> futureRequest = CompletableFuture.supplyAsync(() -> requestService.findOptionalById(requestProduct.getRequestProductId().getRequestId()));
        CompletableFuture<Optional<Product>> futureProduct = CompletableFuture.supplyAsync(() -> productService.findOptionalById(requestProduct.getRequestProductId().getProductId()));

        CompletableFuture.allOf(futureRequest, futureProduct);

        Request request = futureRequest.get()
                .orElseThrow(() ->
                        new BusinessException(NOT_FOUND, "request.id.invalid",
                                requestProduct.getRequestProductId().getRequestId()));

        Product product = futureProduct.get()
                .orElseThrow(() ->
                        new BusinessException(NOT_FOUND, "product.id.invalid",
                                requestProduct.getRequestProductId().getProductId()));

        validateRequestStatus(request);

        requestProduct.setRequestProductId(new RequestProductId(request.getId(), product.getId()));
        requestProduct.setRequest(request);
        requestProduct.setProduct(product);
    }

    private void validateRequestStatus(Request request) {
        if (request.getStatus().getSequence() >= UNDER_BILLING.getSequence()) {
            throw new BusinessException(PRECONDITION_FAILED, "request.product.addition.condition",
                    request.getStatus().getLabel());
        }
    }

    private void copyProperties(RequestProduct existingRequestProduct, RequestProduct requestProduct) {
        existingRequestProduct.setDeclaredProductionCost(requestProduct.getDeclaredProductionCost());
        existingRequestProduct.setUnitaryValue(requestProduct.getUnitaryValue());
        existingRequestProduct.setAmount(requestProduct.getAmount());
        existingRequestProduct.setNotes(requestProduct.getNotes());
        existingRequestProduct.setFilePath(requestProduct.getFilePath());
        existingRequestProduct.setFileLink(requestProduct.getFileLink());
    }

    private void validateRequestStatus(RequestProduct existingRequestProduct) {
        if (existingRequestProduct.getRequest().getStatus().getSequence() >= UNDER_BILLING.getSequence()) {
            throw new BusinessException(PRECONDITION_FAILED, "request.product.addition.condition",
                    existingRequestProduct.getRequest().getStatus());
        }
    }

    private void validateRequestStatusForDeletion(Request request, RequestProduct existingRequestProduct) {
        if (RequestStatus.DELIVERED.getSequence() <= existingRequestProduct.getRequest().getStatus().getSequence()) {
            throw new BusinessException(PRECONDITION_FAILED, "request.product.deletion.condition",
                    existingRequestProduct.getRequest().getStatus());
        }

        if (request.getRequestProducts().size() == 1) {
            throw new BusinessException(PRECONDITION_FAILED, "request.products.quantity");
        }
    }

}