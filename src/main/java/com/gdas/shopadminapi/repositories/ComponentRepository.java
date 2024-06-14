package com.gdas.shopadminapi.repositories;

import com.gdas.shopadminapi.entities.Component;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComponentRepository extends JpaRepository<Component, String> {
}
