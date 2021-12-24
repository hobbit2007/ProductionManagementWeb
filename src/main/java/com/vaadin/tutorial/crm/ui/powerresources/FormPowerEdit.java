package com.vaadin.tutorial.crm.ui.powerresources;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.tutorial.crm.entity.powerresources.PowerResources;
import com.vaadin.tutorial.crm.service.powerresources.PowerResourcesService;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс форма реализующий редактирование показаний
 */
public class FormPowerEdit extends FormLayout {
    HorizontalLayout hButton = new HorizontalLayout();
    TextField value = new TextField("Показания:");
    TextField dateCreate = new TextField("Дата снятия:");
    TextField timeCreate = new TextField("Время снятия:");
    Button save = new Button("Сохранить");
    Button cancel = new Button("Отменить");
    PowerResources powerResources = new PowerResources();
    long powerValueID;
    private final PowerResourcesService powerResourcesService;

    public FormPowerEdit(PowerResourcesService powerResourcesService) {
        this.powerResourcesService = powerResourcesService;

        addClassName("contact-form");

        save.getStyle().set("background-color", "#d3b342");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.getStyle().set("background-color", "#d3b342");
        cancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        dateCreate.setReadOnly(true);
        timeCreate.setReadOnly(true);

        hButton.add(save, cancel);

        setResponsiveSteps(new FormLayout.ResponsiveStep("50px", 3));
        add(value, dateCreate, timeCreate, save, cancel);
    }
    public void setPowerRes(PowerResources powerResources) {
        this.powerResources = powerResources;
        if (powerResources != null) {
            value.setValue(String.valueOf(powerResources.getValue()));
            dateCreate.setValue(String.valueOf(powerResources.getDateCreate()));
            timeCreate.setValue(String.valueOf(powerResources.getTimeCreate()));
            powerValueID = powerResources.getId();
        }
    }
}
