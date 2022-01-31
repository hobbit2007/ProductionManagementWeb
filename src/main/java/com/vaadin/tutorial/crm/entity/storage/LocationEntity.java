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
 * Класс модель содержащий описание полей таблицы store_location
 */
@Entity(name = "store_location")
@Getter
@Setter
public class LocationEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String locationName;
    private String locationDescription;
    private long idUser;
    private Date dateCreate;
    private long delete;
}
