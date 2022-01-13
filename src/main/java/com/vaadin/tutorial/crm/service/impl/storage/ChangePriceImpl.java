package com.vaadin.tutorial.crm.service.impl.storage;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.tutorial.crm.entity.storage.ChangePriceEntity;
import com.vaadin.tutorial.crm.repository.storage.ChangePriceRepository;
import com.vaadin.tutorial.crm.service.storage.ChangePriceService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс реализующий методы интерфейса ChangePriceService
 */
@Service
public class ChangePriceImpl implements ChangePriceService {
    private final ChangePriceRepository changePriceRepository;

    public ChangePriceImpl(ChangePriceRepository changePriceRepository) {
        this.changePriceRepository = changePriceRepository;
    }

    @Override
    public List<ChangePriceEntity> getAll() {
        return changePriceRepository.getAll();
    }

    @Override
    public List<ChangePriceEntity> getAllByIdMaterial(Long idMaterial) {
        return changePriceRepository.getAllByIdMaterial(idMaterial);
    }

    @Override
    public void saveAll(ChangePriceEntity changePriceEntity) {
        if (changePriceEntity != null)
            changePriceRepository.saveAndFlush(changePriceEntity);
        else
            Notification.show("Нет данных для записи!", 3000, Notification.Position.MIDDLE);
    }
}
