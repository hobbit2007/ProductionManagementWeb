package com.vaadin.tutorial.crm.service.storage;

import com.vaadin.tutorial.crm.entity.storage.SupplierEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс интерфейс содержащий методы для доступа к таблице supplier
 */
@Service
public interface SupplierService {
    List<SupplierEntity> getAll();

    void saveAll(SupplierEntity supplierEntity);

    /**
     * Метод, который проверяет наличие поставщика в БД
     * @param supplierName - имя поставщика
     * @return - возвращает массив свойств поставщика, если он есть в БД
     */
    List<SupplierEntity> getCheckSupplier(String supplierName);
}
