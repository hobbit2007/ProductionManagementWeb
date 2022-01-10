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
 * Класс модель описывающий поля таблицы storage_coming
 */
@Entity(name = "storage_coming")
@Getter
@Setter
public class StorageComingEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long idMaterial;
    private double qtyCome;
    private long idMeas;
    private long idUser;
    private Date dateCreate;
    private long delete;
    private double qtyOldCome;
}
