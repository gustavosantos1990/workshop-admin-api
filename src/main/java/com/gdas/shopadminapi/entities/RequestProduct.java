package com.gdas.shopadminapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gdas.shopadminapi.entities.converters.RequestProductDocumentConverter;
import com.gdas.shopadminapi.usecases.CreateRequestProductUseCase;
import com.gdas.shopadminapi.usecases.UpdateRequestProductUseCase;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "request_product")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@DynamicUpdate
public class RequestProduct {

    @EmbeddedId
    @JsonIgnore
    private RequestProductId requestProductId;

    @MapsId("requestId")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "rpd_rqt_id")
    @JsonIgnore
    private Request request;

    @MapsId("productId")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "rpd_pdt_id")
    @NotNull(groups = {CreateRequestProductUseCase.class})
    @Valid
    private Product product;

    @Column(name = "rpd_created_at", nullable = false)
    @JsonProperty("created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    //TODO: stop hibernate updating right after insertion
    @Column(name = "product_document", nullable = false, columnDefinition = "jsonb")
    @Convert(converter = RequestProductDocumentConverter.class)
    @ColumnTransformer(read = "product_document::jsonb", write = "?::jsonb")
    //@JdbcTypeCode(SqlTypes.JSON) > stores as bytea
    @JsonProperty("document")
    private RequestProductDocument productDocument;

    @Column(name = "calculated_production_cost", nullable = false)
    @JsonProperty("calculated_production_cost")
    private BigDecimal calculatedProductionCost;

    @Column(name = "declared_production_cost", nullable = false)
    @JsonProperty("declared_production_cost")
    @NotNull(groups = {CreateRequestProductUseCase.class, UpdateRequestProductUseCase.class})
    @Positive(groups = {CreateRequestProductUseCase.class, UpdateRequestProductUseCase.class})
    private BigDecimal declaredProductionCost;

    @Column(name = "unitary_value", nullable = false)
    @JsonProperty("unitary_value")
    @NotNull(groups = {CreateRequestProductUseCase.class, UpdateRequestProductUseCase.class})
    @Positive(groups = {CreateRequestProductUseCase.class, UpdateRequestProductUseCase.class})
    private BigDecimal unitaryValue;

    @Column(name = "amount", nullable = false)
    @NotNull(groups = {CreateRequestProductUseCase.class, UpdateRequestProductUseCase.class})
    @Positive(groups = {CreateRequestProductUseCase.class, UpdateRequestProductUseCase.class})
    private BigDecimal amount;

    @Column(name = "notes")
    private String notes;

    @Column(name = "file_path")
    @JsonProperty("file_path")
    private String filePath;

    @Column(name = "file_link")
    @JsonProperty("file_link")
    private String fileLink;

    public RequestProduct() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestProduct that)) return false;
        return Objects.equals(requestProductId, that.requestProductId) && Objects.equals(request, that.request) && Objects.equals(product, that.product) && Objects.equals(createdAt, that.createdAt) && Objects.equals(productDocument, that.productDocument) && Objects.equals(calculatedProductionCost, that.calculatedProductionCost) && Objects.equals(declaredProductionCost, that.declaredProductionCost) && Objects.equals(unitaryValue, that.unitaryValue) && Objects.equals(amount, that.amount) && Objects.equals(notes, that.notes) && Objects.equals(filePath, that.filePath) && Objects.equals(fileLink, that.fileLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestProductId, request, product, createdAt, productDocument, calculatedProductionCost, declaredProductionCost, unitaryValue, amount, notes, filePath, fileLink);
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getCalculatedProductionCost() {
        return calculatedProductionCost;
    }

    public void setCalculatedProductionCost(BigDecimal calculatedProductionCost) {
        this.calculatedProductionCost = calculatedProductionCost;
    }

    public BigDecimal getDeclaredProductionCost() {
        return declaredProductionCost;
    }

    public void setDeclaredProductionCost(BigDecimal declaredProductionCost) {
        this.declaredProductionCost = declaredProductionCost;
    }

    public BigDecimal getUnitaryValue() {
        return unitaryValue;
    }

    public void setUnitaryValue(BigDecimal unitaryValue) {
        this.unitaryValue = unitaryValue;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public RequestProductDocument getProductDocument() {
        return productDocument;
    }

    public void setProductDocument(RequestProductDocument productDocument) {
        this.productDocument = productDocument;
    }

    public RequestProductId getRequestProductId() {
        return requestProductId;
    }

    public void setRequestProductId(RequestProductId requestProductId) {
        this.requestProductId = requestProductId;
    }
}
