package com.vaadin.tutorial.crm.service.plccontrollersservice;

import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcWashing;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Класс интерфейс содержащий методы для работы с таблицей plcwashing101
 */
@Service
public interface PlcWashingService {
    List<PlcWashing> getAll();
}
