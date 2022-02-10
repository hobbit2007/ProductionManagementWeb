package com.vaadin.tutorial.crm.ui.plccontrollersui;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import com.vaadin.tutorial.crm.entity.HistoryEntity;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.SignalGroup;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.SignalList;
import com.vaadin.tutorial.crm.security.SecurityUtils;
import com.vaadin.tutorial.crm.service.HistoryService;
import com.vaadin.tutorial.crm.service.plccontrollersservice.SignalGroupsService;
import com.vaadin.tutorial.crm.service.plccontrollersservice.SignalListService;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;
import com.vaadin.tutorial.crm.ui.layout.PlcSignalLayout;

import java.util.Date;

/**
 * Класс реализующий детальный просмотр по переменной ПЛК
 */
@Route(value = "formdetailsignal", layout = PlcSignalLayout.class)
@PageTitle("Детальный просмотр переменной ПЛК | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class FormSignalDetail extends FormLayout {
    TextField varName = new TextField("Имя переменной:");
    TextArea varDescription = new TextArea("Описание переменной:");
    NumberField db = new NumberField("База:");
    NumberField position = new NumberField("Позиция:");
    NumberField offset = new NumberField("Длина, байты");
    ComboBox<SignalGroup> varGroup = new ComboBox<>("Группа переменной:");
    Button close = new Button("Закрыть");
    Button edit = new Button("Редактировать");
    int flag = 0;//Флаг определяющий текст на кнопке 0 - Редактировать, 1 - Сохранить
    private final SignalListService signalListService;
    private final SignalGroupsService signalGroupsService;
    private long groupID, varID;

    public FormSignalDetail(SignalListService signalListService, SignalGroupsService signalGroupsService, HistoryService historyService) {
        this.signalListService = signalListService;
        this.signalGroupsService = signalGroupsService;
        addClassName("contact-form");

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

        setResponsiveSteps(new FormLayout.ResponsiveStep("50px", 3));
        add(varName, varDescription, db, position, offset, varGroup, close, edit);

        edit.addClickListener(e -> {
           varName.setReadOnly(false);
           varDescription.setReadOnly(false);
           db.setReadOnly(false);
           position.setReadOnly(false);
           offset.setReadOnly(false);
           varGroup.setReadOnly(false);
           flag = 1;

            if (edit.getText().equals("Сохранить")) {
                if (!varName.isEmpty() && !varDescription.isEmpty() && !db.isEmpty()) {
                    if (AnyComponent.checkEscSymbol(varName)) {
                        SignalList signalList = new SignalList();
                        HistoryEntity historyEntity = new HistoryEntity();
                        signalList.setSignalName(varName.getValue());
                        signalList.setSignalDescription(varDescription.getValue());
                        signalList.setDbValue(db.getValue().intValue());
                        signalList.setPosition(position.getValue().intValue());
                        signalList.setFOffset(offset.getValue().intValue());
                        signalList.setIdGroup(groupID);
                        signalList.setId(varID);

                        historyEntity.setAction("Обновление переменной: " + varName.getValue() + " DBValue = " + db.getValue().intValue() + " Position = " + position.getValue().intValue() + " Offset = " + offset.getValue().intValue());
                        historyEntity.setPlace("Редактирование переменной ПЛК");
                        historyEntity.setIdUser(SecurityUtils.getAuthentication().getDetails().getId());
                        historyEntity.setCreateRecordDate(new Date());
                        historyEntity.setDelete(0L);

                        try {
                            signalListService.updateValue(signalList);
                            historyService.saveAll(historyEntity);

                            flag = 0;
                        }
                        catch (Exception ex) {
                            Notification.show("Не могу обновить данные!" + ex.getMessage(), 5000, Notification.Position.MIDDLE);
                            return;
                        }
                    }
                    else {
                        Notification.show("Имя переменной не может начинаться со спецсимволов!", 3000, Notification.Position.MIDDLE);
                        return;
                    }
                }
                else {
                    Notification.show("Некоторые поля не заполнены!", 3000, Notification.Position.MIDDLE);
                    return;
                }
            }

            if (flag == 1)
                edit.setText("Сохранить");
            if (flag == 0) {
                edit.setText("Редактировать");
                varName.setReadOnly(true);
                varDescription.setReadOnly(true);
                db.setReadOnly(true);
                position.setReadOnly(true);
                offset.setReadOnly(true);
                varGroup.setReadOnly(true);
            }
        });
        varGroup.addValueChangeListener(e -> {
           if (e.getValue() != null)
               groupID = e.getValue().getId();
        });
        close.addClickListener(event -> fireEvent(new ContactFormEvent.CloseEvent(this)));
    }
    public void setSignalInfo(SignalList signalList) {
        if (signalList != null) {
            SignalGroup signalGroup = new SignalGroup();
            varName.setValue(signalList.getSignalName());
            varName.setReadOnly(true);

            varDescription.setValue(signalList.getSignalDescription());
            varDescription.setReadOnly(true);

            db.setValue((double) signalList.getDbValue());
            db.setReadOnly(true);

            position.setValue((double) signalList.getPosition());
            position.setReadOnly(true);

            offset.setValue((double) signalList.getFOffset());
            offset.setReadOnly(true);

            varGroup.setItems(signalGroupsService.getAll());
            signalGroup.setShortSignalName(signalList.getGroupName().getShortSignalName());
            varGroup.setValue(signalGroup);
            varGroup.setItemLabelGenerator(SignalGroup::getShortSignalName);
            varGroup.setReadOnly(true);

            groupID = signalList.getIdGroup();
            varID = signalList.getId();
        }
    }
    public static abstract class ContactFormEvent extends ComponentEvent<FormSignalDetail> {
        private static Double value;
        protected ContactFormEvent(FormSignalDetail source, Double value) {
            super(source, false);
            this.value = value;
        }

        public static Double getValue() {
            return value;
        }

        public static class EditEvent extends FormSignalDetail.ContactFormEvent {
            EditEvent(FormSignalDetail source, Double value) {
                super(source, value);
            }
        }

        public static class CloseEvent extends FormSignalDetail.ContactFormEvent {
            CloseEvent(FormSignalDetail source) {
                super(source, 0d);
            }
        }

    }
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
