package com.vaadin.tutorial.crm.ui.storage;

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
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.entity.storage.*;
import com.vaadin.tutorial.crm.security.SecurityUtils;
import com.vaadin.tutorial.crm.service.storage.*;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;
import com.vaadin.tutorial.crm.ui.layout.StorageLayout;

import java.util.Date;

/**
 * Класс реализующий добавление нового объекта хранения
 */
@Route(value = "materialadd", layout = StorageLayout.class)
@PageTitle("Добавить объект хранения | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class CreateMaterialDialog extends Dialog {
    VerticalLayout vMain = new VerticalLayout();
    HorizontalLayout hMain1 = new HorizontalLayout();
    HorizontalLayout hMain2 = new HorizontalLayout();
    HorizontalLayout hMain3 = new HorizontalLayout();
    HorizontalLayout hMain4 = new HorizontalLayout();
    HorizontalLayout hButton = new HorizontalLayout();
    ComboBox<LocationEntity> location = new ComboBox<>("Выберите локацию:");
    ComboBox<StorageEntity> storage = new ComboBox<>("Выберите склад:");
    ComboBox<CellEntity> cell = new ComboBox<>("Выберите ячейку:");
    TextField material = new TextField("Название объекта хранения:");
    TextField article = new TextField("Артикул:");
    NumberField qty = new NumberField("Количество:");
    ComboBox<MeasEntity> meas = new ComboBox<>("Ед. измерения:");
    NumberField costPrice = new NumberField("Цена, себестоимость:");
    NumberField marketPrice = new NumberField("Цена, продажа:");
    Button save = new Button("Сохранить");
    Button cancel = new Button("Отмена");
    long storageID = 0;
    long cellID = 0;
    long measID = 0;
    long locationID = 0;
    private final StorageService storageService;
    private final CellService cellService;
    private final MaterialInfoService materialInfoService;
    private final MeasService measService;
    private final LocationService locationService;

    public CreateMaterialDialog(StorageService storageService, CellService cellService, MaterialInfoService materialInfoService, MeasService measService,
                                LocationService locationService) {
        this.storageService = storageService;
        this.cellService = cellService;
        this.materialInfoService = materialInfoService;
        this.measService = measService;
        this.locationService = locationService;

        this.open();

        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        setDraggable(true);

        storage.setEnabled(false);
        cell.setEnabled(false);
        location.setRequired(true);
        location.setItems(locationService.getAll());
        location.setItemLabelGenerator(LocationEntity::getLocationName);
        location.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                storage.setEnabled(true);
                locationID = e.getValue().getId();
                storage.setRequired(true);
                storage.setItems(storageService.getStorageByLocationID(locationID));
                storage.setItemLabelGenerator(StorageEntity::getStorageName);
            }
            else
                storage.setEnabled(false);
        });
        cell.setRequired(true);

        material.setRequired(true);
        material.setWidth("235px");
        article.setRequired(true);
        article.setWidth("150px");
        qty.setRequiredIndicatorVisible(true);
        qty.setValue(1d);

        meas.setRequired(true);
        meas.setItems(measService.getAll());
        meas.setItemLabelGenerator(MeasEntity::getMeasName);

        costPrice.setRequiredIndicatorVisible(true);
        costPrice.setWidth("170px");
        costPrice.setValue(0.00);
        marketPrice.setRequiredIndicatorVisible(true);
        marketPrice.setValue(0.00);

        Icon icon1 = new Icon(VaadinIcon.DISC);
        save.setIcon(icon1);
        save.getStyle().set("background-color", "#d3b342");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Icon icon2 = new Icon(VaadinIcon.CLOSE);
        cancel.setIcon(icon2);
        cancel.getStyle().set("background-color", "#d3b342");
        cancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        storage.addValueChangeListener(e -> {
           if (e.getValue() != null) {
               storageID = e.getValue().getId();
               cell.setEnabled(true);
               cell.setItems(cellService.getAll(storageID));
               cell.setItemLabelGenerator(CellEntity::getCellName);
           }
        });
        cell.addValueChangeListener(e -> {
           if (e.getValue() != null)
               cellID = e.getValue().getId();
        });
        meas.addValueChangeListener(e -> {
           if (e.getValue() != null)
               measID = e.getValue().getId();
        });

        hMain1.add(location, storage, cell);
        hMain1.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);

        hMain2.add(material, article);
        hMain2.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);

        hMain3.add(qty, meas);
        hMain3.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);

        hMain4.add(costPrice, marketPrice);
        hMain4.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);

        hButton.add(save, cancel);
        hButton.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        vMain.add(new AnyComponent().labelTitle("Добавить объект хранения"), hMain1, hMain2, hMain3, hMain4, hButton);
        vMain.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        add(vMain);

        cancel.addClickListener(e -> {
            UI.getCurrent().navigate(StorageSearch.class);
            close();
        });
        save.addClickListener(e -> {
            MaterialInfoEntity materialInfoEntity = new MaterialInfoEntity();
            if (!storage.isEmpty() && !cell.isEmpty() && (!material.isEmpty() || material.getValue().length() != 0) && !qty.isEmpty() &&
            !meas.isEmpty() && !costPrice.isEmpty() && !marketPrice.isEmpty()) {
                if (materialInfoService.getCheckArticle(article.getValue()).size() == 0) {
                    if (AnyComponent.checkEscSymbol(material)) {
                        materialInfoEntity.setIdStorage(storageID);
                        materialInfoEntity.setIdCell(cellID);
                        materialInfoEntity.setMaterialName(material.getValue());
                        materialInfoEntity.setArticle(article.getValue());
                        materialInfoEntity.setQuantity(qty.getValue());
                        materialInfoEntity.setIdMeas(measID);
                        materialInfoEntity.setExpense(0d);
                        materialInfoEntity.setBalance(qty.getValue());
                        materialInfoEntity.setDateCreate(new Date());
                        materialInfoEntity.setIdUser(SecurityUtils.getAuthentication().getDetails().getId());
                        materialInfoEntity.setWriteoff(0L);
                        materialInfoEntity.setCostPrice(costPrice.getValue());
                        materialInfoEntity.setMarketPrice(marketPrice.getValue());
                        materialInfoEntity.setDiffPrice(marketPrice.getValue() - costPrice.getValue());
                        materialInfoEntity.setFlagMove(0L);
                        materialInfoEntity.setDelete(0L);
                        materialInfoEntity.setIdLocation(locationID);

                        try {
                            materialInfoService.saveAll(materialInfoEntity);
                            UI.getCurrent().navigate(StorageSearch.class);
                            close();
                        }
                        catch (Exception ex) {
                            Notification.show("Не могу сохранить запись!" + ex.getMessage(), 5000, Notification.Position.MIDDLE);
                            return;
                        }
                    }
                    else {
                        Notification.show("Объект хранения не может начинаться со спецсимволов!", 5000, Notification.Position.MIDDLE);
                        return;
                    }
                }
                else {
                    Notification.show("Внимание! Объект хранения с таким артикулом уже существует!", 5000, Notification.Position.MIDDLE);
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
