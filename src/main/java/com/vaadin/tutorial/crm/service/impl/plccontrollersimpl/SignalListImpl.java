package com.vaadin.tutorial.crm.service.impl.plccontrollersimpl;

import com.vaadin.tutorial.crm.entity.plccontrollersentity.SignalList;
import com.vaadin.tutorial.crm.repository.plccontrollersrepository.SignalListRepository;
import com.vaadin.tutorial.crm.service.plccontrollersservice.SignalListService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс реализующий методы интерфейса SignalListService
 */
@Service
public class SignalListImpl implements SignalListService {
    private SignalListRepository signalListRepository;
    public SignalListImpl(SignalListRepository signalListRepository) {
        this.signalListRepository = signalListRepository;
    }

    @Override
    public List<SignalList> findSignalList(Long controllerId) {
        return signalListRepository.findSignalList(controllerId);
    }
}
