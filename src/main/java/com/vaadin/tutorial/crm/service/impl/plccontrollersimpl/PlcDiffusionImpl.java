package com.vaadin.tutorial.crm.service.impl.plccontrollersimpl;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcDiffusionValue;
import com.vaadin.tutorial.crm.repository.plccontrollersrepository.PlcDiffusionRepository;
import com.vaadin.tutorial.crm.service.plccontrollersservice.PlcDiffusioService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс содержащий реализацию методов интерфейса PlcDiffusionService
 */
@Service
public class PlcDiffusionImpl implements PlcDiffusioService {
    private final PlcDiffusionRepository plcDiffusionRepository;

    public PlcDiffusionImpl(PlcDiffusionRepository plcDiffusionRepository) {
        this.plcDiffusionRepository = plcDiffusionRepository;
    }

    @Override
    public List<PlcDiffusionValue> getAllSignal() {
        return plcDiffusionRepository.getAllSignal();
    }

    @Override
    public void saveAll(PlcDiffusionValue plcDiffusionValue) {
        if (plcDiffusionValue != null)
            plcDiffusionRepository.saveAndFlush(plcDiffusionValue);
        else
            Notification.show("Нет данных для записи!", 3000, Notification.Position.MIDDLE);
    }
}
