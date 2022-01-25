package com.vaadin.tutorial.crm.service.plccontrollersservice;

import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcDiffusionValue;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Класс интерфейс содержащий методы для работы с таблицей plccontrollervaluedifusion
 */
@Service
public interface PlcDiffusioService {
    List<PlcDiffusionValue> getAllSignal();

    @Transactional
    void saveAll(PlcDiffusionValue plcDiffusionValue);
}
