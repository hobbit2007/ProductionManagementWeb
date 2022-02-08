package com.vaadin.tutorial.crm.repository.storage;

import com.vaadin.tutorial.crm.entity.storage.MaterialInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы к таблице materialinfo
 */
@Repository
public interface MaterialInfoRepository extends JpaRepository<MaterialInfoEntity, Long> {
    @Query("select mi from materialinfo mi where mi.delete = 0 and mi.writeoff = 0 order by mi.dateCreate asc")
    List<MaterialInfoEntity> getAll();

    //Поиск по заданному складу
    @Query("select mi from materialinfo mi where mi.delete = 0 and mi.writeoff = 0 and mi.idStorage = :idStorage order by mi.dateCreate asc")
    List<MaterialInfoEntity> getAllByStorage(@Param("idStorage") Long idStorage);
    //Поиск по заданному складу и локации
    @Query("select mi from materialinfo mi where mi.delete = 0 and mi.writeoff = 0 and mi.idLocation = :locationID and mi.idStorage = :idStorage order by mi.dateCreate asc")
    List<MaterialInfoEntity> getAllByStorageLocation(@Param("idStorage") Long idStorage, @Param("locationID") Long locationID);

    //Поиск по заданному складу, локации и ячейке
    @Query("select mi from materialinfo mi where mi.delete = 0 and mi.writeoff = 0 and mi.idStorage = :idStorage and mi.idLocation = :idLocation and mi.idCell = :idCell order by mi.dateCreate asc")
    List<MaterialInfoEntity> getAllByStorageCell(@Param("idStorage") Long idStorage, @Param("idCell") Long idCell, @Param("idLocation") Long idLocation);

    //Поиск по заданному артикулу
    @Query("select mi from materialinfo mi where mi.delete = 0 and mi.writeoff = 0 and mi.article = :article and mi.idStorage = :idStorage order by mi.dateCreate asc")
    List<MaterialInfoEntity> getAllByArticle(@Param("article") String article, @Param("idStorage") Long idStorage);

    //Поиск по названию объекта хранения
    @Query("select mi from materialinfo mi where mi.delete = 0 and mi.writeoff = 0 and mi.materialName = :materialName and mi.idStorage = :idStorage order by mi.dateCreate asc")
    List<MaterialInfoEntity> getAllByMaterialName(@Param("materialName") String materialName, @Param("idStorage") Long idStorage);

    //Проверяем наличие объекта хранения по артикулу в БД
    @Query("select mi from materialinfo mi where mi.article = :article and mi.writeoff = 0 and mi.delete = 0")
    List<MaterialInfoEntity> getCheckArticle(@Param("article") String article);

    //Проверяем наличие объекта хранения по ID в БД
    @Query("select mi from materialinfo mi where mi.id = :id and mi.delete = 0")
    List<MaterialInfoEntity> getCheckID(@Param("id") Long id);

    //Обновляем некоторые значения в таблице materialinfo
    @Modifying
    @Transactional //Добавляем эту аннотацию, чтоб насильно сделать апдейт, даже если он запрещен
    @Query("update materialinfo mi set mi.materialName = :materialName, mi.article = :article " +
            "where mi.id = :id")
    void updateValue(@Param("materialName") String materialName, @Param("article") String article, @Param("id") long id);

    //Обновляем только поле приход и остаток
    @Modifying
    @Transactional
    @Query("update materialinfo mi set mi.quantity = :prihodNew, mi.balance = :balance where mi.id = :id")
    void updatePrihod(@Param("prihodNew") double prihodNew, @Param("id") long id, @Param("balance") double balance);

    //Обновляем информацию об объекте хранения после перемещения склад/ячейка
    @Modifying
    @Transactional
    @Query("update materialinfo mi set mi.flagMove = :flagMove, mi.idStorage = :storageID, mi.idLocation = :locationID, mi.idCell = :cellID, " +
            "mi.balance = :balance, mi.expense = :expense where mi.id = :id")
    void updateMaterialInfoStorageCell(@Param("flagMove") long flagMove, @Param("storageID") long storageID,
                                       @Param("cellID") long cellID, @Param("balance") double balance, @Param("expense") double expense,
                                       @Param("id") long id, @Param("locationID") long locationID);

    //Списание объекта хранения со склада
    @Modifying
    @Transactional
    @Query("update materialinfo mi set mi.writeoff = :writeoff where mi.id = :id")
    void updateWriteOff(@Param("writeoff") long writeoff, @Param("id") long id);

    //Обновление цены объекта хранения
    @Modifying
    @Transactional
    @Query("update materialinfo mi set mi.costPrice = :costPrice, mi.marketPrice = :marketPrice, mi.diffPrice = :diffPrice where mi.id = :id")
    void updatePrice(@Param("costPrice") double costPrice, @Param("marketPrice") double marketPrice, @Param("diffPrice") double diffPrice,
                     @Param("id") long id);

    //Получаем последний артикул из таблицы materialinfo для генерации следующего
    @Query("select mi.article from materialinfo mi where mi.id = (select max(id) from materialinfo )")
    String findByLastArticle();

    //Обновляем путь к qr коду
    @Modifying
    @Transactional
    @Query("update materialinfo mi set mi.qrNewMaterial = :qrNewMaterial where mi.id = :id")
    void updateQrField(@Param("qrNewMaterial") String qrNewMaterial, @Param("id") long id);

}
