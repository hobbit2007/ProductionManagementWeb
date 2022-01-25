package com.vaadin.tutorial.crm.service.impl.plccontrollersimpl;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcFermentationValue;
import com.vaadin.tutorial.crm.repository.plccontrollersrepository.PlcFermentationRepository;
import com.vaadin.tutorial.crm.service.plccontrollersservice.PlcFermentationService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс содержащий реализацию методов интерфейса PlcFermentationService
 */
@Service
public class PlcFermentationImpl implements PlcFermentationService {
    private final PlcFermentationRepository plcFermentationRepository;

    public PlcFermentationImpl(PlcFermentationRepository plcFermentationRepository) {
        this.plcFermentationRepository = plcFermentationRepository;
    }

    @Override
    public List<PlcFermentationValue> getAllSignal() {
        return plcFermentationRepository.getAllSignal();
    }

    @Override
    public void saveAll(PlcFermentationValue plcFermentationValue) {
        if (plcFermentationValue != null)
            plcFermentationRepository.saveAndFlush(plcFermentationValue);
        else
            Notification.show("Нет данных для записи!", 3000, Notification.Position.MIDDLE);
    }
}
