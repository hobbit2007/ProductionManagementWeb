package com.vaadin.tutorial.crm.service.storage;

import com.vaadin.tutorial.crm.entity.storage.StorageEntity;

import java.util.List;

/**
 * Класс интерфейс содержащий методы для доступа к таблице storage
 */
public interface StorageService {
    List<StorageEntity> getAll();
}
