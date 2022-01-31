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
}
