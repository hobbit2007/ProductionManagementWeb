package com.vaadin.tutorial.crm.service.impl;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.tutorial.crm.entity.HistoryEntity;
import com.vaadin.tutorial.crm.repository.HistoryRepository;
import com.vaadin.tutorial.crm.service.HistoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс реализующий методы интерфейса HistoryEntity
 */
@Service
public class HistoryImpl implements HistoryService {
    private final HistoryRepository historyRepository;

    public HistoryImpl(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Override
    public List<HistoryEntity> getAll() {
        return historyRepository.getAll();
    }

    @Override
    public void saveAll(HistoryEntity historyEntity) {
        if (historyEntity != null)
            historyRepository.saveAndFlush(historyEntity);
        else
            Notification.show("Нет данных для записи!", 3000, Notification.Position.MIDDLE);
    }
}
