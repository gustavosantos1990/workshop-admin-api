package com.gdas.shopadminapi.repositories;

import com.gdas.shopadminapi.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor {

    @Query(value = "SELECT * FROM product", nativeQuery = true)
    List<Product> findProductsOnly();

}
