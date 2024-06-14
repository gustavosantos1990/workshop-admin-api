package com.gdas.shopadminapi.repositories;

import com.gdas.shopadminapi.entities.ProductComponent;
import com.gdas.shopadminapi.entities.ProductComponentId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ProductComponentRepository extends JpaRepository<ProductComponent, ProductComponentId> {

    @Query("SELECT pc FROM ProductComponent pc JOIN FETCH pc.component WHERE pc.product.id = :productId")
    Page<ProductComponent> selectByProductIdFetchComponents(UUID productId, Pageable pageable);

    Optional<ProductComponent> findByProductComponentIdProductIdAndProductComponentIdComponentId(UUID productId, String componentId);
    Page<ProductComponent> findByProductComponentIdComponentId(String componentId, Pageable pageable);

    @Modifying
    @Query("DELETE FROM ProductComponent pc WHERE pc.product.id = :productId AND pc.component.id = :componentId")
    int deleteByProductIdAndComponentId(UUID productId, String componentId);

}
