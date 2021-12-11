package com.vaadin.tutorial.crm.service.plccontrollersservice;

import com.vaadin.tutorial.crm.entity.plccontrollersentity.SignalGroup;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс интерфейс содержащий методы для работы с таблицей groups
 */
@Service
public interface SignalGroupsService {
    List<SignalGroup> getAll();
}
