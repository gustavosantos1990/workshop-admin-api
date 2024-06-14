package com.gdas.shopadminapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.gdas.shopadminapi.enums.Measure;
import com.gdas.shopadminapi.enums.deserializer.MeasureDeserializer;
import com.gdas.shopadminapi.usecases.CreateComponentUseCase;
import com.gdas.shopadminapi.usecases.CreateProductComponentUseCase;
import com.gdas.shopadminapi.usecases.UpdateComponentUseCase;
import com.gdas.shopadminapi.usecases.UpdateProductUseCase;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity
@Table(name = "component")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Component {

    //TODO: create update history

    @Id
    @Column(name = "cmp_id", length = 10)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = {CreateProductComponentUseCase.class, UpdateProductUseCase.class})
    @NotBlank(groups = {CreateProductComponentUseCase.class, UpdateProductUseCase.class})
    private String id;

    @Column(name = "cmp_created_at", nullable = false)
    @CreationTimestamp
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Column(name = "cmp_deleted_at")
    @JsonProperty("deleted_at")
    private LocalDateTime deletedAt;

    @Column(nullable = false, unique = true)
    @Size(min = 2,groups = {CreateComponentUseCase.class})
    private String code;

    @Column(name = "cmp_name", nullable = false, unique = true, updatable = false)
    @NotEmpty(groups = {CreateComponentUseCase.class})
    @Size(min = 2,groups = {CreateComponentUseCase.class})
    private String name;

    @Column(name = "measure", nullable = false, length = 100)
    @Enumerated(EnumType.STRING)
    @NotNull(groups = {CreateComponentUseCase.class, UpdateComponentUseCase.class})
    @JsonDeserialize(using = MeasureDeserializer.class)
    private Measure measure;

    @Column(name = "base_buy_height", nullable = false)
    @JsonProperty("base_buy_height")
    @Digits(integer = 6, fraction = 2)
    private BigDecimal baseBuyHeight;

    @Column(name = "base_buy_width", nullable = false)
    @JsonProperty("base_buy_width")
    @Digits(integer = 6, fraction = 2)
    private BigDecimal baseBuyWidth;

    @Column(name = "base_buy_amount", nullable = false)
    @JsonProperty("base_buy_amount")
    @Digits(integer = 6, fraction = 2)
    private BigDecimal baseBuyAmount;

    @Column(name = "base_buy_paid_value", nullable = false)
    @JsonProperty("base_buy_paid_value")
    @NotNull
    @Positive
    @Digits(integer = 6, fraction = 2)
    private BigDecimal baseBuyPaidValue;

    @Column(name = "photo_address")
    @JsonProperty("photo_address")
    private String photoAddress;

    public Component() {
    }

    public Component(String id) {
        this.id = id;
    }

    @AssertTrue(
            message = "check mandatory fields: non-multi: base_buy_amount, multi: base_buy_width and base_buy_height",
            groups = {CreateComponentUseCase.class, UpdateComponentUseCase.class}
    )
    @JsonIgnore
    public boolean isValid() {
        if (measure == null) return false;

        if (measure.isMultiDimension()) {
            return baseBuyWidth != null && baseBuyHeight != null;
        }

        return baseBuyAmount != null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPhotoAddress() {
        return photoAddress;
    }

    public void setPhotoAddress(String photoAddress) {
        this.photoAddress = photoAddress;
    }

    public BigDecimal getBaseBuyHeight() {
        return baseBuyHeight;
    }

    public void setBaseBuyHeight(BigDecimal baseBuyHeight) {
        this.baseBuyHeight = baseBuyHeight;
    }

    public BigDecimal getBaseBuyWidth() {
        return baseBuyWidth;
    }

    public void setBaseBuyWidth(BigDecimal baseBuyWidth) {
        this.baseBuyWidth = baseBuyWidth;
    }

    public BigDecimal getBaseBuyAmount() {
        return baseBuyAmount;
    }

    public void setBaseBuyAmount(BigDecimal baseBuyAmount) {
        this.baseBuyAmount = baseBuyAmount;
    }

    public BigDecimal getBaseBuyPaidValue() {
        return baseBuyPaidValue;
    }

    public void setBaseBuyPaidValue(BigDecimal baseBuyPaidValue) {
        this.baseBuyPaidValue = baseBuyPaidValue;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @PrePersist
    @PreUpdate
    private void prePersist() {
        if (measure.isMultiDimension()) {
            baseBuyAmount = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            return;
        }
        baseBuyHeight = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        baseBuyWidth = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }

    @PostLoad
    @PostPersist
    @PostUpdate
    private void post() {
        if (measure.isMultiDimension()) {
            baseBuyAmount = baseBuyHeight.multiply(baseBuyWidth).setScale(2, RoundingMode.HALF_UP);
        }
    }

    @AssertTrue(groups = {CreateComponentUseCase.class, UpdateComponentUseCase.class})
    @JsonIgnore
    public boolean isCodeValid() {
        if (code == null || code.isEmpty()) return true;

        char A = 'A', Z = 'Z';

        for (char c : code.toCharArray()) {
            if (c < A || c > Z) return false;
        }

        return true;
    }

}