package com.vaadin.tutorial.crm.ui.storage;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.UI;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.entity.storage.LocationEntity;
import com.vaadin.tutorial.crm.entity.storage.StorageEntity;
import com.vaadin.tutorial.crm.security.SecurityUtils;
import com.vaadin.tutorial.crm.service.storage.LocationService;
import com.vaadin.tutorial.crm.service.storage.StorageService;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;
import com.vaadin.tutorial.crm.ui.layout.StorageLayout;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Класс диалог реализующий добавление склада
 */
@Route(value = "storageadd", layout = StorageLayout.class)
@PageTitle("Добавить склад | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class CreateStorageDialog extends Dialog {
    VerticalLayout vMain = new VerticalLayout();
    HorizontalLayout hMain = new HorizontalLayout();
    HorizontalLayout hMain1 = new HorizontalLayout();
    ComboBox<LocationEntity> location = new ComboBox<>("Выберите локацию:");
    TextField storageName = new TextField("Введите имя склада:");
    TextField storageShortName = new TextField("Введите аббревиатуру склада:");
    Button save = new Button("Сохранить");
    Button cancel = new Button("Отмена");
    long locationID = 0;

    @Autowired
    public CreateStorageDialog(StorageService storageService, LocationService locationService) {
        this.open();
        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);
        setDraggable(true);

        storageName.setEnabled(false);
        storageName.setWidth("255px");
        storageName.setRequired(true);
        storageShortName.setEnabled(false);
        storageShortName.setWidth("255px");
        storageShortName.setRequired(true);

        location.setRequired(true);
        location.setWidth("255px");
        location.setItems(locationService.getAll());
        ItemLabelGenerator<LocationEntity> itemLabelGenerator = locationEntity -> locationEntity.getLocationName() + " - " + locationEntity.getLocationDescription();
        location.setItemLabelGenerator(itemLabelGenerator);
        location.addValueChangeListener(e -> {
           if (e.getValue() != null) {
               storageName.setEnabled(true);
               storageShortName.setEnabled(true);
               locationID = e.getValue().getId();
           }
        });

        Icon icon1 = new Icon(VaadinIcon.DISC);
        save.setIcon(icon1);
        save.getStyle().set("background-color", "#d3b342");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Icon icon2 = new Icon(VaadinIcon.CLOSE);
        cancel.setIcon(icon2);
        cancel.getStyle().set("background-color", "#d3b342");
        cancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        hMain1.add(storageName, storageShortName);
        hMain.add(save, cancel);
        vMain.add(new AnyComponent().labelTitle("Добавить склад"), location, hMain1, hMain);
        vMain.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        add(vMain);

        cancel.addClickListener(e -> {
            UI.getCurrent().navigate(StorageSearch.class);
            close();
        });
        save.addClickListener(e -> {
            if (!location.isEmpty() && !storageName.isEmpty() && !storageShortName.isEmpty()) {
                if (storageService.getCheckStorage(storageName.getValue()).size() == 0) {
                    if (AnyComponent.checkEscSymbol(storageName)) {
                        StorageEntity storageEntity = new StorageEntity();
                        storageEntity.setStorageName(storageName.getValue());
                        storageEntity.setDateCreate(new Date());
                        storageEntity.setIdUser(SecurityUtils.getAuthentication().getDetails().getId());
                        storageEntity.setDelete(0L);
                        storageEntity.setIdLocation(locationID);
                        storageEntity.setShortName(storageShortName.getValue());
                        try {
                            storageService.saveAll(storageEntity);
                            UI.getCurrent().navigate(StorageSearch.class);
                            close();
                        }
                        catch (Exception ex) {
                            Notification.show("Не могу сохранить запись!" + ex.getMessage(), 5000, Notification.Position.MIDDLE);
                            return;
                        }
                    }
                    else {
                        Notification.show("Имя склада не может начинаться со спецсимволов!", 5000, Notification.Position.MIDDLE);
                        return;
                    }
                }
                else {
                    Notification.show("Внимание! Склад с таким названием уже существует!", 5000, Notification.Position.MIDDLE);
                    return;
                }
            }
            else {
                Notification.show("Внимание! Не все поля заполнены!", 5000, Notification.Position.MIDDLE);
                return;
            }
        });
    }
}