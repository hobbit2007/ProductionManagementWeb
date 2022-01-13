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

    /**
     * Метод для поиска всех записей по id объекта хранения
     * @param id - id объекта хранения
     * @param action - действие по складу, например перемещение склад/ячейка
     * @return - возвращает массив найденных значений
     */
    List<MaterialMoveEntity> getAllByID(Long id, String action);

    /**
     * Метод для поиска записи по id для списанного объекта хранения
     * @param id - id объекта хранения
     * @param action - действия по складу с объектом хранения, например перемещение склад/ячейка
     * @return - возвращает массив найденных значений
     */
    List<MaterialMoveEntity> getAllWriteOffByID(Long id, String action);
}
