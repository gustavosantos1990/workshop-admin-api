package com.gdas.shopadminapi.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ItemType {

    DELIVERY("Entrega"),
    COMPONENT("Componente"),
    TAX("Taxa"),
    OTHER("Outro");

    private final String label;

    ItemType(String label) {
        this.label = label;
    }

    public String getId() {
        return this.name();
    }

    public String getLabel() {
        return label;
    }

}
