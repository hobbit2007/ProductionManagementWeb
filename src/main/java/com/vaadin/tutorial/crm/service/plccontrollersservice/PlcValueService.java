package com.vaadin.tutorial.crm.service.plccontrollersservice;

import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcValue;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Класс интерфейс содержащий методы для работы с таблицей plcwashing101
 */
@Scope("session")
@Service
public interface PlcValueService {
    List<PlcValue> getSignalOnController(Long controllerId);

    @Transactional
    void saveAll(PlcValue plcValue);
}
