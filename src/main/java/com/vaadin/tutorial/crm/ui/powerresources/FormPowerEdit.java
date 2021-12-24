package com.vaadin.tutorial.crm.ui.powerresources;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;
import com.vaadin.tutorial.crm.entity.powerresources.PowerResources;
import com.vaadin.tutorial.crm.service.powerresources.PowerResourcesService;

/**
 * Класс форма реализующий редактирование показаний
 */
public class FormPowerEdit extends FormLayout {
    HorizontalLayout hButton = new HorizontalLayout();
    NumberField value = new NumberField("Показания:");
    TextField dateCreate = new TextField("Дата снятия:");
    TextField timeCreate = new TextField("Время снятия:");
    Button save = new Button("Сохранить");
    Button cancel = new Button("Закрыть");
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
        value.setRequiredIndicatorVisible(true);

        hButton.add(save, cancel);

        setResponsiveSteps(new FormLayout.ResponsiveStep("50px", 3));
        add(value, dateCreate, timeCreate, save, cancel);

        save.addClickListener(e -> {
            if (!value.isEmpty())
                fireEvent(new FormPowerEdit.ContactFormEvent.EditEvent(this, value.getValue()));
            else {
                Notification.show("Поле показаний не заполнено!", 3000, Notification.Position.MIDDLE);
                return;
            }
        });
        cancel.addClickListener(e -> {
            fireEvent(new FormPowerEdit.ContactFormEvent.CloseEvent(this));
        });
    }
    public void setPowerRes(PowerResources powerResources) {
        this.powerResources = powerResources;
        if (powerResources != null) {
            value.setValue(powerResources.getValue());
            dateCreate.setValue(String.valueOf(powerResources.getDateCreate()));
            timeCreate.setValue(String.valueOf(powerResources.getTimeCreate()));
            powerValueID = powerResources.getId();
        }
    }

    public static abstract class ContactFormEvent extends ComponentEvent<FormPowerEdit> {
        private static Double value;
        protected ContactFormEvent(FormPowerEdit source, Double value) {
            super(source, false);
            this.value = value;
        }

        public static Double getValue() {
            return value;
        }

        public static class EditEvent extends FormPowerEdit.ContactFormEvent {
            EditEvent(FormPowerEdit source, Double value) {
                super(source, value);
            }
        }

        public static class CloseEvent extends FormPowerEdit.ContactFormEvent {
            CloseEvent(FormPowerEdit source) {
                super(source, 0d);
            }
        }

    }
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
