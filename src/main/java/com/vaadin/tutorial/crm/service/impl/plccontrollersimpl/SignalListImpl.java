package com.vaadin.tutorial.crm.service.impl.plccontrollersimpl;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.SignalList;
import com.vaadin.tutorial.crm.repository.plccontrollersrepository.SignalListRepository;
import com.vaadin.tutorial.crm.service.plccontrollersservice.SignalListService;
import org.aspectj.weaver.ast.Not;
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

    @Override
    public Long countGroups(Long controllerId) {
        return signalListRepository.countGroups(controllerId);
    }

    @Override
    public void saveAll(SignalList signalList) {
        if (signalList != null)
            //signalListRepository.saveAndFlush(signalList);
            signalListRepository.insertValue(signalList.getSignalName(), signalList.getSignalDescription(), signalList.getDbValue(),
                    signalList.getPosition(), signalList.getFOffset(), signalList.getIdUserCreate(), signalList.getDateCreate(),
                    signalList.getIdController(), signalList.getIdGroup());
        else
            Notification.show("Нет данных для записи!", 5000, Notification.Position.MIDDLE);
    }

    @Override
    public void updateValue(SignalList signalList) {
        signalListRepository.updateValue(signalList.getSignalName(), signalList.getSignalDescription(), signalList.getDbValue(),
                signalList.getPosition(), signalList.getFOffset(), signalList.getIdGroup(), signalList.getId());
    }
}
