package com.vaadin.tutorial.crm.service.powerresources;

import com.vaadin.tutorial.crm.entity.powerresources.PowerResources;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

/**
 * Класс интерфейс содержащий методы для работы с таблицей power_resources
 */
@Service
public interface PowerResourcesService {
    List<PowerResources> getAll();

    void saveAll(PowerResources powerResources);

    List<PowerResources> getAllByResourceId(Long resourceID);

    List<PowerResources> getResourceBySearch(Date dateBegin, Date dateEnd);

    void updateValue(PowerResources powerResources);

    void updateValueDaily(Long id, double value);

    void updateValueWeekly(Long id, double value);

    void updateTotalValueWeekly(Long id, double value);

    List<PowerResources> getAllByResourceWashing();

    //выбирает список показаний за указанные даты по указанному id ресурса(для сортировки отчета по дате)
    List<PowerResources> getResourceBySort(Date dateBegin, Date dateEnd, Long id);

    //проверка наличия показаний в БД на выбранную дату и ресурс
    List<PowerResources> getCheckDate(Date dateValue, Long resourceID);
}
