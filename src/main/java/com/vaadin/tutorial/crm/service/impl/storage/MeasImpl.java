package com.vaadin.tutorial.crm.service.impl.storage;

import com.vaadin.tutorial.crm.entity.storage.MeasEntity;
import com.vaadin.tutorial.crm.repository.storage.MeasRepository;
import com.vaadin.tutorial.crm.service.storage.MeasService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс содержащий реализацию методов интерфейса MeasService
 */
@Service
public class MeasImpl implements MeasService {
    private final MeasRepository measRepository;

    public MeasImpl(MeasRepository measRepository) {
        this.measRepository = measRepository;
    }

    @Override
    public List<MeasEntity> getAll() {
        return measRepository.getAll();
    }
}
