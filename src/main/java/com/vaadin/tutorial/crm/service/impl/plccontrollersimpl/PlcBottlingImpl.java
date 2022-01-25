package com.vaadin.tutorial.crm.service.impl.plccontrollersimpl;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcBottlingValue;
import com.vaadin.tutorial.crm.repository.plccontrollersrepository.PlcBottlingRepository;
import com.vaadin.tutorial.crm.service.plccontrollersservice.PlcBottlingService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс содержащий реализацию методов интерфейса PlcBottlingService
 */
@Service
public class PlcBottlingImpl implements PlcBottlingService {
    private final PlcBottlingRepository plcBottlingRepository;

    public PlcBottlingImpl(PlcBottlingRepository plcBottlingRepository) {
        this.plcBottlingRepository = plcBottlingRepository;
    }

    @Override
    public List<PlcBottlingValue> getAllSignal() {
        return plcBottlingRepository.getAllSignal();
    }

    @Override
    public void saveAll(PlcBottlingValue plcBottlingValue) {
        if (plcBottlingValue != null)
            plcBottlingRepository.saveAndFlush(plcBottlingValue);
        else
            Notification.show("Нет данных для записи!", 3000, Notification.Position.MIDDLE);
    }
}
