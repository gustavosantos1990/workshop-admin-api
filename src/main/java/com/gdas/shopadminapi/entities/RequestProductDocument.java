package com.gdas.shopadminapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestProductDocument implements Serializable {

    private String name;
    private BigDecimal price;
    private List<RequestProductComponent> components;

    public RequestProductDocument() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<RequestProductComponent> getComponents() {
        return components;
    }

    public void setComponents(List<RequestProductComponent> components) {
        this.components = components;
    }

    public static RequestProductDocument fromProduct(Product product) {
        RequestProductDocument requestProductDocument = new RequestProductDocument();
        requestProductDocument.setName(product.getName());
        requestProductDocument.setPrice(product.getPrice());
        requestProductDocument.setComponents(product.getComponents()
                .stream()
                .map(RequestProductComponent::fromProductComponent)
                .collect(Collectors.toList()));
        return requestProductDocument;
    }
}
