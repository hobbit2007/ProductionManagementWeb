package com.vaadin.tutorial.crm.service.impl.plccontrollersimpl;

import com.vaadin.tutorial.crm.entity.plccontrollersentity.SignalGroup;
import com.vaadin.tutorial.crm.repository.plccontrollersrepository.SignalGroupRepository;
import com.vaadin.tutorial.crm.service.plccontrollersservice.SignalGroupsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс содержащий реализацию методов интерфейса SignalGroupsService
 */
@Service
public class SignalGroupsImpl implements SignalGroupsService {
    private SignalGroupRepository signalGroupRepository;
    @Autowired
    public SignalGroupsImpl(SignalGroupRepository signalGroupRepository) {
        this.signalGroupRepository = signalGroupRepository;
    }
    @Override
    public List<SignalGroup> getAll() {
        return signalGroupRepository.getAll();
    }
}
