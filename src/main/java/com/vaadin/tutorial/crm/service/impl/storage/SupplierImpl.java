package com.vaadin.tutorial.crm.service.impl.storage;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.tutorial.crm.entity.storage.SupplierEntity;
import com.vaadin.tutorial.crm.repository.storage.SupplierRepository;
import com.vaadin.tutorial.crm.service.storage.SupplierService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс интерфейс содержащий реализацию методов интерфейса SupplierService
 */
@Service
public class SupplierImpl implements SupplierService {
    private final SupplierRepository supplierRepository;

    public SupplierImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public List<SupplierEntity> getAll() {
        return supplierRepository.getAll();
    }

    @Override
    public void saveAll(SupplierEntity supplierEntity) {
        if (supplierEntity != null)
            supplierRepository.saveAndFlush(supplierEntity);
        else
            Notification.show("Нет данных для записи!", 3000, Notification.Position.MIDDLE);
    }

    @Override
    public List<SupplierEntity> getCheckSupplier(String supplierName) {
        return supplierRepository.getCheckSupplier(supplierName);
    }

    @Override
    public void updateSupplier(SupplierEntity supplierEntity) {
        if (supplierEntity != null)
            supplierRepository.updateSupplier(supplierEntity.getSupplierName(), supplierEntity.getId(), supplierEntity.getContract());
        else
            Notification.show("Нет данных для обновления поставщика!", 3000, Notification.Position.MIDDLE);
    }
}
