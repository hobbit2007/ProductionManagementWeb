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
            materialInfoRepository.updateValue(materialInfoEntity.getMaterialName(), materialInfoEntity.getArticle(), materialInfoEntity.getQuantity(),
                    materialInfoEntity.getCostPrice(), materialInfoEntity.getMarketPrice(), materialInfoEntity.getId(),
                    materialInfoEntity.getDiffPrice(), materialInfoEntity.getBalance());
        else
            Notification.show("Нет данных для обновления!", 5000, Notification.Position.MIDDLE);
    }
}
