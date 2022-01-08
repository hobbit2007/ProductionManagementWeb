package com.vaadin.tutorial.crm.service.storage;

import com.vaadin.tutorial.crm.entity.storage.CellEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс интерфейс содержащий методы для доступа к таблице cells
 */
@Service
public interface CellService {
    List<CellEntity> getAll();

    /**
     * Метод, который проверяет наличие ячейки в БД
     * @param cellName - имя ячейки
     * @return - возвращает массив значений, если ячейка существует в БД или пустой массив
     */
    List<CellEntity> getCheckCell(String cellName);

    /**
     * Метод сохраняет созданную ячейку в таблице cells
     * @param cellEntity - объект класса CellEntity
     */
    void saveAll(CellEntity cellEntity);
}
