package com.gdas.shopadminapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.gdas.shopadminapi.enums.PurchaseStatus;
import com.gdas.shopadminapi.enums.deserializer.PurchaseStatusDeserializer;
import com.gdas.shopadminapi.usecases.CreatePurchaseUseCase;
import com.gdas.shopadminapi.usecases.UpdatePurchaseUseCase;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "purchase")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Purchase {

    @Id
    @Column(name = "prc_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prc_created_at", nullable = false)
    @JsonProperty("created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "prc_pvd_id", updatable = false)
    @NotNull(groups = {CreatePurchaseUseCase.class})
    @Valid
    private Provider provider;

    @Column(name = "prc_date", nullable = false)
    @NotNull(groups = {CreatePurchaseUseCase.class, UpdatePurchaseUseCase.class})
    private LocalDate date;

    @Column(name = "prc_value", nullable = false)
    @NotNull(groups = {CreatePurchaseUseCase.class, UpdatePurchaseUseCase.class})
    @Positive(groups = {CreatePurchaseUseCase.class, UpdatePurchaseUseCase.class})
    private BigDecimal value;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonDeserialize(using = PurchaseStatusDeserializer.class)
    private PurchaseStatus status;

    @OneToMany(mappedBy = "purchase")
    private List<PurchaseItem> items;

    @OneToMany(mappedBy = "purchase")
    @JsonProperty("financial_events")
    private List<FinancialEvent> financialEvents;

    public Purchase() {
    }

    public Purchase(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public PurchaseStatus getStatus() {
        return status;
    }

    public void setStatus(PurchaseStatus status) {
        this.status = status;
    }

    public List<PurchaseItem> getItems() {
        return items;
    }

    public void setItems(List<PurchaseItem> items) {
        this.items = items;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<FinancialEvent> getFinancialEvents() {
        return financialEvents;
    }

    public void setFinancialEvents(List<FinancialEvent> financialEvents) {
        this.financialEvents = financialEvents;
    }
}
