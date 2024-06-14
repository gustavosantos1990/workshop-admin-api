package com.gdas.shopadminapi.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Measure {

    UNITY("Unidade", "unit.", false),
    CM("Centímetro", "cm", false),
    CM2("Centímetro Quadrado", "cm²", true);

    private final String label;
    private final String symbol;
    private final boolean multiDimension;

    Measure(String label, String symbol, boolean multiDimension) {
        this.label = label;
        this.symbol = symbol;
        this.multiDimension = multiDimension;
    }

    public String getId() {
        return this.name();
    }

    public String getLabel() {
        return label;
    }

    public String getSymbol() {
        return symbol;
    }

    @JsonProperty("multi_dimension")
    public boolean isMultiDimension() {
        return multiDimension;
    }
}
