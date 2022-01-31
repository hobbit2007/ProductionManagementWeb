package com.vaadin.tutorial.crm.ui.storage;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.entity.storage.LocationEntity;
import com.vaadin.tutorial.crm.security.SecurityUtils;
import com.vaadin.tutorial.crm.service.storage.LocationService;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;
import com.vaadin.tutorial.crm.ui.layout.StorageLayout;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Класс диалог позволяющий создать локацию склада
 */
@Route(value = "locationadd", layout = StorageLayout.class)
@PageTitle("Добавить локацию | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class CreateLocationDialog extends Dialog {
    VerticalLayout vMain = new VerticalLayout();
    HorizontalLayout hMain = new HorizontalLayout();
    TextField locationName = new TextField("Введите имя локации:");
    TextArea locationDescription = new TextArea("Введите описание:");
    Button save = new Button("Сохранить");
    Button cancel = new Button("Отмена");
    @Autowired
    public CreateLocationDialog(LocationService locationService) {
        this.open();
        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);
        setDraggable(true);

        locationName.setWidth("255px");
        locationName.setRequired(true);
        locationName.setTitle("Зона помещения:№ стеллажа");
        locationName.setPlaceholder("Зона помещения:№ стеллажа");
        locationDescription.setWidth("255px");
        locationDescription.setRequired(true);

        Icon icon1 = new Icon(VaadinIcon.DISC);
        save.setIcon(icon1);
        save.getStyle().set("background-color", "#d3b342");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Icon icon2 = new Icon(VaadinIcon.CLOSE);
        cancel.setIcon(icon2);
        cancel.getStyle().set("background-color", "#d3b342");
        cancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        hMain.add(save, cancel);
        vMain.add(new AnyComponent().labelTitle("Добавить локацию"), locationName, locationDescription, hMain);
        vMain.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        add(vMain);

        cancel.addClickListener(e -> close());
        save.addClickListener(e -> {
            if (!locationName.isEmpty() && !locationDescription.isEmpty()) {
                LocationEntity locationEntity = new LocationEntity();
                locationEntity.setLocationName(locationName.getValue());
                locationEntity.setLocationDescription(locationDescription.getValue());
                locationEntity.setIdUser(SecurityUtils.getAuthentication().getDetails().getId());
                locationEntity.setDateCreate(new Date());
                locationEntity.setDelete(0);

                try{
                    locationService.saveAll(locationEntity);
                }
                catch (Exception ex) {
                    Notification.show("Не могу создать локацию! " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
                }
            }
        });
    }
}
