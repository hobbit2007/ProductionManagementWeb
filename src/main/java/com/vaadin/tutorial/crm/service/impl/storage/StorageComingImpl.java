package com.vaadin.tutorial.crm.service.impl.storage;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.tutorial.crm.entity.storage.StorageComingEntity;
import com.vaadin.tutorial.crm.repository.storage.StorageComingRepository;
import com.vaadin.tutorial.crm.service.storage.StorageComingService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс содержащий реализацию методов интерфейса StorageComingService
 */
@Service
public class StorageComingImpl implements StorageComingService {
    private final StorageComingRepository storageComingRepository;

    public StorageComingImpl(StorageComingRepository storageComingRepository) {
        this.storageComingRepository = storageComingRepository;
    }

    @Override
    public List<StorageComingEntity> getAll() {
        return storageComingRepository.getAll();
    }

    @Override
    public void saveAll(StorageComingEntity storageComingEntity) {
        if (storageComingEntity != null)
            storageComingRepository.saveAndFlush(storageComingEntity);
        else
            Notification.show("Нет данных для записи!", 3000, Notification.Position.MIDDLE);
    }

    @Override
    public List<StorageComingEntity> getAllByIdMaterial(Long idMaterial) {
        return storageComingRepository.getAllByIdMaterial(idMaterial);
    }
}
