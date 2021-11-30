package com.vaadin.tutorial.crm.service.plccontrollersservice;

import com.vaadin.tutorial.crm.entity.plccontrollersentity.SignalList;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс модель содержащий методы для работы с таблицей signallist
 */
@Service
public interface SignalListService {
    List<SignalList> findSignalList(Long controllerId);
}
