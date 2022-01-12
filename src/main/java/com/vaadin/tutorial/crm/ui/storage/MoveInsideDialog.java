package com.vaadin.tutorial.crm.ui.storage;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.tutorial.crm.entity.Department;
import com.vaadin.tutorial.crm.entity.Shop;
import com.vaadin.tutorial.crm.entity.User;
import com.vaadin.tutorial.crm.entity.storage.MaterialInfoEntity;
import com.vaadin.tutorial.crm.entity.storage.MaterialMoveEntity;
import com.vaadin.tutorial.crm.security.SecurityUtils;
import com.vaadin.tutorial.crm.service.DepartmentService;
import com.vaadin.tutorial.crm.service.ShopService;
import com.vaadin.tutorial.crm.service.UserService;
import com.vaadin.tutorial.crm.service.storage.MaterialInfoService;
import com.vaadin.tutorial.crm.service.storage.MaterialMoveService;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;

import java.util.Date;

/**
 * Класс диалог реализующий логику внутреннего перемещения объекта хранения
 */
public class MoveInsideDialog extends Dialog {
    VerticalLayout vMain = new VerticalLayout();
    HorizontalLayout hMain = new HorizontalLayout();
    HorizontalLayout hMain1 = new HorizontalLayout();
    HorizontalLayout hButton = new HorizontalLayout();
    Label label = new Label("Укажите куда происходит перемещение:");
    ComboBox<Shop> shop = new ComboBox<>("Подразделение:");
    ComboBox<Department> department = new ComboBox<>("Отдел:");
    ComboBox<User> fio = new ComboBox<>("Кому:");
    NumberField expense = new NumberField("Количество:");
    TextArea description = new TextArea("Примечание:");
    Button move = new Button("Переместить");
    Button cancel = new Button("Отмена");
    long shopID = 0;
    long departmentID = 0;
    long userID = 0;

    public MoveInsideDialog(ShopService shopService, DepartmentService departmentService, UserService userService,
                            MaterialInfoService materialInfoService, Long materialID, Long storageID, Long cellID,
                            MaterialMoveService materialMoveService) {
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

        department.setEnabled(false);
        fio.setEnabled(false);

        shop.setItems(shopService.getAll());
        shop.setItemLabelGenerator(Shop::getShopName);
        shop.setRequired(true);
        shop.addValueChangeListener(e -> {
           if (e.getValue() != null) {
               department.setEnabled(true);
               shopID = e.getValue().getId();
               department.setItems(departmentService.getAll(shopID));
               department.setItemLabelGenerator(Department::getDepartmentName);
               department.setRequired(true);
           }
        });
        department.addValueChangeListener(e -> {
           if (e.getValue() != null) {
               fio.setEnabled(true);
               departmentID = e.getValue().getId();
               fio.setItems(userService.getAllByIdDepartment(departmentID));
               fio.setItemLabelGenerator(User::getFio);
               fio.setRequired(true);
           }
        });
        fio.addValueChangeListener(e -> {
           if (e.getValue() != null)
               userID = e.getValue().getId();
        });
        expense.setValue(1d);
        expense.setRequiredIndicatorVisible(true);

        description.setPlaceholder("Максимум 1500 символов");
        description.setMaxLength(1500);
        description.setRequired(true);

        hMain.add(shop, department, fio);
        hMain1.add(expense, description);
        hButton.add(move, cancel);

        vMain.add(new AnyComponent().labelTitle("Внутренне перемещение"), label, hMain, hMain1, hButton);
        vMain.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        add(vMain);

        cancel.addClickListener(e -> close());
        move.addClickListener(e -> {
            if (!shop.isEmpty() && !department.isEmpty() && !fio.isEmpty() && !expense.isEmpty() && !description.isEmpty()) {
                if (expense.getValue() <= materialInfoService.getCheckID(materialID).get(0).getBalance()) {
                    try {
                        MaterialMoveEntity materialMoveEntity = new MaterialMoveEntity();
                        materialMoveEntity.setIdMaterial(materialID);
                        materialMoveEntity.setIdStorageOld(storageID);
                        materialMoveEntity.setIdStorageNew(storageID);
                        materialMoveEntity.setIdCellOld(cellID);
                        materialMoveEntity.setIdCellNew(cellID);
                        materialMoveEntity.setExpense(expense.getValue());
                        materialMoveEntity.setIdShop(shopID);
                        materialMoveEntity.setIdDepartment(departmentID);
                        materialMoveEntity.setIdUser(userID);
                        materialMoveEntity.setDescription(description.getValue());
                        materialMoveEntity.setWriteoff(0);
                        materialMoveEntity.setAction("внутреннее перемещение");
                        materialMoveEntity.setForWhom("empty");
                        materialMoveEntity.setIdUserCreate(SecurityUtils.getAuthentication().getDetails().getId());
                        materialMoveEntity.setDateCreate(new Date());
                        materialMoveEntity.setDelete(0);

                        materialMoveService.saveAll(materialMoveEntity);

                        MaterialInfoEntity materialInfoEntity = new MaterialInfoEntity();
                        materialInfoEntity.setFlagMove(1);
                        materialInfoEntity.setIdStorage(materialMoveEntity.getIdStorageNew());
                        materialInfoEntity.setIdCell(materialMoveEntity.getIdCellNew());
                        materialInfoEntity.setBalance(materialInfoService.getCheckID(materialID).get(0).getBalance() - expense.getValue());
                        materialInfoEntity.setExpense(expense.getValue());
                        materialInfoEntity.setId(materialID);

                        materialInfoService.updateMaterialInfoStorageCell(materialInfoEntity);

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
