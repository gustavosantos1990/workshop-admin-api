package com.gdas.shopadminapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gdas.shopadminapi.enums.Measure;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestProductComponent implements Serializable {

    private String component;

    private Measure measure;

    private BigDecimal boughtWidth;

    private BigDecimal boughtHeight;

    private BigDecimal boughtAmount;

    @JsonProperty("paid_value")
    private BigDecimal paidValue;

    private BigDecimal width;

    private BigDecimal height;

    private BigDecimal amount;

    private BigDecimal cost;

    public RequestProductComponent() {
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public BigDecimal getBoughtWidth() {
        return boughtWidth;
    }

    public void setBoughtWidth(BigDecimal boughtWidth) {
        this.boughtWidth = boughtWidth;
    }

    public BigDecimal getBoughtHeight() {
        return boughtHeight;
    }

    public void setBoughtHeight(BigDecimal boughtHeight) {
        this.boughtHeight = boughtHeight;
    }

    public BigDecimal getBoughtAmount() {
        return boughtAmount;
    }

    public void setBoughtAmount(BigDecimal boughtAmount) {
        this.boughtAmount = boughtAmount;
    }

    public Measure getMeasure() {
        return measure;
    }

    public void setMeasure(Measure measure) {
        this.measure = measure;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPaidValue() {
        return paidValue;
    }

    public void setPaidValue(BigDecimal paidValue) {
        this.paidValue = paidValue;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public static RequestProductComponent fromProductComponent(ProductComponent productComponent) {
        RequestProductComponent requestProductComponent = new RequestProductComponent();

        requestProductComponent.setComponent(productComponent.getComponent().getName());
        requestProductComponent.setMeasure(productComponent.getComponent().getMeasure());
        requestProductComponent.setBoughtHeight(productComponent.getComponent().getBaseBuyHeight());
        requestProductComponent.setBoughtWidth(productComponent.getComponent().getBaseBuyWidth());
        requestProductComponent.setBoughtAmount(productComponent.getComponent().getBaseBuyAmount());
        requestProductComponent.setPaidValue(productComponent.getComponent().getBaseBuyPaidValue());
        requestProductComponent.setHeight(productComponent.getHeight());
        requestProductComponent.setWidth(productComponent.getWidth());
        requestProductComponent.setAmount(productComponent.getAmount());
        requestProductComponent.setCost(productComponent.getCost());

        return requestProductComponent;
    }
}
