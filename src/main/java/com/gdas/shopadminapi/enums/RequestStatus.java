package com.gdas.shopadminapi.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RequestStatus {

    ESTIMATE(0, "primary", "Orçamento"),
    ACTIVE(1, "primary", "Ativo"),
    DELIVERED(2, "info", "Entregue"),
    UNDER_BILLING(3, "warning", "Faturamento Iniciado"),
    BILLING_REIMBURSEMENTS(4, "warning", "Faturando reembolsos"),
    BILLING_RESULTS(5, "warning", "Faturando resultados"),
    BILLED(6, "secondary", "Faturado"),
    DONE(7, "success", "Finalizado"),
    CANCELED(8, "danger", "Cancelado");

    private final int sequence;
    private final String severity;
    private final String label;

    RequestStatus(int sequence, String severity, String label) {
        this.sequence = sequence;
        this.severity = severity;
        this.label = label;
    }

    public String getId() {
        return this.name();
    }

    public int getSequence() {
        return sequence;
    }

    public String getSeverity() {
        return severity;
    }

    public String getLabel() {
        return label;
    }
}
