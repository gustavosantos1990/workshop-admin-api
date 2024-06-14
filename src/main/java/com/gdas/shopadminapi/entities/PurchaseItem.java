package com.gdas.shopadminapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.gdas.shopadminapi.enums.ItemType;
import com.gdas.shopadminapi.enums.deserializer.ItemTypeDeserializer;
import com.gdas.shopadminapi.enums.Measure;
import com.gdas.shopadminapi.enums.deserializer.MeasureDeserializer;
import com.gdas.shopadminapi.usecases.CreatePurchaseItemUseCase;
import com.gdas.shopadminapi.usecases.CreatePurchaseItemOfMultiDimensionalMeasureUseCase;
import com.gdas.shopadminapi.usecases.CreatePurchaseItemOfRegularMeasureUseCase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "purchase_item")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PurchaseItem {

    @Id
    @Column(name = "pci_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pci_prc_id", updatable = false)
    @JsonIgnore
    private Purchase purchase;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pci_cmp_id")
    private Component component;

    @Column(name = "pci_type", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(groups = {CreatePurchaseItemUseCase.class})
    @JsonDeserialize(using = ItemTypeDeserializer.class)
    private ItemType type;

    @Column(name = "pci_name", nullable = false)
    @NotNull(groups = {CreatePurchaseItemUseCase.class})
    @NotBlank(groups = {CreatePurchaseItemUseCase.class})
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(groups = {CreatePurchaseItemUseCase.class})
    @JsonDeserialize(using = MeasureDeserializer.class)
    private Measure measure;

    @Column(nullable = false)
    @NotNull(groups = {CreatePurchaseItemOfRegularMeasureUseCase.class})
    @Positive(groups = {CreatePurchaseItemOfRegularMeasureUseCase.class})
    private BigDecimal amount;

    @Column
    @NotNull(groups = {CreatePurchaseItemOfMultiDimensionalMeasureUseCase.class})
    @Positive(groups = {CreatePurchaseItemOfMultiDimensionalMeasureUseCase.class})
    private BigDecimal width;

    @Column
    @NotNull(groups = {CreatePurchaseItemOfMultiDimensionalMeasureUseCase.class})
    @Positive(groups = {CreatePurchaseItemOfMultiDimensionalMeasureUseCase.class})
    private BigDecimal height;

    @Column
    private BigDecimal price;

    @Transient
    private BigDecimal unitaryValue;

    public PurchaseItem() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Measure getMeasure() {
        return measure;
    }

    public void setMeasure(Measure measure) {
        this.measure = measure;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getUnitaryValue() {
        return unitaryValue;
    }

    public void setUnitaryValue(BigDecimal unitaryValue) {
        this.unitaryValue = unitaryValue;
    }

    private BigDecimal calculateUnitaryValue() {
        return price
                .divide(amount, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);
    }

    @PostLoad
    @PostPersist
    @PostUpdate
    private void postLoad() {
        unitaryValue = calculateUnitaryValue();
    }
}