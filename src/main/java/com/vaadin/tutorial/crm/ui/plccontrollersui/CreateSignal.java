package com.vaadin.tutorial.crm.ui.plccontrollersui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcControllers;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.SignalGroup;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.SignalList;
import com.vaadin.tutorial.crm.security.SecurityUtils;
import com.vaadin.tutorial.crm.service.plccontrollersservice.PlcControllersService;
import com.vaadin.tutorial.crm.service.plccontrollersservice.SignalGroupsService;
import com.vaadin.tutorial.crm.service.plccontrollersservice.SignalListService;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;
import com.vaadin.tutorial.crm.ui.layout.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Класс позволяющий добавить новый сигнал в таблицу signallist
 */
@Route(value = "createsignal", layout = MainLayout.class)
@PageTitle("Добавление нового сигнала | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class CreateSignal extends Dialog {
    VerticalLayout vMain = new VerticalLayout();
    HorizontalLayout hGroup1 = new HorizontalLayout();
    HorizontalLayout hGroup2 = new HorizontalLayout();
    HorizontalLayout hButton = new HorizontalLayout();
    ComboBox<PlcControllers> controller = new ComboBox<>("Выберите контроллер:");
    ComboBox<SignalGroup> signalGroup = new ComboBox<>("Выберите группу:");
    TextField signalName = new TextField("Введите имя переменной:");
    TextArea signalDescription = new TextArea("Описание переменной:");
    NumberField dbNumber = new NumberField("Введите номер базы:");
    NumberField positionNumber = new NumberField("Введите позицию переменной:");
    NumberField offsetNumber = new NumberField("Длина типа данных переменной");
    Button save = new Button("Сохранить");
    Button cancel = new Button("Отмена");
    long controllerId = 0;
    long signalGroupID = 0;
    PlcControllersService plcControllersService;
    SignalGroupsService signalGroupsService;
    SignalListService signalListService;

    @Autowired
    public CreateSignal(PlcControllersService plcControllersService, SignalGroupsService signalGroupsService,
                        SignalListService signalListService) {
        this.plcControllersService = plcControllersService;
        this.signalGroupsService = signalGroupsService;
        this.signalListService = signalListService;
        setDraggable(true);
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        controller.setRequired(true);
        controller.setItems(plcControllersService.getAll());
        controller.setItemLabelGenerator(PlcControllers::getControllerName);

        signalGroup.setRequired(true);
        signalGroup.setItems(signalGroupsService.getAll());
        signalGroup.setItemLabelGenerator(SignalGroup::getShortSignalDescription);

        hGroup1.add(controller, signalGroup);
        hGroup2.add(signalName, signalDescription);
        signalName.setRequired(true);
        signalName.setPlaceholder("Используйте нижнее подчеркивание!");
        signalName.setTitle("Используйте нижнее подчеркивание!");
        signalDescription.setRequired(true);

        dbNumber.setWidth("401px");
        dbNumber.setRequiredIndicatorVisible(true);
        dbNumber.setValue((double) 1000L);

        positionNumber.setWidth("401px");
        positionNumber.setRequiredIndicatorVisible(true);
        offsetNumber.setWidth("401px");
        offsetNumber.setRequiredIndicatorVisible(true);

        Icon iconSaveButton = new Icon(VaadinIcon.DISC);
        save.setIcon(iconSaveButton);
        save.getStyle().set("background-color", "#d3b342");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Icon iconCancelButton = new Icon(VaadinIcon.CLOSE);
        cancel.setIcon(iconCancelButton);
        cancel.getStyle().set("background-color", "#d3b342");
        cancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        hButton.add(save, cancel);

        vMain.add(new AnyComponent().labelTitle("Новая переменная"), hGroup1, hGroup2, dbNumber, positionNumber, offsetNumber, hButton);
        vMain.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        add(vMain);

        controller.addValueChangeListener(e -> {
           if (e.getValue() != null)
               controllerId = e.getValue().getId();
        });
        signalGroup.addValueChangeListener(e -> {
           if (e.getValue() != null)
               signalGroupID = e.getValue().getId();
        });
        save.addClickListener(e -> {
            SignalList signalList = new SignalList();
            if (controller.getValue() != null && signalGroup.getValue() != null && !signalName.getValue().isEmpty() &&
                    !signalDescription.getValue().isEmpty() && !dbNumber.isEmpty() && !positionNumber.isEmpty() && !offsetNumber.isEmpty()) {
                if (signalName.getValue().charAt(0) != ' ' && signalName.getValue().charAt(0) != '~' &&
                        signalName.getValue().charAt(0) != '`' && signalName.getValue().charAt(0) != '"' &&
                        signalName.getValue().charAt(0) != ':' && signalName.getValue().charAt(0) != ';' &&
                        signalName.getValue().charAt(0) != ',' && signalName.getValue().charAt(0) != '.' &&
                        signalName.getValue().charAt(0) != '!' && signalName.getValue().charAt(0) != '@' &&
                        signalName.getValue().charAt(0) != '#' && signalName.getValue().charAt(0) != '$' &&
                        signalName.getValue().charAt(0) != '%' && signalName.getValue().charAt(0) != '^' &&
                        signalName.getValue().charAt(0) != '&' && signalName.getValue().charAt(0) != '*' &&
                        signalName.getValue().charAt(0) != '(' && signalName.getValue().charAt(0) != ')' &&
                        signalName.getValue().charAt(0) != '-' && signalName.getValue().charAt(0) != '_' &&
                        signalName.getValue().charAt(0) != '+' && signalName.getValue().charAt(0) != '+' &&
                        signalName.getValue().charAt(0) != '\'') {
                    try {
                        signalList.setSignalName(signalName.getValue().trim());
                        signalList.setSignalDescription(signalDescription.getValue());
                        signalList.setDelete(0L);
                        signalList.setDbValue(dbNumber.getValue().intValue());
                        signalList.setPosition(positionNumber.getValue().intValue());
                        signalList.setFOffset(offsetNumber.getValue().intValue());
                        signalList.setIdUserCreate(SecurityUtils.getAuthentication().getDetails().getId());
                        signalList.setDateCreate(new Date());
                        signalList.setIdController(controllerId);
                        signalList.setIdGroup(signalGroupID);

                        signalListService.saveAll(signalList);
                        Notification.show("Переменная успешно создана!", 5000, Notification.Position.MIDDLE);
                        close();
                    }
                    catch (Exception ex) {
                        Notification.show("Не могу сделать запись в БД! " + ex.getMessage());
                        return;
                    }
                }
                else {
                    Notification.show("Имя переменной не может начинаться со спецсимволов!", 5000, Notification.Position.MIDDLE);
                    return;
                }
            }
            else {
                Notification.show("Не все поля заполнены!", 5000, Notification.Position.MIDDLE);
                return;
            }
        });
        cancel.addClickListener(e -> {
           close();
        });
    }
}
