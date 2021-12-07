package com.vaadin.tutorial.crm.service.impl.plccontrollersimpl;

import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcControllers;
import com.vaadin.tutorial.crm.repository.plccontrollersrepository.PlcControllersRepository;
import com.vaadin.tutorial.crm.service.plccontrollersservice.PlcControllersService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс реализующий методы для работы с таблицей plccontrollers
 */
@Service
public class PlcControllersImpl implements PlcControllersService {
    private PlcControllersRepository plcControllersRepository;

    public PlcControllersImpl(PlcControllersRepository plcControllersRepository) {
        this.plcControllersRepository = plcControllersRepository;
    }

    @Override
    public List<PlcControllers> getAll() {
        return plcControllersRepository.getAll();
    }

    @Override
    public List<PlcControllers> getAllByID(long id) {
        return plcControllersRepository.getAllByID(id);
    }
}
