package com.gdas.shopadminapi.services.request;

import com.gdas.shopadminapi.entities.Request;
import com.gdas.shopadminapi.exceptions.BusinessException;
import com.gdas.shopadminapi.repositories.RequestRepository;
import com.gdas.shopadminapi.utils.DateTimeUtils;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.springframework.beans.BeanUtils.copyProperties;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@Transactional
public class RequestBasicService {

    private static final String[] IGNORE_ON_UPDATE =
            {"id", "customer", "createdAt", "canceledAt", "status", "requestProducts"};

    private final RequestRepository requestRepository;

    public RequestBasicService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public List<Request> findBetweenPeriod(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            start = DateTimeUtils.firstDayOfCurrentMonth();
            end = DateTimeUtils.lastDayOfCurrentMonth();
        }

        return requestRepository.findByDueDateBetween(start, end);
    }

    public Optional<Request> findOptionalById(Long requestId) {
        return requestRepository.findById(requestId);
    }

    public Request findById(Long requestId) {
        return findOptionalById(requestId)
            .orElseThrow(() -> new BusinessException(NOT_FOUND, "request.id.invalid", requestId));
    }

    public Request save(Request request) {
        return requestRepository.save(request);
    }

    public Request update(Request request) {
        Request existingRequest = findById(request.getId());
        copyProperties(request, existingRequest, IGNORE_ON_UPDATE);
        return requestRepository.save(existingRequest);
    }
}
