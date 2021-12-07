package com.vaadin.tutorial.crm.service.plccontrollersservice;

import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcControllers;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс интерфейс содержит методы для работы с таблицей plccontrollers
 */
@Service
public interface PlcControllersService {
    List<PlcControllers> getAll();

    List<PlcControllers> getAllByID(long id);
}
