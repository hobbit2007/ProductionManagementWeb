package com.vaadin.tutorial.crm.service.impl.storage;

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
}
