package com.vaadin.tutorial.crm.service.plccontrollersservice;

import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcBottlingValue;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Класс интерфейс содержащий методы для работы с таблицей plccontrollervaluebottling
 */
@Service
public interface PlcBottlingService {
    List<PlcBottlingValue> getAllSignal();

    @Transactional
    void saveAll(PlcBottlingValue plcBottlingValue);
}
