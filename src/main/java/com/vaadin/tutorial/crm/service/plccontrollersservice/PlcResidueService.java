package com.vaadin.tutorial.crm.service.plccontrollersservice;

import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcResidueValue;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Класс интерфейс содержащий методы для работы с таблицей plccontrollervalueresidue
 */
@Service
public interface PlcResidueService {
    List<PlcResidueValue> getAllSignal();

    @Transactional
    void saveAll(PlcResidueValue plcResidueValue);
}
