package com.gdas.shopadminapi.controllers.rest;

import com.gdas.shopadminapi.entities.Component;
import com.gdas.shopadminapi.usecases.CreateComponentUseCase;
import com.gdas.shopadminapi.usecases.UpdateComponentUseCase;
import com.gdas.shopadminapi.services.product.ComponentBasicService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static java.lang.String.format;

@RestController
@RequestMapping("/v1/components")
public class ComponentController {

    private final ComponentBasicService componentBasicService;

    public ComponentController(ComponentBasicService componentBasicService) {
        this.componentBasicService = componentBasicService;
    }

    @GetMapping
    public ResponseEntity<List<Component>> findAllComponents() {
        List<Component> components = componentBasicService.findAll();
        return ResponseEntity.ok(components);
    }

    @GetMapping("{id}")
    public ResponseEntity<Component> findById(@PathVariable String id) {
        Component components = componentBasicService.findById(id);
        return ResponseEntity.ok(components);
    }

    @PostMapping
    public ResponseEntity<Component> create(@Validated(CreateComponentUseCase.class) @RequestBody Component component) {
        Component newComponent = componentBasicService.create(component);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path(format("/%s", newComponent.getId()))
                .buildAndExpand(newComponent)
                .toUri();
        return ResponseEntity.created(uri).body(newComponent);
    }

    @PutMapping("{id}")
    public ResponseEntity<Component> update(
            @PathVariable String id,
            @Validated(UpdateComponentUseCase.class) @RequestBody Component component) {
        Component updated = componentBasicService.update(id, component);
        return ResponseEntity.ok(updated);
    }

}
