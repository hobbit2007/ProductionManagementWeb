package com.vaadin.tutorial.crm.service.impl.storage;

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
}
