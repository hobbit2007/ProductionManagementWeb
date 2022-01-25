package com.vaadin.tutorial.crm.service.impl.plccontrollersimpl;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcDryingValue;
import com.vaadin.tutorial.crm.repository.plccontrollersrepository.PlcDryingRepository;
import com.vaadin.tutorial.crm.service.plccontrollersservice.PlcDryingService;

import java.util.List;

/**
 * Класс содержащий реализацию методов интерфейса PlcDryingService
 */
public class PlcDryingImpl implements PlcDryingService {
    private final PlcDryingRepository plcDryingRepository;

    public PlcDryingImpl(PlcDryingRepository plcDryingRepository) {
        this.plcDryingRepository = plcDryingRepository;
    }

    @Override
    public List<PlcDryingValue> getAllSignal() {
        return plcDryingRepository.getAllSignal();
    }

    @Override
    public void saveAll(PlcDryingValue plcDryingValue) {
        if (plcDryingValue != null)
            plcDryingRepository.saveAndFlush(plcDryingValue);
        else
            Notification.show("Нет данных для записи!", 3000, Notification.Position.MIDDLE);
    }
}
