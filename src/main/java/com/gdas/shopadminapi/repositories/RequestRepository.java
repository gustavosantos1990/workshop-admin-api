package com.gdas.shopadminapi.repositories;

import com.gdas.shopadminapi.entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findByDueDateBetween(LocalDate startDate, LocalDate endDate);

}
