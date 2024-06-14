package com.gdas.shopadminapi.repositories;

import com.gdas.shopadminapi.entities.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProviderRepository extends JpaRepository<Provider, UUID> {
}
