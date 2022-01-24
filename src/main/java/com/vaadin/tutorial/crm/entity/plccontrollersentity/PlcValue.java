package com.vaadin.tutorial.crm.entity.plccontrollersentity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;

/**
 * Класс модель описывающий таблицу plcwashing, которая содержит значения
 * сигналов, записанные раз в 10 минут из контроллера мойки
 */
@Entity(name = "plccontrollersvalue")
@Getter
@Setter
public class PlcValue implements Externalizable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Long idSignalName;
    private Float value;
    private Long idOrderNum;
    private Date dateCreate;
    private Long delete;
    private Long idController;
    private Long alarm;

    @ManyToOne
    @JoinColumn(name = "idSignalName", referencedColumnName = "id", insertable = false, updatable = false)
    private SignalList info;

    @Override
    public void writeExternal(ObjectOutput objectOutput) throws IOException {

    }

    @Override
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {

    }
}
