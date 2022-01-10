package com.vaadin.tutorial.crm.ui.storage;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;
import com.vaadin.tutorial.crm.entity.storage.MaterialInfoEntity;
import com.vaadin.tutorial.crm.entity.storage.StorageComingEntity;
import com.vaadin.tutorial.crm.security.SecurityUtils;
import com.vaadin.tutorial.crm.service.storage.MaterialInfoService;
import com.vaadin.tutorial.crm.service.storage.StorageComingService;
import org.apache.commons.math3.util.*;
import oshi.jna.platform.mac.SystemB;

import java.util.Date;

/**
 * Класс форма реализующий детальный просмотр информации по объекту хранения
 */
public class FormMaterialDetail extends FormLayout {
    HorizontalLayout hButton = new HorizontalLayout();
    TextField storage = new TextField("Склад:");
    TextField cell = new TextField("Ячейка:");
    TextField material = new TextField("Объект хранения:");
    TextField article = new TextField("Артикул:");
    NumberField qty = new NumberField("Приход:");
    NumberField expense = new NumberField("Расход:");
    NumberField balance = new NumberField("Остаток:");
    TextField meas = new TextField("Ед. измерения:");
    NumberField costPrice = new NumberField("Цена, себестоимость");
    NumberField marketPrice = new NumberField("Цена, продажа:");
    NumberField diffPrice = new NumberField("Цена, разница:");
    TextField dateCreate = new TextField("Дата создания:");
    TextField user = new TextField("Кто создал:");
    Button close = new Button("Закрыть");
    Button edit = new Button("Редактировать");
    Button prihod = new Button("Сделать приход");
    Button moveStore = new Button("Пер-ние склад/ячейка");
    Button moveInside = new Button("Внутреннее пер-ние");
    Button writeOff = new Button("Списать");
    Button prihodHistory = new Button("История приход");
    private final String ROLE = "ADMIN";
    long materialID = 0;
    double qtyOld;
    double balanceOld;
    int flag = 0;//Флаг определяющий текст на кнопке 0 - Редактировать, 1 - Сохранить
    int flagPrihod = 0;//Флаг определяющий текст на кнопке 0 - Сделать приход, 1 - Сохранить
    public FormMaterialDetail(MaterialInfoService materialInfoService, StorageComingService storageComingService) {
        addClassName("contact-form");

        close.getStyle().set("background-color", "#d3b342");
        close.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        edit.getStyle().set("background-color", "#d3b342");
        edit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        prihod.getStyle().set("background-color", "#d3b342");
        prihod.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        moveStore.getStyle().set("background-color", "#d3b342");
        moveStore.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        moveInside.getStyle().set("background-color", "#d3b342");
        moveInside.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        writeOff.getStyle().set("background-color", "#d3b342");
        writeOff.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        prihodHistory.getStyle().set("background-color", "#d3b342");
        prihodHistory.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        setResponsiveSteps(new FormLayout.ResponsiveStep("50px", 4));

        //if (SecurityUtils.getAuthentication().getDetails().getRole().equals(ROLE))
        //    edit.setVisible(true);
        //else
        //    edit.setVisible(false);

        add(storage, cell, material, article, qty, expense, balance, meas, costPrice, marketPrice, diffPrice, dateCreate, user, close,
                edit, prihod, moveStore, moveInside, writeOff, prihodHistory);

        //Если записи в таблице истории приходов есть, то кнопку активируем
        if (storageComingService.getAll().size() != 0)
            prihodHistory.setEnabled(true);
        else
            prihodHistory.setEnabled(false);

        close.addClickListener(event -> fireEvent(new ContactFormEvent.CloseEvent(this)));

        edit.addClickListener(e -> {
            material.setReadOnly(false);
            article.setReadOnly(false);
            flag = 1;
            if (edit.getText().equals("Сохранить")) {
                MaterialInfoEntity materialInfoEntity = new MaterialInfoEntity();
                material.setReadOnly(true);
                article.setReadOnly(true);

                try {
                    if (!material.getValue().isEmpty() && !article.getValue().isEmpty() && !qty.isEmpty() &&
                            !costPrice.isEmpty() && !marketPrice.isEmpty() && materialID != 0) {

                        materialInfoEntity.setId(materialID);
                        materialInfoEntity.setMaterialName(material.getValue());
                        materialInfoEntity.setArticle(article.getValue());

                        materialInfoService.updateValue(materialInfoEntity);

                        flag = 0;
                    }
                    else {
                        Notification.show("Некоторые поля не заполнены!", 3000, Notification.Position.MIDDLE);
                        return;
                    }
                }
                catch (Exception ex) {
                    Notification.show("Не могу обновить данные!" + ex.getMessage(), 5000, Notification.Position.MIDDLE);
                    return;
                }
            }
            if (flag == 1)
                edit.setText("Сохранить");
            if (flag == 0)
                edit.setText("Редактировать");
        });

        prihod.addClickListener(e -> {
            qty.setReadOnly(false);
            flagPrihod = 1;
            if (prihod.getText().equals("Сохранить")) {
                MaterialInfoEntity materialInfoEntity = new MaterialInfoEntity();
                qty.setReadOnly(true);
                if (!qty.isEmpty() && materialID != 0) {
                    materialInfoEntity.setQuantity(qty.getValue());
                    //Округляем до двух знаков после запятой
                    materialInfoEntity.setBalance(Precision.round(qty.getValue() + materialInfoService.getCheckArticle(article.getValue()).get(0).getBalance(), 2));
                    materialInfoEntity.setId(materialID);
                    try {
                        materialInfoService.updatePrihod(materialInfoEntity);
                        additionalPrihod(materialInfoService, storageComingService, qtyOld, materialInfoEntity.getQuantity(), balanceOld, materialInfoEntity.getBalance());

                        balance.setValue(materialInfoEntity.getBalance());
                        flagPrihod = 0;
                    } catch (Exception ex) {
                        Notification.show("Не могу обновить данные!" + ex.getMessage(), 5000, Notification.Position.MIDDLE);
                        return;
                    }
                } else {
                    Notification.show("Не заполнено поле количество!", 3000, Notification.Position.MIDDLE);
                    return;
                }
            }
            if (flagPrihod == 1) {
                prihod.setText("Сохранить");
                qtyOld = qty.getValue();
                balanceOld = balance.getValue();
            }
            if (flagPrihod == 0)
                prihod.setText("Сделать приход");
        });

        prihodHistory.addClickListener(e -> {
           new PrihodHistoryDialog(storageComingService).open();
        });
    }
    public void setMaterialInfo(MaterialInfoEntity materialInfoEntity) {
        if (materialInfoEntity != null) {
            Div pricePrefix = new Div();
            pricePrefix.setText("₽");
            Div pricePrefixCost = new Div();
            pricePrefixCost.setText("₽");
            Div pricePrefixMarket = new Div();
            pricePrefixMarket.setText("₽");

            storage.setValue(materialInfoEntity.getStorage().getStorageName());
            storage.getElement().setAttribute("data-title", storage.getValue());
            storage.setClassName("tooltip");
            storage.setReadOnly(true);

            cell.setValue(materialInfoEntity.getCell().getCellName());
            cell.setReadOnly(true);

            material.setValue(materialInfoEntity.getMaterialName());
            material.setReadOnly(true);

            article.setValue(materialInfoEntity.getArticle());
            article.setReadOnly(true);

            qty.setValue(materialInfoEntity.getQuantity());
            qty.setReadOnly(true);

            expense.setValue(materialInfoEntity.getExpense());
            expense.setReadOnly(true);

            balance.setValue(materialInfoEntity.getBalance());
            balance.setReadOnly(true);

            meas.setValue(materialInfoEntity.getMeas().getMeasName());
            meas.setReadOnly(true);

            costPrice.setValue(materialInfoEntity.getCostPrice());
            costPrice.setPrefixComponent(pricePrefixCost);
            costPrice.setReadOnly(true);

            marketPrice.setValue(materialInfoEntity.getMarketPrice());
            marketPrice.setPrefixComponent(pricePrefixMarket);
            marketPrice.setReadOnly(true);

            diffPrice.setValue(materialInfoEntity.getDiffPrice());
            diffPrice.setPrefixComponent(pricePrefix);
            diffPrice.setReadOnly(true);

            dateCreate.setValue(materialInfoEntity.getDateCreate().toString());
            dateCreate.setReadOnly(true);

            user.setValue(materialInfoEntity.getUser().getFio());
            user.getElement().setAttribute("data-title", user.getValue());
            user.setClassName("tooltip");
            user.setReadOnly(true);

            materialID = materialInfoEntity.getId();
        }
    }
    private void additionalPrihod(MaterialInfoService materialInfoService, StorageComingService storageComingService, Double qtyOld,
                                  Double qtyNew, Double balanceOld, Double balanceNew) {
        StorageComingEntity storageComingEntity = new StorageComingEntity();
        storageComingEntity.setIdMaterial(materialID);
        storageComingEntity.setQtyCome(qtyNew);
        storageComingEntity.setIdMeas(materialInfoService.getCheckArticle(article.getValue()).get(0).getIdMeas());
        storageComingEntity.setIdUser(SecurityUtils.getAuthentication().getDetails().getId());
        storageComingEntity.setDateCreate(new Date());
        storageComingEntity.setDelete(0L);
        storageComingEntity.setQtyOldCome(qtyOld);
        storageComingEntity.setBalanceOld(balanceOld);
        storageComingEntity.setBalanceNew(balanceNew);
        try {
            storageComingService.saveAll(storageComingEntity);
        }
        catch (Exception ex) {
            Notification.show("Не могу сохранить данные по приходу!" + ex.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }
    public static abstract class ContactFormEvent extends ComponentEvent<FormMaterialDetail> {
        private static Double value;
        protected ContactFormEvent(FormMaterialDetail source, Double value) {
            super(source, false);
            this.value = value;
        }

        public static Double getValue() {
            return value;
        }

        public static class EditEvent extends FormMaterialDetail.ContactFormEvent {
            EditEvent(FormMaterialDetail source, Double value) {
                super(source, value);
            }
        }

        public static class CloseEvent extends FormMaterialDetail.ContactFormEvent {
            CloseEvent(FormMaterialDetail source) {
                super(source, 0d);
            }
        }

    }
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
