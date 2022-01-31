package com.vaadin.tutorial.crm.ui.storage;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;
import com.vaadin.tutorial.crm.entity.storage.ChangePriceEntity;
import com.vaadin.tutorial.crm.entity.storage.MaterialInfoEntity;
import com.vaadin.tutorial.crm.entity.storage.StorageComingEntity;
import com.vaadin.tutorial.crm.security.SecurityUtils;
import com.vaadin.tutorial.crm.service.DepartmentService;
import com.vaadin.tutorial.crm.service.ShopService;
import com.vaadin.tutorial.crm.service.UserService;
import com.vaadin.tutorial.crm.service.storage.*;
import org.apache.commons.math3.util.*;

import java.util.Date;

/**
 * Класс форма реализующий детальный просмотр информации по объекту хранения
 */
public class FormMaterialDetail extends FormLayout {
    HorizontalLayout hButton = new HorizontalLayout();
    TextField location = new TextField("Локация:");
    TextField storage = new TextField("Склад:");
    TextField cell = new TextField("Ячейка:");
    TextField material = new TextField("Объект хранения:");
    TextField article = new TextField("Артикул:");
    TextField description = new TextField("Описание:");
    TextField supplier = new TextField("Поставщик:");
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
    Button moveSCHistory = new Button("История склад/ячейка");
    Button moveInsideHistory = new Button("История ВП");
    Button writeOffHistory = new Button("История списания");
    Button changePrice = new Button("Изменить цену");
    Button changePriceHistory = new Button("История ИЦ");
    private final String ROLE = "ADMIN";
    long materialID = 0;
    long locationID = 0;
    long storageID = 0;
    long cellID = 0;
    double qtyOld;
    double balanceOld;
    double costPriceOld;
    double marketPriceOld;
    double diffPriceOld;
    int flag = 0;//Флаг определяющий текст на кнопке 0 - Редактировать, 1 - Сохранить
    int flagPrihod = 0;//Флаг определяющий текст на кнопке 0 - Сделать приход, 1 - Сохранить
    int flagPrice = 0;//Флаг определяющий текст на кнопке 0 - Изменить цену, 1 - Сохранить
    private final StorageComingService storageComingService;
    private final MaterialMoveService materialMoveService;
    private final ChangePriceService changePriceService;
    public FormMaterialDetail(MaterialInfoService materialInfoService, StorageComingService storageComingService, StorageService storageService,
                              CellService cellService, MaterialMoveService materialMoveService, ShopService shopService,
                              DepartmentService departmentService, UserService userService, ChangePriceService changePriceService, LocationService locationService) {
        addClassName("contact-form");
        this.storageComingService = storageComingService;
        this.materialMoveService = materialMoveService;
        this.changePriceService = changePriceService;

        Icon icon = new Icon(VaadinIcon.CLOSE);
        close.setIcon(icon);
        close.getStyle().set("background-color", "#d3b342");
        close.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.getElement().setAttribute("data-title", close.getText());
        close.setClassName("tooltip");

        Icon icon1 = new Icon(VaadinIcon.EDIT);
        edit.setIcon(icon1);
        edit.getStyle().set("background-color", "#d3b342");
        edit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Icon icon2 = new Icon(VaadinIcon.PLUS_CIRCLE_O);
        prihod.setIcon(icon2);
        prihod.getStyle().set("background-color", "#d3b342");
        prihod.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Icon icon3 = new Icon(VaadinIcon.INSERT);
        moveStore.setIcon(icon3);
        moveStore.getStyle().set("background-color", "#d3b342");
        moveStore.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Icon icon11 = new Icon(VaadinIcon.INSERT);
        moveInside.setIcon(icon11);
        moveInside.getStyle().set("background-color", "#d3b342");
        moveInside.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Icon icon4 = new Icon(VaadinIcon.MINUS_CIRCLE_O);
        writeOff.setIcon(icon4);
        writeOff.getStyle().set("background-color", "#d3b342");
        writeOff.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Icon icon5 = new Icon(VaadinIcon.FILE_TEXT_O);
        prihodHistory.setIcon(icon5);
        prihodHistory.getStyle().set("background-color", "#d3b342");
        prihodHistory.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Icon icon6 = new Icon(VaadinIcon.FILE_TEXT_O);
        moveSCHistory.setIcon(icon6);
        moveSCHistory.getStyle().set("background-color", "#d3b342");
        moveSCHistory.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Icon icon7 = new Icon(VaadinIcon.FILE_TEXT_O);
        moveInsideHistory.setIcon(icon7);
        moveInsideHistory.getStyle().set("background-color", "#d3b342");
        moveInsideHistory.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Icon icon8 = new Icon(VaadinIcon.FILE_TEXT_O);
        writeOffHistory.setIcon(icon8);
        writeOffHistory.getStyle().set("background-color", "#d3b342");
        writeOffHistory.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Icon icon9 = new Icon(VaadinIcon.MONEY_EXCHANGE);
        changePrice.setIcon(icon9);
        changePrice.getStyle().set("background-color", "#d3b342");
        changePrice.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Icon icon10 = new Icon(VaadinIcon.FILE_TEXT_O);
        changePriceHistory.setIcon(icon10);
        changePriceHistory.getStyle().set("background-color", "#d3b342");
        changePriceHistory.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        setResponsiveSteps(new FormLayout.ResponsiveStep("50px", 4));

        if (SecurityUtils.getAuthentication().getDetails().getRole().equals(ROLE))
            changePrice.setEnabled(true);
        else
            changePrice.setEnabled(false);

        add(location, storage, cell, material, article, description, supplier, qty, expense, balance, meas, costPrice, marketPrice, diffPrice, dateCreate, user, close,
                edit, prihod, moveStore, moveInside, writeOff, changePrice, prihodHistory, moveSCHistory, moveInsideHistory, writeOffHistory,
                changePriceHistory);

        close.addClickListener(event -> fireEvent(new ContactFormEvent.CloseEvent(this)));
        //Обработка события кнопки Редактирование
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

        //Обработка события кнопки Сделать приход
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
                        prihodHistory.setEnabled(true);
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

        //Обработка события кнопки История приход
        prihodHistory.addClickListener(e -> {
           new PrihodHistoryDialog(storageComingService, materialID).open();
        });

        //Обработка события нажатия кнопки Перемещение между складом/ячейкой
        moveStore.addClickListener(e -> {
            if (materialInfoService.getCheckID(materialID).get(0).getBalance() > 0) {
                if (storageID != 0 && cellID != 0)
                    new MoveStoreDialog(storageService, cellService, storageID, cellID, materialMoveService, materialID, materialInfoService, locationID,
                            locationService).open();
                else {
                    Notification.show("Не могу найти склад или ячейку!", 3000, Notification.Position.MIDDLE);
                    return;
                }
            }
            else {
                Notification.show("Перемещение не возможно, нулевой остаток!", 3000, Notification.Position.MIDDLE);
                return;
            }
        });

        //Обработка нажатия кнопки Внутреннее перемещение
        moveInside.addClickListener(e -> {
            if (materialInfoService.getCheckID(materialID).get(0).getBalance() > 0) {
                new MoveInsideDialog(shopService, departmentService, userService, materialInfoService, materialID,
                        materialInfoService.getCheckID(materialID).get(0).getIdStorage(),
                        materialInfoService.getCheckID(materialID).get(0).getIdCell(), materialMoveService).open();
            }
            else {
                Notification.show("Перемещение не возможно, нулевой остаток!", 3000, Notification.Position.MIDDLE);
                return;
            }
        });

        //Обработка нажатия кнопки Списать
        writeOff.addClickListener(e -> {
            if (materialInfoService.getCheckID(materialID).get(0).getBalance() > 0) {
                new WriteOffDialog(materialInfoService, materialID, materialInfoService.getCheckID(materialID).get(0).getFlagMove(),
                        materialInfoService.getCheckID(materialID).get(0).getIdStorage(),
                        materialInfoService.getCheckID(materialID).get(0).getIdCell(), materialMoveService).open();
            }
            else {
                Notification.show("Списание не возможно, нулевой остаток!", 3000, Notification.Position.MIDDLE);
                return;
            }
        });

        //Обработка нажатия кнопки История перемещений склад/ячейка
        moveSCHistory.addClickListener(a -> {
           new MoveSCHistory(materialMoveService, materialID).open();
        });

        //Обработка нажатия кнопки История внутренних перемещений
        moveInsideHistory.addClickListener(a -> {
            new MoveInsideHistory(materialMoveService, materialID).open();
        });

        //Обработка нажатия кнопки История списания
        writeOffHistory.addClickListener(a -> {
            new WriteOffHistory(materialMoveService, materialID).open();
        });

        //Обработка события кнопки Изменить цену
        changePrice.addClickListener(e -> {
            costPrice.setReadOnly(false);
            marketPrice.setReadOnly(false);
            flagPrice = 1;
            if (changePrice.getText().equals("Сохранить")) {
                MaterialInfoEntity materialInfoEntity = new MaterialInfoEntity();
                ChangePriceEntity changePriceEntity = new ChangePriceEntity();
                costPrice.setReadOnly(true);
                marketPrice.setReadOnly(true);

                try {
                    if (!costPrice.isEmpty() && !marketPrice.isEmpty() && materialID != 0) {

                        materialInfoEntity.setId(materialID);
                        materialInfoEntity.setCostPrice(Precision.round(costPrice.getValue(), 2));
                        materialInfoEntity.setMarketPrice(Precision.round(marketPrice.getValue(), 2));
                        materialInfoEntity.setDiffPrice(Precision.round(materialInfoEntity.getMarketPrice() - materialInfoEntity.getCostPrice(), 2));

                        materialInfoService.updatePrice(materialInfoEntity);

                        if (!costPrice.isEmpty() && !marketPrice.isEmpty()) {
                            changePriceEntity.setIdMaterial(materialID);
                            changePriceEntity.setCostPriceOld(costPriceOld);
                            changePriceEntity.setMarketPriceOld(marketPriceOld);
                            changePriceEntity.setDiffPriceOld(diffPriceOld);
                            changePriceEntity.setCostPriceNew(costPrice.getValue());
                            changePriceEntity.setMarketPriceNew(marketPrice.getValue());
                            changePriceEntity.setDiffPriceNew(changePriceEntity.getMarketPriceNew() - changePriceEntity.getCostPriceNew());
                            changePriceEntity.setIdUserCreate(SecurityUtils.getAuthentication().getDetails().getId());
                            changePriceEntity.setDateCreate(new Date());
                            changePriceEntity.setDelete(0);
                            try {
                                changePriceService.saveAll(changePriceEntity);
                                diffPrice.setValue(materialInfoEntity.getDiffPrice());
                                changePriceHistory.setEnabled(true);
                            }
                            catch (Exception ex) {
                                Notification.show("Не могу записать данные!" + ex.getMessage(), 5000, Notification.Position.MIDDLE);
                                return;
                            }
                        }
                        else {
                            Notification.show("Не все поля заполнены!", 3000, Notification.Position.MIDDLE);
                            return;
                        }

                        flagPrice = 0;
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
            if (flagPrice == 1) {
                changePrice.setText("Сохранить");
                costPriceOld = costPrice.getValue();
                marketPriceOld = marketPrice.getValue();
                diffPriceOld = diffPrice.getValue();
            }
            if (flagPrice == 0)
                changePrice.setText("Изменить цену");
        });

        //Обработка нажатия кнопки История изменения цены
        changePriceHistory.addClickListener(a -> {
            new ChangePriceHistory(changePriceService, materialID).open();
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

            location.setValue(materialInfoEntity.getLocationEntity().getLocationName());
            location.getElement().setAttribute("data-title", location.getValue());
            location.setClassName("tooltip");
            location.setReadOnly(true);

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

            description.setValue(materialInfoEntity.getDescription());
            description.setReadOnly(true);
            description.getElement().setAttribute("data-title", description.getValue());
            description.setClassName("tooltip");

            supplier.setValue(materialInfoEntity.getSupplierEntity().getSupplierName());
            supplier.setReadOnly(true);
            supplier.getElement().setAttribute("data-title", supplier.getValue());
            supplier.setClassName("tooltip");

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
            locationID = materialInfoEntity.getIdLocation();
            storageID = materialInfoEntity.getIdStorage();
            cellID = materialInfoEntity.getIdCell();

            //Если записи в таблице истории приходов есть, то кнопку активируем
            if (storageComingService.getAllByIdMaterial(materialID).size() != 0)
                prihodHistory.setEnabled(true);
            else
                prihodHistory.setEnabled(false);
            if (materialMoveService.getAllByID(materialID, "перемещение склад/ячейка").size() != 0) //materialID, "перемещение склад/ячейка"
                moveSCHistory.setEnabled(true);
            else
                moveSCHistory.setEnabled(false);
            if (materialMoveService.getAllByID(materialID, "внутреннее перемещение").size() != 0) //materialID, "перемещение склад/ячейка"
                moveInsideHistory.setEnabled(true);
            else
                moveInsideHistory.setEnabled(false);
            if (materialMoveService.getAllWriteOffByID(materialID, "списание").size() != 0) //materialID, "перемещение склад/ячейка"
                writeOffHistory.setEnabled(true);
            else
                writeOffHistory.setEnabled(false);
            if (changePriceService.getAllByIdMaterial(materialID).size() != 0) //materialID, "перемещение склад/ячейка"
                changePriceHistory.setEnabled(true);
            else
                changePriceHistory.setEnabled(false);
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
