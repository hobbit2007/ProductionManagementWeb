package com.vaadin.tutorial.crm.entity.plccontrollersentity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Класс модель описывающий таблицу plccontrollervaluedifusion, которая содержит значения переменных ПЛК диффузия
 */
@Entity(name = "plccontrollervaluedifusion")
@Getter
@Setter
public class PlcDiffusionValue implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Long idSignal;
    private Float value;
    private Long idOrderNum;
    private Date dateCreate;
    private Long alarm;
    private Long delete;

    @ManyToOne
    @JoinColumn(name = "idSignal", referencedColumnName = "id", insertable = false, updatable = false)
    private SignalList info;
}
