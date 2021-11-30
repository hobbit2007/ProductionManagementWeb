package com.vaadin.tutorial.crm.service.impl.plccontrollersimpl;

import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcValue;
import com.vaadin.tutorial.crm.repository.plccontrollersrepository.PlcValueRepository;
import com.vaadin.tutorial.crm.service.plccontrollersservice.PlcValueService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс содержащий реализацию методов интерфейса PlcValueService
 */
@Service
public class PlcValueImpl implements PlcValueService {
    private PlcValueRepository plcWashingRepository;

    public PlcValueImpl(PlcValueRepository plcWashingRepository) {
        this.plcWashingRepository = plcWashingRepository;
    }

    @Override
    public List<PlcValue> getSignalOnController(Long controllerId) {
        return plcWashingRepository.getSignalOnController(controllerId);
    }
}
