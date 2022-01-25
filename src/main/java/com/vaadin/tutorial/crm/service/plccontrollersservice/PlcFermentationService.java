package com.vaadin.tutorial.crm.service.plccontrollersservice;

import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcFermentationValue;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Класс интерфейс содержащий методы для работы с таблицей plccontrollervaluefermentation
 */
@Service
public interface PlcFermentationService {
    List<PlcFermentationValue> getAllSignal();

    @Transactional
    void saveAll(PlcFermentationValue plcFermentationValue);
}
