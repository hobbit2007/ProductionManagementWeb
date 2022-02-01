package com.vaadin.tutorial.crm.service.impl.storage;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.tutorial.crm.entity.storage.CellEntity;
import com.vaadin.tutorial.crm.repository.storage.CellRepository;
import com.vaadin.tutorial.crm.service.storage.CellService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс содержащий реализацию методов интерфейса CellService
 */
@Service
public class CellImpl implements CellService {
    private final CellRepository cellRepository;

    public CellImpl(CellRepository cellRepository) {
        this.cellRepository = cellRepository;
    }

    @Override
    public List<CellEntity> getAll(Long storageID, Long locationID) {
        return cellRepository.getAll(storageID, locationID);
    }

    @Override
    public List<CellEntity> getCheckCell(String cellName, Long storageID) {
        return cellRepository.getCheckCell(cellName, storageID);
    }

    @Override
    public void saveAll(CellEntity cellEntity) {
        if (cellEntity != null)
            cellRepository.saveAndFlush(cellEntity);
        else
            Notification.show("Нет данных для записи!", 5000, Notification.Position.MIDDLE);
    }

    @Override
    public List<CellEntity> getFindCellByID(Long cellID, Long storageID) {
        return cellRepository.getFindCellByID(cellID, storageID);
    }

    @Override
    public void updateCellName(CellEntity cellEntity) {
        if (cellEntity != null)
            cellRepository.updateCellName(cellEntity.getCellName(), cellEntity.getId());
        else
            Notification.show("Нет данных для обновления!", 3000, Notification.Position.MIDDLE);
    }
}
