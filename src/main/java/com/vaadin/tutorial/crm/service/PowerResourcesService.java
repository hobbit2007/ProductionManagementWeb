package com.vaadin.tutorial.crm.service;

import com.vaadin.tutorial.crm.entity.PowerResources;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс интерфейс содержащий методы для работы с таблицей power_resources
 */
@Service
public interface PowerResourcesService {
    List<PowerResources> getAll();

    void saveAll(PowerResources powerResources);
}
