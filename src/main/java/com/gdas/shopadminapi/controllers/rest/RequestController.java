package com.gdas.shopadminapi.controllers.rest;

import com.gdas.shopadminapi.entities.Request;
import com.gdas.shopadminapi.usecases.CreateRequestUseCase;
import com.gdas.shopadminapi.usecases.UpdateRequestUseCase;
import com.gdas.shopadminapi.usecases.UpdateRequestStatusUseCase;
import com.gdas.shopadminapi.services.request.RequestBasicService;
import com.gdas.shopadminapi.services.request.RequestCreatorService;
import com.gdas.shopadminapi.services.request.RequestStatusUpdaterService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static java.lang.String.format;

@RestController
@RequestMapping("/v1/requests")
public class RequestController {

    private final RequestBasicService requestBasicService;
    private final RequestCreatorService requestCreatorService;
    private final RequestStatusUpdaterService requestStatusUpdaterService;

    public RequestController(RequestBasicService requestBasicService,
                             RequestCreatorService requestCreatorService,
                             RequestStatusUpdaterService requestStatusUpdaterService) {
        this.requestBasicService = requestBasicService;
        this.requestCreatorService = requestCreatorService;
        this.requestStatusUpdaterService = requestStatusUpdaterService;
    }

    @GetMapping
    public ResponseEntity<List<Request>> findAllRequests(
            @RequestParam(name = "start_date", required = false) LocalDate startDate,
            @RequestParam(name = "end_date", required = false) LocalDate endDate
    ) {
        List<Request> requests = requestBasicService.findBetweenPeriod(startDate, endDate);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Request> findById(@PathVariable Long requestId) {
        Request request = requestBasicService.findById(requestId);
        return ResponseEntity.ok(request);
    }

    @PostMapping
    public ResponseEntity<Request> create(@Validated(CreateRequestUseCase.class) @RequestBody Request component) {
        Request newRequest = requestCreatorService.create(component);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path(format("/%s", newRequest.getId()))
                .buildAndExpand(newRequest)
                .toUri();
        return ResponseEntity.created(uri).body(newRequest);
    }

    @PutMapping("/{requestId}")
    public ResponseEntity<Request> update(
            @PathVariable Long requestId,
            @Validated(UpdateRequestUseCase.class) @RequestBody Request request) {
        request.setId(requestId);
        Request updated = requestBasicService.update(request);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{requestId}")
    public ResponseEntity<Request> updateStatus(
            @PathVariable Long requestId,
            @Validated(UpdateRequestStatusUseCase.class) @RequestBody Request request) {
        request.setId(requestId);
        Request updated = requestStatusUpdaterService.updateStatus(request);
        return ResponseEntity.ok(updated);
    }

}
