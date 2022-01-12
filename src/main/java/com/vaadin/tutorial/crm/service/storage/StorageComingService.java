package com.vaadin.tutorial.crm.service.storage;

import com.vaadin.tutorial.crm.entity.storage.StorageComingEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс интерфейс содержащий методы доступа к таблице storage_coming
 */
@Service
public interface StorageComingService {
    List<StorageComingEntity> getAll();

    void saveAll(StorageComingEntity storageComingEntity);

    /**
     * Метод для поиска всех приходов объекта хранения по id
     * @param idMaterial - id объекта хранения
     * @return - возвращает список прихода для объекта хранения
     */
    List<StorageComingEntity> getAllByIdMaterial(Long idMaterial);
}
