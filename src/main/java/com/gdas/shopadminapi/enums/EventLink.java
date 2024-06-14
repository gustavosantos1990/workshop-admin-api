package com.gdas.shopadminapi.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EventLink {

    REQUEST("Pedido"),
    REQUEST_PRODUCT("Produto"),
    ACQUISITION("Compra"),
    NONE("N/A");

    private final String label;

    EventLink(String label) {
        this.label = label;
    }

    public String getId() {
        return this.name();
    }

    public String getLabel() {
        return label;
    }

}
