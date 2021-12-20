package com.vaadin.tutorial.crm.entity.powerresources;

import com.vaadin.tutorial.crm.entity.powerresources.PowerResourceDict;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

/**
 * Класс модель описывающий таблицу power_resources
 */
@Entity(name = "power_resources")
@Getter
@Setter
public class PowerResources implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Long idPowerResource;
    private double value;
    private Long idUser;
    private Date dateCreate;
    private Long delete;

    @ManyToOne
    @JoinColumn(name = "idPowerResource", referencedColumnName = "id", insertable = false, updatable = false)
    private PowerResourceDict powerResourceDict;
}
