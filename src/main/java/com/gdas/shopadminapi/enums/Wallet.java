package com.gdas.shopadminapi.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Wallet {

    OPERATIONS("Operações"),
    REVENUE("Receita"),
    COMMISSION("Comissão"),
    PROFIT("Lucro");

    private final String label;

    Wallet(String label) {
        this.label = label;
    }

    public String getId() {
        return this.name();
    }

    public String getLabel() {
        return label;
    }
}
