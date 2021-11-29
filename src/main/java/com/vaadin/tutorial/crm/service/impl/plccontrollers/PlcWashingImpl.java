package com.vaadin.tutorial.crm.service.impl.plccontrollers;

import com.vaadin.tutorial.crm.entity.PlcControllers.PlcWashing;
import com.vaadin.tutorial.crm.repository.plccontrollers.PlcWashingRepository;
import com.vaadin.tutorial.crm.service.plccontrollers.PlcWashingService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс содержащий реализацию методов интерфейса PlcWashingService
 */
@Service
public class PlcWashingImpl implements PlcWashingService {
    private PlcWashingRepository plcWashingRepository;

    public PlcWashingImpl(PlcWashingRepository plcWashingRepository) {
        this.plcWashingRepository = plcWashingRepository;
    }

    @Override
    public List<PlcWashing> getAll() {
        return plcWashingRepository.getAll();
    }
}
