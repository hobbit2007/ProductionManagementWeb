package com.vaadin.tutorial.crm.service.storage;

import com.vaadin.tutorial.crm.entity.storage.MaterialInfoEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс интерфейс содержащий методы для доступа к таблице materialinfo
 */
@Service
public interface MaterialInfoService {
    List<MaterialInfoEntity> getAll();

    /**
     * Метод проверяет наличие объекта хранения по артикулу в БД
     * @param article - введенный артикул
     * @return - возвращает массив, если не пустой, то объект хранения существует в БД, если пустой, то не существует
     */
    List<MaterialInfoEntity> getCheckArticle(String article);

    /**
     * Метод сохраняет в БД введенную информацию по объекту хранения
     * @param materialInfoEntity - объект класса MaterialInfoEntity
     */
    void saveAll(MaterialInfoEntity materialInfoEntity);

    /**
     * Метод, который обновляет некоторые значения в таблице materialinfo
     * @param materialInfoEntity - объект класса MaterialInfoEntity
     */
    void updateValue(MaterialInfoEntity materialInfoEntity);

    /**
     * Метод, который обновляет только поле приход
     * @param materialInfoEntity - объект класса MaterialInfoEntity
     */
    void updatePrihod(MaterialInfoEntity materialInfoEntity);

    /**
     * Метод совершающий поиск по заданному складу
     * @param idStorage - id склада
     * @return - возвращает набор данных по выбранному складу
     */
    List<MaterialInfoEntity> getAllByStorage(Long idStorage);

    /**
     * Метод совершающий поиск по заданному складу и ячейке
     * @param idStorage - id склада
     * @param idCell - id ячейки
     * @return - возвращает набор данных по выбранному складу и ячейке
     */
    List<MaterialInfoEntity> getAllByStorageCell(Long idStorage, Long idCell);

    /**
     * Метод совершающий поиск по заданному артикулу
     * @param article - артикул объекта хранения
     * @return - возвращает набор данных по выбранному артикулу
     */
    List<MaterialInfoEntity> getAllByArticle(String article, Long idStore);

    /**
     * Метод осуществляющий поиск по названию объекта хранения
     * @param materialName - наименование объекта хранения
     * @return - возвращает набор данных по выбранному объекту хранения
     */
    List<MaterialInfoEntity> getAllByMaterialName(String materialName, Long idStore);
}
