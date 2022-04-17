package com.vaadin.tutorial.crm.ui.storage;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.tutorial.crm.entity.storage.*;
import com.vaadin.tutorial.crm.security.SecurityUtils;
import com.vaadin.tutorial.crm.service.storage.*;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;

import java.util.Date;

/**
 * Класс диалог реализующий перемещение объекта хранения между складами и ячейками
 */
public class MoveStoreDialog extends Dialog {
    VerticalLayout vMain = new VerticalLayout();
    HorizontalLayout hMain = new HorizontalLayout();
    HorizontalLayout hButton = new HorizontalLayout();
    ComboBox<LocationEntity> location = new ComboBox<>("Текущая локация:");
    ComboBox<StorageEntity> storage = new ComboBox<>("Текущий склад:");
    ComboBox<CellEntity> cell = new ComboBox<>("Текущая ячейка:");
    ComboBox<LocationEntity> locationNew = new ComboBox<>("Выберите локацию:");
    ComboBox<StorageEntity> storageNew = new ComboBox<>("Выберите склад:");
    ComboBox<CellEntity> cellNew = new ComboBox<>("Выберите ячейку:");
    NumberField expense = new NumberField("Количество:");
    Button move = new Button("Переместить");
    Button cancel = new Button("Отмена");
    long storageNewID = 0;
    long cellNewID = 0;
    long locationNewID = 0;

    public MoveStoreDialog(StorageService storageService, CellService cellService, Long storageID, Long cellID,
                           MaterialMoveService materialMoveService, Long materialID, MaterialInfoService materialInfoService, Long locationID,
                           LocationService locationService, MaterialInfoEntity materialInfo) {
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        setDraggable(true);

        Icon icon1 = new Icon(VaadinIcon.INSERT);
        move.setIcon(icon1);
        move.getStyle().set("background-color", "#d3b342");
        move.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Icon icon2 = new Icon(VaadinIcon.CLOSE);
        cancel.setIcon(icon2);
        cancel.getStyle().set("background-color", "#d3b342");
        cancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        hButton.add(move, cancel);

        LocationEntity locationEntity = new LocationEntity();
        locationEntity.setLocationName(locationService.getFindLocationByID(locationID).get(0).getLocationName()+"-"+locationService.getFindLocationByID(locationID).get(0).getLocationDescription());
        location.setItems(locationService.getFindLocationByStorageID(storageID));
        ItemLabelGenerator<LocationEntity> itemLabelGenerator = locationEntity1 -> locationEntity.getLocationName() + " - " + locationEntity.getLocationDescription();
        location.setItemLabelGenerator(itemLabelGenerator);
        location.setValue(locationEntity);
        location.setReadOnly(true);

        StorageEntity storageEntity = new StorageEntity();
        storageEntity.setStorageName(storageService.getFindStorageByID(storageID).get(0).getStorageName());
        storage.setItems(storageService.getFindStorageByID(storageID));
        storage.setItemLabelGenerator(StorageEntity::getStorageName);
        storage.setValue(storageEntity);
        storage.setReadOnly(true);

        CellEntity cellEntity = new CellEntity();
        cellEntity.setCellName(cellService.getFindCellByID(cellID, storageID).get(0).getCellName());
        cell.setItems(cellService.getFindCellByID(cellID, storageID));
        cell.setItemLabelGenerator(CellEntity::getCellName);
        cell.setValue(cellEntity);
        cell.setReadOnly(true);

        storageNew.setItems(storageService.getAll());
        storageNew.setItemLabelGenerator(StorageEntity::getStorageName);

        locationNew.setEnabled(false);

        cellNew.setEnabled(false);

        expense.setValue(materialInfoService.getCheckID(materialID).get(0).getBalance());

        hMain.add(storage, location, cell, storageNew, locationNew, cellNew);
        hMain.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        vMain.add(new AnyComponent().labelTitle("Перемещение между складом/ячейкой"), hMain, expense, hButton);
        vMain.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        add(vMain);

        locationNew.addValueChangeListener(e -> {
           if (e.getValue() != null) {
               locationNewID = e.getValue().getId();
               cellNew.setEnabled(true);
               cellNew.setItems(cellService.getAll(storageID, locationID));
               cellNew.setItemLabelGenerator(CellEntity::getCellName);
           }
           else
               cellNew.setEnabled(false);
        });
        storageNew.addValueChangeListener(e -> {
           if (e.getValue() != null) {
               storageNewID = e.getValue().getId();
               locationNew.setEnabled(true);
               locationNew.setItems(locationService.getFindLocationByStorageID(storageNewID));
               ItemLabelGenerator<LocationEntity> itemLabelGeneratorNew = locationEntity1 -> locationEntity.getLocationName();
               locationNew.setItemLabelGenerator(itemLabelGeneratorNew);
           }
           else
               locationNew.setEnabled(false);
        });
        cellNew.addValueChangeListener(e -> {
           if (e.getValue() != null)
               cellNewID = e.getValue().getId();
        });
        cancel.addClickListener(e -> close());
        move.addClickListener(e -> {
           if (!locationNew.isEmpty() && !storageNew.isEmpty() && !cellNew.isEmpty() && !expense.isEmpty()) {
               if (expense.getValue() <= materialInfoService.getCheckID(materialID).get(0).getBalance()) {
                   try {
                       MaterialMoveEntity materialMoveEntity = new MaterialMoveEntity();
                       materialMoveEntity.setIdMaterial(materialID);
                       materialMoveEntity.setIdStorageOld(storageID);
                       materialMoveEntity.setIdStorageNew(storageNewID);
                       materialMoveEntity.setIdCellOld(cellID);
                       materialMoveEntity.setIdCellNew(cellNewID);
                       materialMoveEntity.setExpense(expense.getValue());
                       materialMoveEntity.setIdShop(3);
                       materialMoveEntity.setIdDepartment(10);
                       materialMoveEntity.setIdUser(2);
                       materialMoveEntity.setDescription("empty");
                       materialMoveEntity.setWriteoff(0);
                       materialMoveEntity.setAction("перемещение склад/ячейка");
                       materialMoveEntity.setForWhom("empty");
                       materialMoveEntity.setIdUserCreate(SecurityUtils.getAuthentication().getDetails().getId());
                       materialMoveEntity.setDateCreate(new Date());
                       materialMoveEntity.setDelete(0);
                       materialMoveEntity.setIdLocationOld(locationID);
                       materialMoveEntity.setIdLocationNew(locationNewID);

                       materialMoveService.saveAll(materialMoveEntity);

                       if (materialInfoService.getCheckID(materialID).get(0).getBalance() - expense.getValue() > 0) { // Если перемещаем не весь материал
                           try {
                               MaterialInfoEntity materialInfoEntity = new MaterialInfoEntity();
                               materialInfoEntity.setFlagMove(1);
                               materialInfoEntity.setBalance(materialInfoService.getCheckID(materialID).get(0).getBalance() - expense.getValue());
                               materialInfoEntity.setExpense(expense.getValue());
                               materialInfoEntity.setId(materialID);

                               materialInfoService.updateMaterialInfoBalance(materialInfoEntity);
                           }
                           catch (Exception ex) {
                               Notification.show("Не могу обновить запись после перемещения материала!" + ex.getMessage(), 10000, Notification.Position.MIDDLE);
                               return;
                           }

                           try {
                               MaterialInfoEntity materialInfoEntity = new MaterialInfoEntity();
                               materialInfoEntity.setFlagMove(0);//из этой ячейки этот материал еще не перемещали
                               materialInfoEntity.setMaterialName(materialInfo.getMaterialName());
                               materialInfoEntity.setIdStorage(materialMoveEntity.getIdStorageNew());
                               materialInfoEntity.setIdLocation(materialMoveEntity.getIdLocationNew());
                               materialInfoEntity.setIdCell(materialMoveEntity.getIdCellNew());
                               materialInfoEntity.setArticle(materialInfo.getArticle());
                               materialInfoEntity.setQuantity(expense.getValue());
                               materialInfoEntity.setBalance(expense.getValue());
                               materialInfoEntity.setExpense(0);
                               materialInfoEntity.setDateCreate(new Date());
                               materialInfoEntity.setIdUser(SecurityUtils.getAuthentication().getDetails().getId());
                               materialInfoEntity.setWriteoff(0);
                               materialInfoEntity.setCostPrice(materialInfo.getCostPrice());
                               materialInfoEntity.setMarketPrice(materialInfo.getMarketPrice());
                               materialInfoEntity.setDiffPrice(materialInfo.getDiffPrice());
                               materialInfoEntity.setDelete(0);
                               materialInfoEntity.setIdSupplier(materialInfo.getIdSupplier());
                               materialInfoEntity.setDescription(materialInfo.getDescription());
                               materialInfoEntity.setQrNewMaterial(materialInfo.getQrNewMaterial());
                               materialInfoEntity.setIdMeas(materialInfo.getIdMeas());

                               materialInfoService.saveAll(materialInfoEntity);
                           }
                           catch (Exception ex) {
                               Notification.show("Не могу создать новую запись для перемещаемого материала!" + ex.getMessage(), 50000, Notification.Position.MIDDLE);
                               return;
                           }
                       }
                       if (materialInfoService.getCheckID(materialID).get(0).getBalance() - expense.getValue() == 0) {
                           try {
                               MaterialInfoEntity materialInfoEntity = new MaterialInfoEntity();
                               materialInfoEntity.setFlagMove(1);
                               materialInfoEntity.setIdStorage(materialMoveEntity.getIdStorageNew());
                               materialInfoEntity.setIdLocation(materialMoveEntity.getIdLocationNew());
                               materialInfoEntity.setIdCell(materialMoveEntity.getIdCellNew());
                               //materialInfoEntity.setBalance(materialInfoService.getCheckID(materialID).get(0).getBalance() - expense.getValue());
                               //materialInfoEntity.setExpense(expense.getValue());
                               materialInfoEntity.setId(materialID);

                               materialInfoService.updateMaterialInfoStorage(materialInfoEntity);
                           }
                           catch (Exception ex) {
                               Notification.show("Не могу обновить запись при полном перемещении материала! " + ex.getMessage(), 11000, Notification.Position.MIDDLE);
                               return;
                           }
                       }

                       close();
                   } catch (Exception ex) {
                       Notification.show("Не могу сохранить или обновить запись!" + ex.getMessage(), 5000, Notification.Position.MIDDLE);
                       return;
                   }
               }
               else {
                   Notification.show("Такого количества нет на складе!", 3000, Notification.Position.MIDDLE);
                   return;
               }
           }
           else {
               Notification.show("Не все заполнено!", 3000, Notification.Position.MIDDLE);
               return;
           }
        });
    }
}
