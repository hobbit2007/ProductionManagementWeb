package com.vaadin.tutorial.crm.service.impl.storage;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.tutorial.crm.entity.storage.MaterialInfoEntity;
import com.vaadin.tutorial.crm.repository.storage.MaterialInfoRepository;
import com.vaadin.tutorial.crm.service.storage.MaterialInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс содержащий реализацию методов для интерфейса MaterialInfoService
 */
@Service
public class MaterialInfoImpl implements MaterialInfoService {
    private final MaterialInfoRepository materialInfoRepository;

    public MaterialInfoImpl(MaterialInfoRepository materialInfoRepository) {
        this.materialInfoRepository = materialInfoRepository;
    }

    @Override
    public List<MaterialInfoEntity> getAll() {
        return materialInfoRepository.getAll();
    }

    @Override
    public List<MaterialInfoEntity> getCheckArticle(String article) {
        return materialInfoRepository.getCheckArticle(article);
    }

    @Override
    public void saveAll(MaterialInfoEntity materialInfoEntity) {
        if (materialInfoEntity != null)
            materialInfoRepository.saveAndFlush(materialInfoEntity);
        else
            Notification.show("Нет данных для сохранения!", 5000, Notification.Position.MIDDLE);
    }

    @Override
    public void updateValue(MaterialInfoEntity materialInfoEntity) {
        if (materialInfoEntity != null)
            materialInfoRepository.updateValue(materialInfoEntity.getMaterialName(), materialInfoEntity.getArticle(), materialInfoEntity.getId());
        else
            Notification.show("Нет данных для обновления!", 5000, Notification.Position.MIDDLE);
    }

    @Override
    public void updatePrihod(MaterialInfoEntity materialInfoEntity) {
        if (materialInfoEntity != null)
            materialInfoRepository.updatePrihod(materialInfoEntity.getQuantity(), materialInfoEntity.getId(), materialInfoEntity.getBalance());
        else
            Notification.show("Нет данных для обновления!", 5000, Notification.Position.MIDDLE);
    }

    @Override
    public List<MaterialInfoEntity> getAllByStorage(Long idStorage) {
        return materialInfoRepository.getAllByStorage(idStorage);
    }

    @Override
    public List<MaterialInfoEntity> getAllByStorageCell(Long idStorage, Long idCell) {
        return materialInfoRepository.getAllByStorageCell(idStorage, idCell);
    }

    @Override
    public List<MaterialInfoEntity> getAllByArticle(String article, Long idStore) {
        return materialInfoRepository.getAllByArticle(article, idStore);
    }

    @Override
    public List<MaterialInfoEntity> getAllByMaterialName(String materialName, Long idStore) {
        return materialInfoRepository.getAllByMaterialName(materialName, idStore);
    }

    @Override
    public List<MaterialInfoEntity> getCheckID(Long id) {
        return materialInfoRepository.getCheckID(id);
    }

    @Override
    public void updateMaterialInfoStorageCell(MaterialInfoEntity materialInfoEntity) {
        if (materialInfoEntity != null)
            materialInfoRepository.updateMaterialInfoStorageCell(materialInfoEntity.getFlagMove(), materialInfoEntity.getIdStorage(),
                    materialInfoEntity.getIdCell(), materialInfoEntity.getBalance(), materialInfoEntity.getExpense(),
                    materialInfoEntity.getId());
        else
            Notification.show("Нет данных для обновления!", 5000, Notification.Position.MIDDLE);
    }

    @Override
    public void updateWriteOff(Long writeOff, Long id) {
        materialInfoRepository.updateWriteOff(writeOff, id);
    }
}
