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
     * Метод совершает поиск всех объектов хранения в заданном складе
     * @param idStorage - id склада
     * @return - возвращает набор данных по выбранному складу
     */
    List<MaterialInfoEntity> getAllByStorage(Long idStorage);

    /**
     * Метод совершающий поиск всех объектов хранения по заданному складу и локации
     * @param idStorage - id склада
     * @param locationID - id локации
     * @return - возвращает набор данных по выбранному складу и локации
     */
    List<MaterialInfoEntity> getAllByStorageLocation(Long idStorage, Long locationID);

    /**
     * Метод совершающий поиск всех объектов хранения по заданному складу, локации и ячейке
     * @param idStorage - id склада
     * @param idCell - id ячейки
     * @return - возвращает набор данных по выбранному складу, локации и ячейке
     */
    List<MaterialInfoEntity> getAllByStorageCell(Long idStorage, Long idCell, Long idLocation);

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

    /**
     * Метод проверяет наличие объекта хранения по ID в БД
     * @param id - id объекта хранения
     * @return - возвращает список свойств найденного объекта хранения
     */
    List<MaterialInfoEntity> getCheckID(Long id);

    /**
     * Метод, который обновляет информацию об объекте хранения после перемещения склад/ячейка
     * @param materialInfoEntity - объект класса MaterialInfoEntity
     */
    void updateMaterialInfoStorageCell(MaterialInfoEntity materialInfoEntity);

    /**
     * Метод для списания объекта хранения со склада
     * @param writeOff - флаг списания
     * @param id - id объекта хранения
     */
    void updateWriteOff(Long writeOff, Long id);

    /**
     * Метод для обновления цены объекта хранения
     * @param materialInfoEntity - объект класс MaterialInfoEntity
     */
    void updatePrice(MaterialInfoEntity materialInfoEntity);

    /**
     * Метод получает последний артикул из таблицы materialinfo
     * @return - возвращает сгенерированный новый артикул
     */
    String findByLastArticle();

    /**
     * Метод записывает путь к изображению QR кода
     * @param path - путь к файлу с изображением qr кода
     * @param id - id объекта хранения
     */
    void updateQrField(String path, Long id);
}
