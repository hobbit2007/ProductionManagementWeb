package com.vaadin.tutorial.crm.ui.storage;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
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
import com.vaadin.tutorial.crm.entity.storage.*;
import com.vaadin.tutorial.crm.model.DataLabel;
import com.vaadin.tutorial.crm.security.SecurityUtils;
import com.vaadin.tutorial.crm.service.storage.*;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;
import com.vaadin.tutorial.crm.ui.layout.StorageLayout;
import com.vaadin.tutorial.crm.ui.report.PowerResourceReport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    ComboBox<SupplierEntity> supplier = new ComboBox<>("Выберите поставщика:");
    TextArea description = new TextArea("Описание:");
    Button save = new Button("Сохранить");
    Button cancel = new Button("Отмена");
    Button printLabel = new Button("Печать этикетки");
    long storageID = 0;
    long cellID = 0;
    long measID = 0;
    long locationID = 0;
    long supplierID = 0;
    private final StorageService storageService;
    private final CellService cellService;
    private final MaterialInfoService materialInfoService;
    private final MeasService measService;
    private final LocationService locationService;
    private final SupplierService supplierService;
    MaterialInfoEntity materialInfoEntity = new MaterialInfoEntity();

    public CreateMaterialDialog(StorageService storageService, CellService cellService, MaterialInfoService materialInfoService, MeasService measService,
                                LocationService locationService, SupplierService supplierService) {
        this.storageService = storageService;
        this.cellService = cellService;
        this.materialInfoService = materialInfoService;
        this.measService = measService;
        this.locationService = locationService;
        this.supplierService = supplierService;
        FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("10px", 3));
        formLayout.setWidth("831px");

        this.open();

        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        setDraggable(true);
        storage.setRequired(true);
        storage.setItems(storageService.getAll());
        storage.setItemLabelGenerator(StorageEntity::getStorageName);
        cell.setEnabled(false);
        location.setRequired(true);
        location.setEnabled(false);

        location.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                locationID = e.getValue().getId();
                cell.setEnabled(true);
                cell.setItems(cellService.getAll(storageID, locationID));
                cell.setItemLabelGenerator(CellEntity::getCellName);
            }
        });
        cell.setRequired(true);

        material.setRequired(true);
        material.setWidth("235px");

        article.setReadOnly(true);
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
        marketPrice.setReadOnly(true);

        Icon icon1 = new Icon(VaadinIcon.DISC);
        save.setIcon(icon1);
        save.getStyle().set("background-color", "#d3b342");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Icon icon2 = new Icon(VaadinIcon.CLOSE);
        cancel.setIcon(icon2);
        cancel.getStyle().set("background-color", "#d3b342");
        cancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Icon icon3 = new Icon(VaadinIcon.PRINT);
        printLabel.setIcon(icon3);
        printLabel.getStyle().set("background-color", "#d3b342");
        printLabel.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        printLabel.setEnabled(false);

        description.setValue("Без описания");

        supplier.setItems(supplierService.getAll());
        supplier.setItemLabelGenerator(SupplierEntity::getSupplierName);
        supplier.setRequired(true);
        supplier.addValueChangeListener(e -> {
           if (e.getValue() != null)
               supplierID = e.getValue().getId();
        });

        storage.addValueChangeListener(e -> {
           if (e.getValue() != null) {
               storageID = e.getValue().getId();
               location.setEnabled(true);
               location.setItems(locationService.getFindLocationByStorageID(storageID));
               ItemLabelGenerator<LocationEntity> itemLabelGenerator = locationEntity -> locationEntity.getLocationName() + " - " + locationEntity.getLocationDescription();
               location.setItemLabelGenerator(itemLabelGenerator);
               //article.setValue(createArticle(materialInfoService.findByLastArticle(), storageService.getFindStorageByID(storageID).get(0).getShortName()));
           }
        });
        cell.addValueChangeListener(e -> {
           if (e.getValue() != null)
               cellID = e.getValue().getId();
        });
        meas.addValueChangeListener(e -> {
           if (e.getValue() != null)
               measID = e.getValue().getId();
               article.setValue(createArticle(materialInfoService.findByLastArticle(), storageService.getFindStorageByID(storageID).get(0).getShortName(), e.getValue().getMeasName()));
        });

        hMain1.add(storage, location, cell);
        hMain1.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);

        hMain2.add(material, article, supplier);
        hMain2.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);

        hMain3.add(description, qty, meas);
        hMain3.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);

        hMain4.add(costPrice, marketPrice);
        hMain4.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);

        hButton.add(save, cancel, printLabel);
        hButton.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        formLayout.add(storage, location, cell, material, article, supplier, description, qty, meas, costPrice, marketPrice);
        vMain.add(new AnyComponent().labelTitle("Добавить объект хранения"), formLayout, hButton);
        vMain.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        add(vMain);

        cancel.addClickListener(e -> {
            UI.getCurrent().navigate(StorageSearch.class);
            close();
        });
        save.addClickListener(e -> {

            if (!storage.isEmpty() && !cell.isEmpty() && (!material.isEmpty() || material.getValue().length() != 0) && !qty.isEmpty() &&
            !meas.isEmpty() && !costPrice.isEmpty() && !marketPrice.isEmpty() && supplierID != 0) {
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
                        materialInfoEntity.setMarketPrice(costPrice.getValue());
                        materialInfoEntity.setDiffPrice(marketPrice.getValue() - costPrice.getValue());
                        materialInfoEntity.setFlagMove(0L);
                        materialInfoEntity.setDelete(0L);
                        materialInfoEntity.setIdLocation(locationID);
                        materialInfoEntity.setIdSupplier(supplierID);
                        materialInfoEntity.setDescription(description.getValue());
                        materialInfoEntity.setQrNewMaterial("empty");
                        try {
                            materialInfoService.saveAll(materialInfoEntity);
                            //UI.getCurrent().navigate(StorageSearch.class);
                            //close();
                            printLabel.setEnabled(true);
                            cancel.setEnabled(false);
                            save.setEnabled(false);
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
        printLabel.addClickListener(e -> {
            Dialog prihodLabelPrint;
            List<DataLabel> dataLabelList = new ArrayList<>();
            DataLabel dataLabel = new DataLabel();
            dataLabel.setStorage(storage.getValue().getStorageName());
            dataLabel.setCell(cell.getValue().getCellName());
            dataLabel.setSupplier(supplier.getValue().getSupplierName());
            dataLabel.setMaterialInfo(material.getValue());
            dataLabel.setArticle(article.getValue());

            dataLabelList.add(dataLabel);

            prihodLabelPrint = new PrihodLabelPrint(dataLabelList, materialInfoEntity.getId(), materialInfoService);
            try {
                prihodLabelPrint.open();
                UI.getCurrent().navigate(StorageSearch.class);
                close();
            }
            catch (Exception ex) {
                Notification.show("Не могу открыть этикетку!"+ex.getMessage(), 5000, Notification.Position.MIDDLE);
                prihodLabelPrint.close();
            }
        });
    }
    /**
     * Метод для формирования артикула в формате SSS-NNNNN_YY
     * @param article - Последний сгенерированный артикул
     * @param shortStoreName - Короткое название склада в котором будет храниться объект
     * @param meas - Единица измерения объекта хранения
     * @return - Возвращает новый артикул в заданном формате
     */
    private String createArticle(String article, String shortStoreName, String meas) {

        int givenNumber;
        String formattedNumber;
        int lastTwoDigits;

        if (article.equals("0") || article.equals(""))
            givenNumber = 1;
        else {
            String[] lastIDList = article.split("-");
            givenNumber = Integer.valueOf(lastIDList[1]);
            givenNumber = givenNumber + 1;
        }
        formattedNumber = String.format("%05d", givenNumber);
        lastTwoDigits = Calendar.getInstance().get(Calendar.YEAR) % 100; //Получаем последние 2 цифры текущего года

        return shortStoreName +"-"+ formattedNumber+"-"+lastTwoDigits+"-"+meas;
    }

}
