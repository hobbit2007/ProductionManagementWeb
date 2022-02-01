package com.vaadin.tutorial.crm.service.impl.storage;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.tutorial.crm.entity.storage.LocationEntity;
import com.vaadin.tutorial.crm.repository.storage.LocationRepository;
import com.vaadin.tutorial.crm.service.storage.LocationService;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Класс реализующий методы интерфейса LocationService
 */
@Service
public class LocationImpl implements LocationService {
    private final LocationRepository locationRepository;

    public LocationImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public List<LocationEntity> getAll() {
        return locationRepository.getAll();
    }

    @Override
    public void saveAll(LocationEntity locationEntity) {
        if (locationEntity != null)
            locationRepository.saveAndFlush(locationEntity);
        else
            Notification.show("Нет данных для записи!", 3000, Notification.Position.MIDDLE);
    }

    @Override
    public List<LocationEntity> getFindLocationByID(Long locationID) {
        return locationRepository.getFindLocationByID(locationID);
    }

    @Override
    public void updateLocation(LocationEntity locationEntity) {
        if (locationEntity != null)
            locationRepository.updateLocation(locationEntity.getLocationName(), locationEntity.getId(), locationEntity.getLocationDescription());
        else
            Notification.show("Нет данных для обновления локации!", 3000, Notification.Position.MIDDLE);
    }

    @Override
    public List<LocationEntity> getCheckLocation(String locationName) {
        return locationRepository.getCheckLocation(locationName);
    }

    @Override
    public List<LocationEntity> getFindLocationByStorageID(Long storageID) {
        return locationRepository.getFindLocationByStorageID(storageID);
    }
}
