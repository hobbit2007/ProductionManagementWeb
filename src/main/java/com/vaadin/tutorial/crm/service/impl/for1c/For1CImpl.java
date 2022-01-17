package com.vaadin.tutorial.crm.service.impl.for1c;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.tutorial.crm.entity.for1c.For1CEntity;
import com.vaadin.tutorial.crm.repository.for1c.For1CRepository;
import com.vaadin.tutorial.crm.service.for1c.For1CService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс реализующий методы интерфейса For1CService
 */
@Service
public class For1CImpl implements For1CService {
    private final For1CRepository for1CRepository;

    public For1CImpl(For1CRepository for1CRepository) {
        this.for1CRepository = for1CRepository;
    }

    @Override
    public List<For1CEntity> getAll() {
        return for1CRepository.getAll();
    }

    @Override
    public void saveAll(For1CEntity for1CEntity) {
        if (for1CEntity != null)
            for1CRepository.saveAndFlush(for1CEntity);
        else
            Notification.show("Нет данных для записи!", 3000, Notification.Position.MIDDLE);
    }

    @Override
    public void updateAfterLoadTo1C() {
        for1CRepository.updateAfterLoadTo1C();
    }
}
