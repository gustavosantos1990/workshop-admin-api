package com.gdas.shopadminapi.controllers.rest;

import com.gdas.shopadminapi.entities.RequestProduct;
import com.gdas.shopadminapi.entities.RequestProductId;
import com.gdas.shopadminapi.services.request.RequestProductBasicService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.gdas.shopadminapi.usecases.CreateRequestProductUseCase;
import com.gdas.shopadminapi.usecases.UpdateRequestProductUseCase;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static java.lang.String.format;

@RestController
@RequestMapping("/v1/requests/{requestId}/products")
public class RequestProductController {

    private final RequestProductBasicService requestProductService;

    public RequestProductController(RequestProductBasicService requestProductService) {
        this.requestProductService = requestProductService;
    }

    @GetMapping
    public ResponseEntity<List<RequestProduct>> findProducts(@PathVariable Long requestId) {
        List<RequestProduct> requestProducts = requestProductService.findByRequest(requestId);
        return ResponseEntity.ok(requestProducts);
    }

    @PostMapping
    public ResponseEntity<RequestProduct> create(
            @PathVariable Long requestId,
            @Validated(CreateRequestProductUseCase.class) @RequestBody RequestProduct requestProduct) {
        requestProduct.setRequestProductId(new RequestProductId(requestId, requestProduct.getProduct().getId()));
        RequestProduct newRequest = requestProductService.save(requestProduct);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path(format("/%s", newRequest.getProduct().getId()))
                .buildAndExpand(newRequest)
                .toUri();
        return ResponseEntity.created(uri).body(newRequest);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<RequestProduct> update(
            @PathVariable Long requestId,
            @PathVariable UUID productId,
            @Validated(UpdateRequestProductUseCase.class) @RequestBody RequestProduct requestProduct) {

        requestProduct.setRequestProductId(new RequestProductId(requestId, productId));

        RequestProduct updated = requestProductService.update(requestProduct);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<RequestProduct> delete(
            @PathVariable Long requestId,
            @PathVariable UUID productId) {

        requestProductService.delete(new RequestProductId(requestId, productId));

        return ResponseEntity.noContent().build();
    }

}
