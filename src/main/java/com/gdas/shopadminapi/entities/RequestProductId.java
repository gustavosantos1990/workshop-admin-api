package com.gdas.shopadminapi.entities;

import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.UUID;

@Embeddable
public class RequestProductId {

    private Long requestId;

    private UUID productId;

    public RequestProductId() {
    }

    public RequestProductId(Long requestId, UUID productId) {
        this.requestId = requestId;
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestProductId that)) return false;
        return Objects.equals(requestId, that.requestId) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, productId);
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }
}
