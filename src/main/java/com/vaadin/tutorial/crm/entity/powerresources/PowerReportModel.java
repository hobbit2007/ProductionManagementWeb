package com.vaadin.tutorial.crm.entity.powerresources;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

/**
 * Класс модель для передачи параметров в класс отчета
 * id - id ресурса
 * msg - название отчета
 */
@Service
@Getter
@Setter
public class PowerReportModel {
    private long id;
    private String msg;
}
