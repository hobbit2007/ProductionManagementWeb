package com.vaadin.tutorial.crm.service.storage;

import com.vaadin.tutorial.crm.entity.storage.MaterialMoveEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс интерфейс содержащий методы для доступа к таблице material_move
 */
@Service
public interface MaterialMoveService {
    List<MaterialMoveEntity> getAll();

    void saveAll(MaterialMoveEntity materialMoveEntity);
}
