package com.gdas.shopadminapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gdas.shopadminapi.enums.ProductStatus;
import com.gdas.shopadminapi.usecases.CreateProductUseCase;
import com.gdas.shopadminapi.usecases.CreateRequestProductUseCase;
import com.gdas.shopadminapi.usecases.UpdateProductUseCase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "product")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product {

    @Id
    @Column(name = "pdt_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = {CreateRequestProductUseCase.class})
    private UUID id;

    @Column(name = "pdt_name", nullable = false, unique = true)
    @NotNull(groups = {CreateProductUseCase.class, UpdateProductUseCase.class})
    @NotBlank(groups = {CreateProductUseCase.class, UpdateProductUseCase.class})
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private ProductStatus status;

    @Column(name = "production_duration_in_minutes", nullable = false)
    @JsonProperty("production_duration_in_minutes")
    @NotNull(groups = {CreateProductUseCase.class, UpdateProductUseCase.class})
    @Positive(groups = {CreateProductUseCase.class, UpdateProductUseCase.class})
    private Integer productionDurationInMinutes;

    @Column(name = "pdt_created_at", nullable = false)
    @CreationTimestamp
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Column(name = "pdt_deleted_at")
    @JsonProperty("deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false)
    @NotNull(groups = {CreateProductUseCase.class, UpdateProductUseCase.class})
    @Positive(groups = {CreateProductUseCase.class, UpdateProductUseCase.class})
    private BigDecimal price;

    @Column(name = "photo_address")
    @JsonProperty("photo_address")
    private String photoAddress;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ProductComponent> components;

    public Product() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Objects.equals(id, product.id) && Objects.equals(name, product.name) && status == product.status && Objects.equals(productionDurationInMinutes, product.productionDurationInMinutes) && Objects.equals(createdAt, product.createdAt) && Objects.equals(deletedAt, product.deletedAt) && Objects.equals(description, product.description) && Objects.equals(price, product.price) && Objects.equals(photoAddress, product.photoAddress) && Objects.equals(components, product.components);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status, productionDurationInMinutes, createdAt, deletedAt, description, price, photoAddress, components);
    }

    public Product(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public int getProductionDurationInMinutes() {
        return productionDurationInMinutes;
    }

    public void setProductionDurationInMinutes(int productionDurationInMinutes) {
        this.productionDurationInMinutes = productionDurationInMinutes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPhotoAddress() {
        return photoAddress;
    }

    public void setPhotoAddress(String photoAddress) {
        this.photoAddress = photoAddress;
    }

    public void setProductionDurationInMinutes(Integer productionDurationInMinutes) {
        this.productionDurationInMinutes = productionDurationInMinutes;
    }

    public List<ProductComponent> getComponents() {
        return components;
    }

    public void setComponents(List<ProductComponent> components) {
        this.components = components;
    }

    public BigDecimal productionCost() {
        if (components == null) return null;

        return components
                .stream()
                .map(ProductComponent::getCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }
}