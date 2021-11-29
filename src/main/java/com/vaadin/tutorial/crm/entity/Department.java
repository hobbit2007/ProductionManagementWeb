package com.vaadin.tutorial.crm.entity;

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

/**
 * Класс модель описывающий таблицу department
 */
@Entity(name = "department")
@Getter
@Setter
public class Department implements Externalizable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String departmentName;

    private Long idShop;

    private Long delete;


    @Override
    public void writeExternal(ObjectOutput objectOutput) throws IOException {

    }

    @Override
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {

    }
}
