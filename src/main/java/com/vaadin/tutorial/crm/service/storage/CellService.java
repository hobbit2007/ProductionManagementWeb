package com.vaadin.tutorial.crm.service.storage;

import com.vaadin.tutorial.crm.entity.storage.CellEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс интерфейс содержащий методы для доступа к таблице cells
 */
@Service
public interface CellService {
    List<CellEntity> getAll(Long storageID, Long idLocation);

    /**
     * Метод, который проверяет наличие ячейки в БД
     * @param cellName - имя ячейки
     * @return - возвращает массив значений, если ячейка существует в БД или пустой массив
     */
    List<CellEntity> getCheckCell(String cellName, Long storageID);

    /**
     * Метод сохраняет созданную ячейку в таблице cells
     * @param cellEntity - объект класса CellEntity
     */
    void saveAll(CellEntity cellEntity);

    /**
     * Метод реализует поиск ячейки по ID
     * @param cellID - id ячейки
     * @param storageID - id склада
     * @return - возвращает список свойств найденной ячейки
     */
    List<CellEntity> getFindCellByID(Long cellID, Long storageID);

    /**
     * Метод для обновления имени ячейки
     * @param cellEntity - объект класса CellEntity
     */
    void updateCellName(CellEntity cellEntity);
}
