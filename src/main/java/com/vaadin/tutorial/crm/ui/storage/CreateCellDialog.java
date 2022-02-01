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
import com.vaadin.tutorial.crm.entity.storage.CellEntity;
import com.vaadin.tutorial.crm.entity.storage.LocationEntity;
import com.vaadin.tutorial.crm.entity.storage.StorageEntity;
import com.vaadin.tutorial.crm.security.SecurityUtils;
import com.vaadin.tutorial.crm.service.storage.CellService;
import com.vaadin.tutorial.crm.service.storage.LocationService;
import com.vaadin.tutorial.crm.service.storage.StorageService;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;
import com.vaadin.tutorial.crm.ui.layout.StorageLayout;

import java.util.Date;

/**
 * Класс реализующий диалог создания новой ячейки и сохранение информации о ней в БД
 */
@Route(value = "celladd", layout = StorageLayout.class)
@PageTitle("Добавить ячейку | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class CreateCellDialog extends Dialog {
    private final CellService cellService;
    private final LocationService locationService;
    ComboBox<LocationEntity> location = new ComboBox<>("Выберите локацию:");
    VerticalLayout vMain = new VerticalLayout();
    HorizontalLayout hMain = new HorizontalLayout();
    HorizontalLayout hMain1 = new HorizontalLayout();
    TextField cellName = new TextField("Введите имя ячейки:");
    Button save = new Button("Сохранить");
    Button cancel = new Button("Отмена");
    long storageID = 0;
    long locationID = 0;
    public CreateCellDialog(CellService cellService, StorageService storageService, LocationService locationService) {
        this.cellService = cellService;
        this.locationService = locationService;
        this.open();
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        setDraggable(true);

        cellName.setWidth("255px");
        cellName.setEnabled(false);
        cellName.setRequired(true);
        cellName.setTitle("№ секция:ячейка:№ полка");
        cellName.setPlaceholder("№ секция:ячейка:№ полка");

        location.setWidth("255px");
        location.setRequired(true);
        location.setItems(locationService.getAll());
        ItemLabelGenerator<LocationEntity> itemLabelGenerator = locationEntity -> locationEntity.getLocationName() + " - " + locationEntity.getLocationDescription();
        location.setItemLabelGenerator(itemLabelGenerator);
        location.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                locationID = e.getValue().getId();
                storageID = e.getValue().getIdStorage();
                cellName.setEnabled(true);
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

        hMain1.add(location);
        hMain.add(save, cancel);
        vMain.add(new AnyComponent().labelTitle("Добавить ячейку"), hMain1, cellName, hMain);
        vMain.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        add(vMain);

        cancel.addClickListener(e -> {
            UI.getCurrent().navigate(StorageSearch.class);
           close();
        });
        save.addClickListener(e -> {
            CellEntity cellEntity = new CellEntity();
            if (!cellName.isEmpty() && !location.isEmpty()) {
                if (cellService.getCheckCell(cellName.getValue(), storageID).size() == 0) {
                    if (AnyComponent.checkEscSymbol(cellName)) {

                        cellEntity.setCellName(cellName.getValue());
                        cellEntity.setIdStorage(storageID);
                        cellEntity.setDateCreate(new Date());
                        cellEntity.setIdUser(SecurityUtils.getAuthentication().getDetails().getId());
                        cellEntity.setDelete(0L);
                        cellEntity.setIdLocation(locationID);
                        try {
                            cellService.saveAll(cellEntity);
                            UI.getCurrent().navigate(StorageSearch.class);
                            close();
                        }
                        catch (Exception ex) {
                            Notification.show("Не могу сохранить запись!" + ex.getMessage(), 5000, Notification.Position.MIDDLE);
                            return;
                        }
                    }
                    else {
                        Notification.show("Имя ячейки не может начинаться со спецсимволов!", 5000, Notification.Position.MIDDLE);
                        return;
                    }
                }
                else {
                    Notification.show("Внимание! Ячейка с таким названием уже существует!", 5000, Notification.Position.MIDDLE);
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
