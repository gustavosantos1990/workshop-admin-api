package com.gdas.shopadminapi.services.product;

import com.gdas.shopadminapi.entities.Component;
import com.gdas.shopadminapi.exceptions.BusinessException;
import com.gdas.shopadminapi.repositories.ComponentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;

@Service
public class ComponentBasicService {

    Logger logger = LoggerFactory.getLogger(ComponentBasicService.class);

    private final ComponentRepository componentRepository;

    public ComponentBasicService(ComponentRepository componentRepository) {
        this.componentRepository = componentRepository;
    }

    public Component create(Component component) {
        logger.info("about to create new component: {}", component);
        return componentRepository.save(component);
    }

    public List<Component> findAll() {
        return componentRepository.findAll();
    }

    public Component findById(String componentId) {
        Optional<Component> optionalComponent = componentRepository.findById(componentId);
        return optionalComponent.orElseThrow(() ->
                new BusinessException(NOT_FOUND, "component.id.invalid", componentId));
    }

    public Component update(String componentId, Component component) {
        Component existingComponent = findById(componentId);
        updateProperties(existingComponent, component);
        return componentRepository.save(existingComponent);
    }

    private void updateProperties(Component existingComponent, Component component) {

        if(!existingComponent.getMeasure().equals(component.getMeasure())){
            throw new BusinessException(PRECONDITION_FAILED, "component.measure.update.not-allowed");
        }

        existingComponent.setName(component.getName());
        existingComponent.setCode(component.getCode());
        existingComponent.setBaseBuyPaidValue(component.getBaseBuyPaidValue());

        if (existingComponent.getMeasure().isMultiDimension()) {
            existingComponent.setBaseBuyWidth(component.getBaseBuyWidth());
            existingComponent.setBaseBuyHeight(component.getBaseBuyHeight());
            return;
        }

        existingComponent.setBaseBuyAmount(component.getBaseBuyAmount());
    }

}
