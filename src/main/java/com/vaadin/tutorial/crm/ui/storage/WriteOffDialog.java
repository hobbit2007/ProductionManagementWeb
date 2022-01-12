package com.vaadin.tutorial.crm.ui.storage;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.tutorial.crm.entity.storage.MaterialInfoEntity;
import com.vaadin.tutorial.crm.entity.storage.MaterialMoveEntity;
import com.vaadin.tutorial.crm.security.SecurityUtils;
import com.vaadin.tutorial.crm.service.storage.MaterialInfoService;
import com.vaadin.tutorial.crm.service.storage.MaterialMoveService;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;

import java.util.Date;

/**
 * Класс реализующий списание объекта хранения
 */
public class WriteOffDialog extends Dialog {
    VerticalLayout vMain = new VerticalLayout();
    HorizontalLayout hMain = new HorizontalLayout();
    HorizontalLayout hButton = new HorizontalLayout();
    NumberField expense = new NumberField("Количество:");
    TextArea description = new TextArea("Причина списания:");
    TextArea forWhom = new TextArea("Куда списано:");
    Button move = new Button("Списать");
    Button cancel = new Button("Отмена");
    public WriteOffDialog(MaterialInfoService materialInfoService, Long materialID, Long flagMove, Long storageID, Long cellID,
                          MaterialMoveService materialMoveService) {
        expense.setValue(materialInfoService.getCheckID(materialID).get(0).getBalance());
        expense.setRequiredIndicatorVisible(true);
        description.setPlaceholder("Максимум 1500 символов");
        description.setMaxLength(1500);
        description.setRequired(true);
        forWhom.setPlaceholder("Максимум 1500 символов");
        forWhom.setMaxLength(1500);
        forWhom.setRequired(true);

        Icon icon1 = new Icon(VaadinIcon.INSERT);
        move.setIcon(icon1);
        move.getStyle().set("background-color", "#d3b342");
        move.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Icon icon2 = new Icon(VaadinIcon.CLOSE);
        cancel.setIcon(icon2);
        cancel.getStyle().set("background-color", "#d3b342");
        cancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        hMain.add(description, forWhom);
        hButton.add(move, cancel);
        vMain.add(new AnyComponent().labelTitle("Списание"), expense, hMain, hButton);
        vMain.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        add(vMain);

        cancel.addClickListener(e -> close());
        move.addClickListener(e -> {
            if (!description.isEmpty() && !forWhom.isEmpty() && !expense.isEmpty()) {
                if (expense.getValue() <= materialInfoService.getCheckID(materialID).get(0).getBalance()) {
                    try {
                        MaterialMoveEntity materialMoveEntity = new MaterialMoveEntity();
                        materialMoveEntity.setIdMaterial(materialID);
                        materialMoveEntity.setIdStorageOld(storageID);
                        materialMoveEntity.setIdStorageNew(storageID);
                        materialMoveEntity.setIdCellOld(cellID);
                        materialMoveEntity.setIdCellNew(cellID);
                        materialMoveEntity.setExpense(expense.getValue());
                        materialMoveEntity.setIdShop(3);
                        materialMoveEntity.setIdDepartment(10);
                        materialMoveEntity.setIdUser(2);
                        materialMoveEntity.setDescription(description.getValue());
                        materialMoveEntity.setWriteoff(1);
                        materialMoveEntity.setAction("списание");
                        materialMoveEntity.setForWhom(forWhom.getValue());
                        materialMoveEntity.setIdUserCreate(SecurityUtils.getAuthentication().getDetails().getId());
                        materialMoveEntity.setDateCreate(new Date());
                        materialMoveEntity.setDelete(0);

                        materialMoveService.saveAll(materialMoveEntity);

                        MaterialInfoEntity materialInfoEntity = new MaterialInfoEntity();
                        materialInfoEntity.setFlagMove(flagMove);
                        materialInfoEntity.setIdStorage(materialMoveEntity.getIdStorageNew());
                        materialInfoEntity.setIdCell(materialMoveEntity.getIdCellNew());
                        materialInfoEntity.setBalance(materialInfoService.getCheckID(materialID).get(0).getBalance() - expense.getValue());
                        materialInfoEntity.setExpense(expense.getValue());
                        materialInfoEntity.setId(materialID);

                        materialInfoService.updateMaterialInfoStorageCell(materialInfoEntity);
                        if (materialInfoEntity.getBalance() == 0)
                            materialInfoService.updateWriteOff(1L, materialID);

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
