package com.vaadin.tutorial.crm.ui.storage;

import com.vaadin.flow.component.UI;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.entity.storage.SupplierEntity;
import com.vaadin.tutorial.crm.security.SecurityUtils;
import com.vaadin.tutorial.crm.service.storage.SupplierService;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;
import com.vaadin.tutorial.crm.ui.layout.StorageLayout;

import java.util.Date;

/**
 * Класс диалог реализующий форму создания поставщика(контрагента)
 */
@Route(value = "supplieradd", layout = StorageLayout.class)
@PageTitle("Добавить поставщика | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class CreateSupplierDialog extends Dialog {
    VerticalLayout vMain = new VerticalLayout();
    HorizontalLayout hMain = new HorizontalLayout();
    HorizontalLayout hMain1 = new HorizontalLayout();
    TextField supplierName = new TextField("Введите имя поставщика:");
    TextField contractNumber = new TextField("Введите номер контракта:");
    Button save = new Button("Сохранить");
    Button cancel = new Button("Отмена");
    public CreateSupplierDialog(SupplierService supplierService) {
        this.open();
        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);
        setDraggable(true);

        supplierName.setWidth("255px");
        supplierName.setRequired(true);

        contractNumber.setWidth("255px");
        contractNumber.setRequired(true);

        Icon icon1 = new Icon(VaadinIcon.DISC);
        save.setIcon(icon1);
        save.getStyle().set("background-color", "#d3b342");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Icon icon2 = new Icon(VaadinIcon.CLOSE);
        cancel.setIcon(icon2);
        cancel.getStyle().set("background-color", "#d3b342");
        cancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        hMain1.add(supplierName, contractNumber);
        hMain.add(save, cancel);
        vMain.add(new AnyComponent().labelTitle("Добавить поставщика"), hMain1, hMain);
        vMain.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        add(vMain);

        cancel.addClickListener(e -> {
            UI.getCurrent().navigate(StorageSearch.class);
            close();
        });
        save.addClickListener(e -> {
           if (!supplierName.isEmpty() && !contractNumber.isEmpty()) {
               if (AnyComponent.checkEscSymbol(supplierName) && AnyComponent.checkEscSymbol(contractNumber)) {
                   if (supplierService.getCheckSupplier(supplierName.getValue()).size() == 0) {
                       SupplierEntity supplierEntity = new SupplierEntity();
                       supplierEntity.setSupplierName(supplierName.getValue());
                       supplierEntity.setContract(contractNumber.getValue());
                       supplierEntity.setIdUser(SecurityUtils.getAuthentication().getDetails().getId());
                       supplierEntity.setDateCreate(new Date());
                       supplierEntity.setDelete(0);
                       try {
                           supplierService.saveAll(supplierEntity);
                           UI.getCurrent().navigate(StorageSearch.class);
                           close();
                       }
                       catch (Exception ex) {
                           Notification.show("Не могу сохранить запись!" + ex.getMessage(), 5000, Notification.Position.MIDDLE);
                           return;
                       }
                   }
                   else {
                       Notification.show("Внимание! Поставщик с таким названием уже существует!", 5000, Notification.Position.MIDDLE);
                       return;
                   }
               }
               else {
                   Notification.show("Имя или номер контракта не может начинаться со спецсимволов!", 5000, Notification.Position.MIDDLE);
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
