package com.gdas.shopadminapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.gdas.shopadminapi.enums.Event;
import com.gdas.shopadminapi.enums.Operation;
import com.gdas.shopadminapi.enums.Wallet;
import com.gdas.shopadminapi.enums.deserializer.EventDeserializer;
import com.gdas.shopadminapi.usecases.CreateFinancialEventUseCase;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "financial_event")
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinancialEvent {

    @Id
    @Column(name = "fev_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fev_created_at", nullable = false)
    @CreationTimestamp
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Column(name = "fev_date", nullable = false)
    @NotNull(groups = {CreateFinancialEventUseCase.class})
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fev_rqt_id")
    @Valid
    private Request request;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fev_prc_id")
    @Valid
    private Purchase purchase;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(groups = {CreateFinancialEventUseCase.class})
    @JsonDeserialize(using = EventDeserializer.class)
    private Event event;

    @Column(name = "description", nullable = false)
    @NotEmpty(groups = {CreateFinancialEventUseCase.class})
    @NotNull(groups = {CreateFinancialEventUseCase.class})
    @Size(min = 5, groups = {CreateFinancialEventUseCase.class})
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private Operation operation;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private Wallet wallet;

    @Column(name = "fev_value", nullable = false)
    @NotNull(groups = {CreateFinancialEventUseCase.class})
    @Positive(groups = {CreateFinancialEventUseCase.class})
    @Digits(integer = 10, fraction = 2, groups = {CreateFinancialEventUseCase.class})
    private BigDecimal value;

    @Column(name = "banking_operation", nullable = false)
    @JsonProperty("banking_operation")
    private boolean bankingOperation;

    @Column
    private Integer percentage;

    public FinancialEvent() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public boolean isBankingOperation() {
        return bankingOperation;
    }

    public void setBankingOperation(boolean bankingOperation) {
        this.bankingOperation = bankingOperation;
    }
}
