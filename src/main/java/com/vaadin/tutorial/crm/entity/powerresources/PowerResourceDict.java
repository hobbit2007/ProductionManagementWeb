package com.vaadin.tutorial.crm.entity.powerresources;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Date;

/**
 * Класс модель описывающий таблицу power_resource_dict
 */
@Entity(name = "power_resource_dict")
@Getter
@Setter
public class PowerResourceDict implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String resourceName;
    private Long idUser;
    private Date dateCreate;
    private Long delete;
    private Long category;
}