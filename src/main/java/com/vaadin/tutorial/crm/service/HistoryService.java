package com.vaadin.tutorial.crm.service;

import com.vaadin.tutorial.crm.entity.HistoryEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс содержит методы для доступа к таблице history
 */
@Service
public interface HistoryService {
    List<HistoryEntity> getAll();

    /**
     * Метод сохраняет свойства объекта histruEntity в таблице history
     * @param historyEntity - объект класса HistoryEntity
     */
    void saveAll(HistoryEntity historyEntity);
}
