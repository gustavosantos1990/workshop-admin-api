package com.gdas.shopadminapi.repositories;

import com.gdas.shopadminapi.entities.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
