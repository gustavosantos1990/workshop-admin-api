package com.gdas.shopadminapi.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PurchaseStatus {

    CREATED("primary", "Criado"),
    DONE("success", "Finalizado");

    private final String severity;
    private final String label;

    PurchaseStatus(String severity, String label) {
        this.severity = severity;
        this.label = label;
    }

    public String getId() {
        return this.name();
    }

    public String getSeverity() {
        return severity;
    }

    public String getLabel() {
        return label;
    }
}
