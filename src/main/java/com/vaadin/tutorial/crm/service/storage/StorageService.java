package com.vaadin.tutorial.crm.service.storage;

import com.vaadin.tutorial.crm.entity.storage.StorageEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс интерфейс содержащий методы для доступа к таблице storage
 */
@Service
public interface StorageService {
    List<StorageEntity> getAll();

    /**
     * Метод проверяет наличие склада в БД
     * @param storeName - название проверяемого склада
     * @return - возвращает массив значений, если склад есть в БД или пустой массив, если нет
     */
    List<StorageEntity> getCheckStorage(String storeName);

    /**
     * Метод сохраняет данные в таблице storage
     * @param storageEntity - объект класса StorageEntity с заполненными свойствами для сохранения
     */
    void saveAll(StorageEntity storageEntity);

    /**
     * Метод реализует поиск склада по ID
     * @param storageID - id склада
     * @return - возвратит список свойств найденного склада
     */
    List<StorageEntity> getFindStorageByID(Long storageID);
}
