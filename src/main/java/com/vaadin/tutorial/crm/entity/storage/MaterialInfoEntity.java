package com.vaadin.tutorial.crm.entity.storage;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Класс модель описывающий поля таблицы materialinfo
 */
@Entity(name = "materialinfo")
@Getter
@Setter
public class MaterialInfoEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long idStorage;
    private long idCell;
    private String materialName;
    private String article;
    private long quantity;
    private double weight;
    private long idMeas;
    private long expense;
    private double expenseWeight;
    private long balance;
    private double balanceWeight;
    private Date dateCreate;
    private long idUser;
    private long writeOff;
    private double costPrice;
    private double marketPrice;
    private double diffPrice;
    private long flagMove;
    private long delete;
}
