package com.gdas.shopadminapi.repositories;

import com.gdas.shopadminapi.entities.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {
    List<PurchaseItem> findByPurchaseId(Long purchaseId);
    Optional<PurchaseItem> findByPurchaseIdAndId(Long purchaseId, Long itemId);
}
