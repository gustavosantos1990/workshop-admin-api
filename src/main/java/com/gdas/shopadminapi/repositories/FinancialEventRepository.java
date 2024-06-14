package com.gdas.shopadminapi.repositories;

import com.gdas.shopadminapi.entities.FinancialEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface FinancialEventRepository extends JpaRepository<FinancialEvent, Long> {

    List<FinancialEvent> findByRequestId(Long requestId);

    @Query("SELECT SUM(f.value) FROM FinancialEvent f WHERE f.date < :date AND f.bankingOperation = true")
    BigDecimal balanceBeforeStartDate(LocalDate date);

    @Query("SELECT SUM(f.value) FROM FinancialEvent f WHERE f.bankingOperation = true")
    BigDecimal balanceOfAllTime();

    List<FinancialEvent> findByDateBetween(LocalDate start, LocalDate end);

}
