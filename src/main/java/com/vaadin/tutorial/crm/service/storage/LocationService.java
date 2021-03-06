package com.vaadin.tutorial.crm.service.storage;

import com.vaadin.tutorial.crm.entity.storage.LocationEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс интерфейс содержащий методы для доступа к таблице store_location
 */
@Service
public interface LocationService {
    List<LocationEntity> getAll();

    void saveAll(LocationEntity locationEntity);

    /**
     * Метод для поиска локации по id
     * @param locationID - id локации
     * @return - возвращает список локации и ее свойств
     */
    List<LocationEntity> getFindLocationByID(Long locationID);

    /**
     * Метод, который обновляет имя и описание локации
     * @param locationEntity - объект класса LocationEntity
     */
    void updateLocation(LocationEntity locationEntity);

    /**
     * Метод, который проверяет наличие локации в БД
     * @param locationName - имя локации
     * @return - возвращает массив содержащий свойства локации, если она существует в БД
     */
    List<LocationEntity> getCheckLocation(String locationName);

    /**
     * Метод для поиска всех локаций принадлежащих складу
     * @param storageID - id склада
     * @return - возвращает массив всех локаций по заданному id склада
     */
    List<LocationEntity> getFindLocationByStorageID(Long storageID);
}
