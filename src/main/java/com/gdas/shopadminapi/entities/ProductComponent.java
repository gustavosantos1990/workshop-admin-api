package com.gdas.shopadminapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.gdas.shopadminapi.usecases.CreateProductComponentUseCase;
import com.gdas.shopadminapi.usecases.UpdateProductUseCase;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "product_component")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductComponent {

    @EmbeddedId
    @JsonIgnore
    private ProductComponentId productComponentId;

    @MapsId("productId")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "pco_pdt_id")
    @JsonIgnore
    private Product product;

    @MapsId("componentId")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "pco_cmp_id")
    @NotNull(groups = {CreateProductComponentUseCase.class, UpdateProductUseCase.class})
    @Valid
    private Component component;

    @Column(nullable = false)
    @Min(0)
    private BigDecimal height;

    @Column(nullable = false)
    @Min(0)
    private BigDecimal width;

    @Column(nullable = false)
    @Min(0)
    private BigDecimal amount;

    @Transient
    private BigDecimal cost;

    public ProductComponent() {
    }

    public ProductComponent(ProductComponentId productComponentId) {
        this.productComponentId = productComponentId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public ProductComponentId getProductComponentId() {
        return productComponentId;
    }

    public void setProductComponentId(ProductComponentId productComponentId) {
        this.productComponentId = productComponentId;
    }

    @PrePersist
    @PreUpdate
    private void prePersist() {
        if (component.getMeasure().isMultiDimension()) {
            amount = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            return;
        }
        height = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        width = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }

    @PostLoad
    @PostPersist
    @PostUpdate
    private void post() {
        cost = calculateCost();
        if (component.getMeasure().isMultiDimension()) {
            amount = height.multiply(width).setScale(2, RoundingMode.HALF_UP);
        }
    }

    public BigDecimal calculateCost() {
        return component.getMeasure().isMultiDimension()
                ? calculateCostFormMultiDimensionalComponent()
                : calculateCostFormSingleDimensionalComponent();
    }

    public BigDecimal calculateCostFormSingleDimensionalComponent() {
        BigDecimal paidValue = component.getBaseBuyPaidValue();
        BigDecimal boughtAmount = component.getBaseBuyAmount();
        return amount.multiply(paidValue).divide(boughtAmount, 2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateCostFormMultiDimensionalComponent() {
        BigDecimal paidValue = component.getBaseBuyPaidValue();
        BigDecimal boughtAmount = component.getBaseBuyHeight()
                .multiply(component.getBaseBuyWidth());
        BigDecimal componentAmount = height.multiply(width);
        return componentAmount.multiply(paidValue).divide(boughtAmount, 2, RoundingMode.HALF_UP);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

}
