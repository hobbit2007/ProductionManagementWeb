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
}
