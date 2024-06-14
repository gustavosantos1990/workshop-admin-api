package com.gdas.shopadminapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gdas.shopadminapi.usecases.CreateFinancialEventUseCase;
import com.gdas.shopadminapi.usecases.CreateRequestUseCase;
import com.gdas.shopadminapi.usecases.UpdateRequestStatusUseCase;
import com.gdas.shopadminapi.enums.RequestStatus;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "request")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Request {

    @Id
    @Column(name = "rqt_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = {CreateFinancialEventUseCase.class})
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "rqt_ctm_id")
    @NotNull(groups = {CreateRequestUseCase.class})
    @Valid
    private Customer customer;

    @Column(name = "rqt_created_at", nullable = false)
    @JsonProperty("created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "rqt_canceled_at", updatable = false)
    @JsonProperty("canceled_at")
    private LocalDateTime canceledAt;

    @Column(name = "due_date", nullable = false)
    @JsonProperty("due_date")
    @NotNull(groups = {CreateRequestUseCase.class})
    private LocalDate dueDate;

    @Column(name = "notes")
    private String notes;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(groups = UpdateRequestStatusUseCase.class)
    private RequestStatus status;

    @OneToMany(mappedBy = "request")
    @JsonProperty("request_products")
    private List<RequestProduct> requestProducts;

    public Request() {
    }

    public Request(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Request request)) return false;
        return Objects.equals(id, request.id) && Objects.equals(customer, request.customer) && Objects.equals(createdAt, request.createdAt) && Objects.equals(canceledAt, request.canceledAt) && Objects.equals(dueDate, request.dueDate) && Objects.equals(notes, request.notes) && Objects.equals(rating, request.rating) && status == request.status && Objects.equals(requestProducts, request.requestProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer, createdAt, canceledAt, dueDate, notes, rating, status, requestProducts);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCanceledAt() {
        return canceledAt;
    }

    public void setCanceledAt(LocalDateTime canceledAt) {
        this.canceledAt = canceledAt;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public List<RequestProduct> getRequestProducts() {
        return requestProducts;
    }

    public void setRequestProducts(List<RequestProduct> requestProducts) {
        this.requestProducts = requestProducts;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

}
