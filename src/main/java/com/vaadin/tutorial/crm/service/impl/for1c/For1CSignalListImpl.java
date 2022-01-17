package com.vaadin.tutorial.crm.service.impl.for1c;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.tutorial.crm.entity.for1c.For1CSignalListEntity;
import com.vaadin.tutorial.crm.repository.for1c.For1CSignalListRepository;
import com.vaadin.tutorial.crm.service.for1c.For1CSignalListService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс реализующий методы интерфейса For1CSignalListService
 */
@Service
public class For1CSignalListImpl implements For1CSignalListService {
    private final For1CSignalListRepository for1CSignalListRepository;

    public For1CSignalListImpl(For1CSignalListRepository for1CSignalListRepository) {
        this.for1CSignalListRepository = for1CSignalListRepository;
    }

    @Override
    public List<For1CSignalListEntity> getAll() {
        return for1CSignalListRepository.getAll();
    }

    @Override
    public void saveAll(For1CSignalListEntity for1CSignalListEntity) {
        if (for1CSignalListEntity != null)
            for1CSignalListRepository.saveAndFlush(for1CSignalListEntity);
        else
            Notification.show("Нет данных для записи!", 3000, Notification.Position.MIDDLE);
    }

    @Override
    public void updateDeleteRecord(Long id) {
        for1CSignalListRepository.updateDeleteRecord(id);
    }
}
