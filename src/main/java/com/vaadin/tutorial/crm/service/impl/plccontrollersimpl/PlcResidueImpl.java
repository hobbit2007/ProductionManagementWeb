package com.vaadin.tutorial.crm.service.impl.plccontrollersimpl;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcResidueValue;
import com.vaadin.tutorial.crm.repository.plccontrollersrepository.PlcResidueRepository;
import com.vaadin.tutorial.crm.service.plccontrollersservice.PlcResidueService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс содержащий реализацию методов интерфейса PlcResidueService
 */
@Service
public class PlcResidueImpl implements PlcResidueService {
    private final PlcResidueRepository plcResidueRepository;

    public PlcResidueImpl(PlcResidueRepository plcResidueRepository) {
        this.plcResidueRepository = plcResidueRepository;
    }

    @Override
    public List<PlcResidueValue> getAllSignal() {
        return plcResidueRepository.getAllSignal();
    }

    @Override
    public void saveAll(PlcResidueValue plcResidueValue) {
        if (plcResidueValue != null)
            plcResidueRepository.saveAndFlush(plcResidueValue);
        else
            Notification.show("Нет данных для записи!", 3000, Notification.Position.MIDDLE);
    }
}
