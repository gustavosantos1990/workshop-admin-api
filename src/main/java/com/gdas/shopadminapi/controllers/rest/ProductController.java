package com.gdas.shopadminapi.controllers.rest;

import com.gdas.shopadminapi.entities.Product;
import com.gdas.shopadminapi.usecases.CreateProductUseCase;
import com.gdas.shopadminapi.usecases.UpdateProductUseCase;
import com.gdas.shopadminapi.services.product.ProductBasicService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static java.lang.String.format;

@RestController
@RequestMapping("/v1/products")
public class ProductController {

    private final ProductBasicService productBasicService;

    public ProductController(ProductBasicService productBasicService) {
        this.productBasicService = productBasicService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> findAllProducts() {
        List<Product> products = productBasicService.findAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> findById(@PathVariable UUID productId) {
        Product product = productBasicService.findById(productId);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<Product> post(
            @Validated(CreateProductUseCase.class) @RequestBody Product product) {
        Product newProduct = productBasicService.create(product);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path(format("/%s", newProduct.getId()))
                .buildAndExpand(newProduct)
                .toUri();
        return ResponseEntity.created(uri).body(newProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> put(
            @PathVariable UUID id,
            @Validated(UpdateProductUseCase.class) @RequestBody Product product) {
        Product newProduct = productBasicService.update(id, product);
        return ResponseEntity.ok(newProduct);
    }

}
