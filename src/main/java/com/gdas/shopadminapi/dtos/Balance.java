package com.gdas.shopadminapi.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gdas.shopadminapi.entities.FinancialEvent;

import java.math.BigDecimal;
import java.util.List;

public class Balance {

    @JsonProperty("balance_before_period")
    private BigDecimal balanceBeforePeriod;

    @JsonProperty("all_time_balance")
    private BigDecimal allTimeBalance;

    private List<FinancialEvent> events;

    public Balance() {
    }

    public Balance(BigDecimal balanceBeforePeriod, BigDecimal allTimeBalance, List<FinancialEvent> events) {
        this.balanceBeforePeriod = balanceBeforePeriod;
        this.allTimeBalance = allTimeBalance;
        this.events = events;
    }

    public BigDecimal getBalanceBeforePeriod() {
        return balanceBeforePeriod;
    }

    public void setBalanceBeforePeriod(BigDecimal balanceBeforePeriod) {
        this.balanceBeforePeriod = balanceBeforePeriod;
    }

    public List<FinancialEvent> getEvents() {
        return events;
    }

    public void setEvents(List<FinancialEvent> events) {
        this.events = events;
    }

    public BigDecimal getAllTimeBalance() {
        return allTimeBalance;
    }

    public void setAllTimeBalance(BigDecimal allTimeBalance) {
        this.allTimeBalance = allTimeBalance;
    }
}
