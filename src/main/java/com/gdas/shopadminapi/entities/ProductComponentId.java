package com.gdas.shopadminapi.entities;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class ProductComponentId {

    private UUID productId;

    private String componentId;

    public ProductComponentId() {
    }

    public ProductComponentId(UUID productId, String componentId) {
        this.productId = productId;
        this.componentId = componentId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }
}
