package com.vaadin.tutorial.crm.entity.plccontrollersentity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;

/**
 * Класс модель описывающий таблицу signallist
 */
@Entity(name = "signallist")
@Getter
@Setter
public class SignalList implements Externalizable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String signalName;
    private String signalDescription;
    private Long delete;
    private int dbValue;
    private int position;
    private int offset;
    private Long idUserCreate;
    private Date dateCreate;
    private Long idController;

    @Override
    public void writeExternal(ObjectOutput objectOutput) throws IOException {

    }

    @Override
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {

    }
}
