package com.vaadin.tutorial.crm.repository.storage;

import com.vaadin.tutorial.crm.entity.storage.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Класс репозиторий содержащий sql запросы к таблице supplier
 */
@Repository
public interface SupplierRepository extends JpaRepository<SupplierEntity, Long> {
    @Query("select s from supplier s where s.delete = 0 order by s.supplierName asc")
    List<SupplierEntity> getAll();

    //Запрос проверяет наличие поставщика в БД
    @Query("select s from supplier s where s.supplierName = :supplierName and s.delete = 0")
    List<SupplierEntity> getCheckSupplier(@Param("supplierName") String supplierName);

    //Обновляем имя и номер контракта
    @Modifying
    @Transactional
    @Query("update supplier s set s.supplierName = :supplierName, s.contract = :contract where s.id = :id")
    void updateSupplier(@Param("supplierName") String supplierName, @Param("id") Long id, @Param("contract") String contract);
}
