package com.vaadin.tutorial.crm.service.impl.storage;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.tutorial.crm.entity.storage.StorageEntity;
import com.vaadin.tutorial.crm.repository.storage.StorageRepository;
import com.vaadin.tutorial.crm.service.storage.StorageService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс содержащий реализацию методов интерфейса StorageService
 */
@Service
public class StorageImpl implements StorageService {
    private final StorageRepository storageRepository;

    public StorageImpl(StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    @Override
    public List<StorageEntity> getAll() {
        return storageRepository.getAll();
    }

    @Override
    public List<StorageEntity> getCheckStorage(String storeName) {
        return storageRepository.getCheckStorage(storeName);
    }

    @Override
    public void saveAll(StorageEntity storageEntity) {
        if (storageEntity != null)
            storageRepository.saveAndFlush(storageEntity);
        else
            Notification.show("Нет данных для записи!", 5000, Notification.Position.MIDDLE);
    }

    @Override
    public List<StorageEntity> getFindStorageByID(Long storageID) {
        return storageRepository.getFindStorageByID(storageID);
    }

    @Override
    public void updateStorageName(StorageEntity storageEntity) {
        if (storageEntity != null)
            storageRepository.updateStorageName(storageEntity.getStorageName(), storageEntity.getId());
        else
            Notification.show("Нет данных для обновления!", 3000, Notification.Position.MIDDLE);
    }

    @Override
    public List<StorageEntity> getStorageByLocationID(Long locationID) {
        return storageRepository.getStorageByLocationID(locationID);
    }
}
