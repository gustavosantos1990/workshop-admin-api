package com.gdas.shopadminapi.repositories;

import com.gdas.shopadminapi.entities.RequestProduct;
import com.gdas.shopadminapi.entities.RequestProductId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestProductRepository extends JpaRepository<RequestProduct, RequestProductId> {
    List<RequestProduct> findByRequestId(Long requestId);
}
