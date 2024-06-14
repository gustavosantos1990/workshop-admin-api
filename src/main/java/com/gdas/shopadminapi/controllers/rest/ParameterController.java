package com.gdas.shopadminapi.controllers.rest;

import com.gdas.shopadminapi.enums.ItemType;
import com.gdas.shopadminapi.enums.Event;
import com.gdas.shopadminapi.enums.Measure;
import com.gdas.shopadminapi.enums.RequestStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/v1/parameters")
public class ParameterController {

    @GetMapping("/measures")
    public ResponseEntity<List<Measure>> measures() {
        return ResponseEntity.ok(Arrays.asList(Measure.values()));
    }

    @GetMapping("/requests/statuses")
    public ResponseEntity<List<RequestStatus>> requestStatues() {
        return ResponseEntity.ok(Arrays.asList(RequestStatus.values()));
    }

    @GetMapping("/financial_events")
    public ResponseEntity<List<Event>> financialEvents() {
        return ResponseEntity.ok(Arrays.asList(Event.values()));
    }

    @GetMapping("/purchases/items/types")
    public ResponseEntity<List<ItemType>> purchaseItemsTypes() {
        return ResponseEntity.ok(Arrays.asList(ItemType.values()));
    }

}
