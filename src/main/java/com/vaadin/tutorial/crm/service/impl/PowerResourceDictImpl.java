package com.vaadin.tutorial.crm.service.impl;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.tutorial.crm.entity.PowerResourceDict;
import com.vaadin.tutorial.crm.repository.PowerResourceDictRepository;
import com.vaadin.tutorial.crm.service.PowerResourceDictService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс реализующий методы интерфейса PowerResourceDictService
 */
@Service
public class PowerResourceDictImpl implements PowerResourceDictService {
    private final PowerResourceDictRepository powerResourceDictRepository;

    public PowerResourceDictImpl(PowerResourceDictRepository powerResourceDictRepository) {
        this.powerResourceDictRepository = powerResourceDictRepository;
    }

    @Override
    public List<PowerResourceDict> getAll() {
        return powerResourceDictRepository.getAll();
    }

    @Override
    public void saveAll(PowerResourceDict powerResourceDict) {
        if (powerResourceDict != null)
            powerResourceDictRepository.saveAndFlush(powerResourceDict);
        else
            Notification.show("Нет данных для записи!", 5000, Notification.Position.MIDDLE);
    }
}
