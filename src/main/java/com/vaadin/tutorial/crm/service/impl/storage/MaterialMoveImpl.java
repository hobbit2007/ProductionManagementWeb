package com.vaadin.tutorial.crm.service.impl.storage;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.tutorial.crm.entity.storage.MaterialMoveEntity;
import com.vaadin.tutorial.crm.repository.storage.MaterialMoveRepository;
import com.vaadin.tutorial.crm.service.storage.MaterialMoveService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс содержащий реализацию методов интерфейса MaterialMoveService
 */
@Service
public class MaterialMoveImpl implements MaterialMoveService {
    private final MaterialMoveRepository materialMoveRepository;

    public MaterialMoveImpl(MaterialMoveRepository materialMoveRepository) {
        this.materialMoveRepository = materialMoveRepository;
    }

    @Override
    public List<MaterialMoveEntity> getAll() {
        return materialMoveRepository.getAll();
    }

    @Override
    public void saveAll(MaterialMoveEntity materialMoveEntity) {
        if (materialMoveEntity != null)
            materialMoveRepository.saveAndFlush(materialMoveEntity);
        else
            Notification.show("Нет данных для записи!", 3000, Notification.Position.MIDDLE);
    }

    @Override
    public List<MaterialMoveEntity> getAllByID(Long id, String action) {
        return materialMoveRepository.getAllByID(id, action);
    }
}
