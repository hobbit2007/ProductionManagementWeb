package com.vaadin.tutorial.crm.service.plccontrollersservice;

import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcDryingValue;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Класс интерфейс содержащий методы для работы с таблицей plccontrollervaluedrying
 */
@Service
public interface PlcDryingService {
    List<PlcDryingValue> getAllSignal();

    @Transactional
    void saveAll(PlcDryingValue plcDryingValue);
}
