package com.vaadin.tutorial.crm.service.plccontrollersservice;

import com.vaadin.tutorial.crm.entity.plccontrollersentity.SignalList;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс модель содержащий методы для работы с таблицей signallist
 */
@Scope("session")
@Service
public interface SignalListService {
    List<SignalList> findSignalList(Long controllerId);

    Long countGroups(Long controllerId);

    void saveAll(SignalList signalList);

    /**
     * Метод, который обновляет некоторые значения в таблице signallist
     * @param signalList - объект класса SignalList
     */
    void updateValue(SignalList signalList);
}
