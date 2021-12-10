package com.vaadin.tutorial.crm.service.plccontrollersservice;

import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcControllers;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс интерфейс содержит методы для работы с таблицей plccontrollers
 */
@Scope("session")
@Service
public interface PlcControllersService {
    List<PlcControllers> getAll();

    List<PlcControllers> getAllByID(long id);
}
