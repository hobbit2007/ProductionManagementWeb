package com.vaadin.tutorial.crm.entity.powerresources;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.time.OffsetTime;

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
    private long idPowerResource;
    private double value;
    private Long idUser;
    private Date dateCreate;
    private Long delete;
    private OffsetTime timeCreate;
    private double valueDaily;
    private double valueWeekly;
    private double totalValueWeekly;

    @Transient
    String description;

    @ManyToOne
    @JoinColumn(name = "idPowerResource", referencedColumnName = "id", insertable = false, updatable = false)
    private PowerResourceDict powerResourceDict;

    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne
    @JoinColumn(name = "idPowerResource", referencedColumnName = "id", insertable = false, updatable = false)
    private PowerResources parent;

    public PowerResources() {}

    public PowerResources(String description, PowerResources parent) {
        this.description = description;
        this.parent = parent;
    }

    public PowerResources(double value, Date dateCreate, OffsetTime timeCreate,
                          long id, PowerResources parent) {
        this.value = value;
        this.dateCreate = dateCreate;
        this.timeCreate = timeCreate;
        this.id = id;
        this.parent = parent;
    }
}