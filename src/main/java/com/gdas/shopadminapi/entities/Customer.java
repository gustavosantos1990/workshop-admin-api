package com.gdas.shopadminapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gdas.shopadminapi.usecases.CreateRequestUseCase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "customer")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Customer {

    @Id
    @Column(name = "ctm_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "ctm_created_at", nullable = false)
    @JsonProperty("created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "ctm_name", nullable = false)
    @NotNull(groups = {CreateRequestUseCase.class})
    @NotBlank(groups = {CreateRequestUseCase.class})
    private String name;

    @Column(name = "phone", nullable = false, unique = true)
    @NotNull(groups = {CreateRequestUseCase.class})
    @NotBlank(groups = {CreateRequestUseCase.class})
    @Size(min = 11, max = 11, groups = {CreateRequestUseCase.class})
    private String phone;

    @Column(name = "facebook_chat_number")
    private String facebookChatNumber;

    @Column
    private Integer rating;

    public Customer() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFacebookChatNumber() {
        return facebookChatNumber;
    }

    public void setFacebookChatNumber(String facebookChatNumber) {
        this.facebookChatNumber = facebookChatNumber;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

}
