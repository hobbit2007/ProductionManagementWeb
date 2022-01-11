package com.vaadin.tutorial.crm.service.impl.powerresources;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.tutorial.crm.entity.powerresources.PowerResources;
import com.vaadin.tutorial.crm.repository.powerresources.PowerResourcesRepository;
import com.vaadin.tutorial.crm.service.powerresources.PowerResourcesService;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

/**
 * Класс реализующий методы интерфейса PowerResourcesService
 */
@Service
public class PowerResourcesImpl implements PowerResourcesService {
    private final PowerResourcesRepository powerResourcesRepository;

    public PowerResourcesImpl(PowerResourcesRepository powerResourcesRepository) {
        this.powerResourcesRepository = powerResourcesRepository;
    }

    @Override
    public List<PowerResources> getAll() {
        return powerResourcesRepository.getAll();
    }

    @Override
    public void saveAll(PowerResources powerResources) {
        if (powerResources != null)
            powerResourcesRepository.saveAndFlush(powerResources);
        else
            Notification.show("Нет данных для записи!", 5000, Notification.Position.MIDDLE);
    }

    @Override
    public List<PowerResources> getAllByResourceId(Long resourceID) {
        return powerResourcesRepository.getAllByResourceId(resourceID);
    }

    @Override
    public List<PowerResources> getResourceBySearch(Date dateBegin, Date dateEnd) {
        return powerResourcesRepository.getResourceBySearch(dateBegin, dateEnd);
    }

    @Override
    public void updateValue(PowerResources powerResources) {
        powerResourcesRepository.updateValue(powerResources.getId(), powerResources.getValue());
    }

    @Override
    public void updateValueDaily(Long id, double value) {
        powerResourcesRepository.updateValueDaily(id, value);
    }

    @Override
    public void updateValueWeekly(Long id, double value) {
        powerResourcesRepository.updateValueWeekly(id, value);
    }

    @Override
    public void updateTotalValueWeekly(Long id, double value) {
        powerResourcesRepository.updateTotalValueWeekly(id, value);
    }

    @Override
    public List<PowerResources> getAllByResourceWashing() {
        return powerResourcesRepository.getAllByResourceWashing();
    }

    /**
     * выбирает список показаний за указанные даты по указанному id ресурса(для сортировки отчета по дате)
     * @param dateBegin - дата начала
     * @param dateEnd - дата окончания
     * @param id - id ресурса
     * @return - массив значений
     */
    @Override
    public List<PowerResources> getResourceBySort(Date dateBegin, Date dateEnd, Long id) {
        return powerResourcesRepository.getResourceBySort(dateBegin, dateEnd, id);
    }

    /**
     * Метод проверяет наличие показаний в БД на указанную дату
     * @param dateValue - дата, выбранная при вводе показаний
     * @return - возвращает список, если на выбранную дату присутствуют показания
     */
    @Override
    public List<PowerResources> getCheckDate(Date dateValue) {
        return powerResourcesRepository.getCheckDate(dateValue);
    }
}
