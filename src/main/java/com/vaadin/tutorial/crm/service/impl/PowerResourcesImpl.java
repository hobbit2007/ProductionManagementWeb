package com.vaadin.tutorial.crm.service.impl;

import com.vaadin.tutorial.crm.entity.PowerResources;
import com.vaadin.tutorial.crm.repository.PowerResourcesRepository;
import com.vaadin.tutorial.crm.service.PowerResourcesService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс реализующий методы интерфейса PowerResourcesService
 */
@Service
public class PowerResourcesImpl implements PowerResourcesService {
    private final PowerResourcesRepository powerResourcesRepository;

    public PowerResourcesImpl(PowerResourcesRepository powerResourcesRepository) {
        this.powerResourcesRepository = powerResourcesRepository;
    }

    @Override
    public List<PowerResources> getAll() {
        return null;
    }

    @Override
    public void saveAll(PowerResources powerResources) {

    }
}
