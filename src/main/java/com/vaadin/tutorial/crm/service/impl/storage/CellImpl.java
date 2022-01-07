package com.vaadin.tutorial.crm.service.impl.storage;

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
    public List<CellEntity> getAll() {
        return cellRepository.getAll();
    }
}
