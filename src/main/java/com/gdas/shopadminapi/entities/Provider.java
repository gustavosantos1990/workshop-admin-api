package com.gdas.shopadminapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.gdas.shopadminapi.usecases.CreateProviderUseCase;
import com.gdas.shopadminapi.usecases.CreatePurchaseUseCase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Entity
@Table(name = "provider")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Provider {

    @Id
    @Column(name = "pvd_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = {CreatePurchaseUseCase.class})
    private UUID id;

    @Column(name = "pvd_name", nullable = false, unique = true)
    @NotNull(groups = {CreateProviderUseCase.class})
    @NotBlank(groups = {CreateProviderUseCase.class})
    private String name;

    @Column(name = "phone")
    @Size(min = 10, max = 11, groups = {CreateProviderUseCase.class})
    private String phone;

    public Provider() {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
