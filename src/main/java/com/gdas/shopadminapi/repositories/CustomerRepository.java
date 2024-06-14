package com.gdas.shopadminapi.repositories;

import com.gdas.shopadminapi.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}
