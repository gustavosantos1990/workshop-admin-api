package com.gdas.shopadminapi.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gdas.shopadminapi.records.SubEvent;

import java.util.List;

import static com.gdas.shopadminapi.enums.Operation.CREDIT;
import static com.gdas.shopadminapi.enums.Operation.DEBT;
import static com.gdas.shopadminapi.enums.Wallet.*;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Event {

    ADVANCE_OR_PAYMENT_OF_REQUEST(0, "Adiantamento ou Pagamento de Pedido", true, false, List.of(new SubEvent(CREDIT, REVENUE))),
    PICKUP_OR_DELIVERY_OF_REQUEST(0, "Retirada ou Entrega de Pedido", true, false, List.of(new SubEvent(DEBT, OPERATIONS))),
    PRODUCTION_COST(1, "Custo de Produção", false, false, List.of(new SubEvent(DEBT, OPERATIONS))),
    EXTRA_EXPENSES_OF_REQUEST(1, "Despesas Adicionais do Pedido", true, false, List.of(new SubEvent(DEBT, OPERATIONS))),
    DELIVERY_REIMBURSEMENT(1, "Reembolso de Entrega e/ou Retirada", false, false, List.of(new SubEvent(DEBT, REVENUE), new SubEvent(CREDIT, OPERATIONS))),
    COST_REIMBURSEMENT(1, "Reembolso de Custos de Produção", false, false, List.of(new SubEvent(DEBT, REVENUE), new SubEvent(CREDIT, OPERATIONS))),
    EXTRA_EXPENSES_REIMBURSEMENT(1, "Reembolso de Despesas Adicionais do Pedido", true, false, List.of(new SubEvent(DEBT, REVENUE), new SubEvent(CREDIT, OPERATIONS))),
    EXPENSE_REIMBURSEMENT_OF_RESULTS(2, "Reembolso de Despesas do Pedido", false, true, List.of(new SubEvent(DEBT, REVENUE), new SubEvent(CREDIT, OPERATIONS))),
    COMMISSION_OF_RESULTS(2, "Comissão", false, true, List.of(new SubEvent(DEBT, REVENUE), new SubEvent(CREDIT, COMMISSION))),
    PROFIT_OF_RESULTS(2, "Lucro", false, true, List.of(new SubEvent(DEBT, REVENUE), new SubEvent(CREDIT, PROFIT))),
    COMMISSION_PAYMENT(-1, "Pagamento de Comissão", true, false, List.of(new SubEvent(DEBT, COMMISSION))),
    PROFIT_WITHDRAWAL(-1, "Retirada de Lucro", true, false, List.of(new SubEvent(DEBT, PROFIT))),
    PURCHASE(-1, "Compra", true, false, List.of(new SubEvent(DEBT, OPERATIONS)));
//    GENERIC_INCOME(-1, "Depósito Genérico", true, OPERATIONS),
//    GENERIC_OUTCOME(-1, "Débito Genérico", true, OPERATIONS),
//    BALANCE_MOVEMENT_FROM_OPERATIONS_TO_REVENUE(-1, "Movimentação de Saldo de Operações para Receita", false, REVENUE),
//    BALANCE_MOVEMENT_FROM_OPERATIONS_TO_PROFIT(-1, "Movimentação de Saldo de Operações para Lucro", false, Wallet.PROFIT),
//    BALANCE_MOVEMENT_FROM_OPERATIONS_TO_COMMISSION(-1, "Movimentação de Saldo de Operações para Comissão", false, Wallet.COMMISSION),
//    BALANCE_MOVEMENT_FROM_REVENUE_TO_OPERATIONS(-1, "Movimentação de Saldo de Receita para Operações", false, OPERATIONS),
//    BALANCE_MOVEMENT_FROM_REVENUE_TO_PROFIT(-1, "Movimentação de Saldo de Receita para Lucro", false, Wallet.PROFIT),
//    BALANCE_MOVEMENT_FROM_REVENUE_TO_COMMISSION(-1, "Movimentação de Saldo de Receita para Comissão", false, Wallet.COMMISSION),
//    BALANCE_MOVEMENT_FROM_PROFIT_TO_OPERATIONS(-1, "Movimentação de Saldo de Lucro para Operações", false, OPERATIONS),
//    BALANCE_MOVEMENT_FROM_PROFIT_TO_REVENUE(-1, "Movimentação de Saldo de Lucro para Receita", false, REVENUE),
//    BALANCE_MOVEMENT_FROM_PROFIT_TO_COMMISSION(-1, "Movimentação de Saldo de Lucro para Comissão", false, Wallet.COMMISSION),
//    BALANCE_MOVEMENT_FROM_COMMISSION_TO_OPERATIONS(-1, "Movimentação de Saldo de Comissão para Operações", false, OPERATIONS),
//    BALANCE_MOVEMENT_FROM_COMMISSION_TO_REVENUE(-1, "Movimentação de Saldo de Comissão para Receita", false, REVENUE),
//    BALANCE_MOVEMENT_FROM_COMMISSION_TO_PROFIT(-1, "Movimentação de Saldo de Comissão para Lucro", false, Wallet.PROFIT);

    private final Integer phase;
    private final String label;
    private final boolean bankingOperation;
    private final boolean percentageRequired;
    private final List<SubEvent> subEvents;

    Event(Integer phase, String label, boolean bankingOperation, boolean percentageRequired, List<SubEvent> subEvents) {
        this.phase = phase;
        this.label = label;
        this.bankingOperation = bankingOperation;
        this.percentageRequired = percentageRequired;
        this.subEvents = subEvents;
    }

    public String getId() {
        return this.name();
    }

    public Integer getPhase() {
        return phase;
    }

    public String getLabel() {
        return label;
    }

    @JsonProperty("banking_operation")
    public boolean isBankingOperation() {
        return bankingOperation;
    }

    public boolean isPercentageRequired() {
        return percentageRequired;
    }

    public List<SubEvent> getSubEvents() {
        return subEvents;
    }
}
