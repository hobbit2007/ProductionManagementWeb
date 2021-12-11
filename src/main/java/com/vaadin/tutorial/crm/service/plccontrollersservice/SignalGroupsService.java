package com.vaadin.tutorial.crm.service.plccontrollersservice;

import com.vaadin.tutorial.crm.entity.plccontrollersentity.SignalGroup;

import java.util.List;

/**
 * Класс интерфейс содержащий методы для работы с таблицей groups
 */
public interface SignalGroupsService {
    List<SignalGroup> getAll();
}
