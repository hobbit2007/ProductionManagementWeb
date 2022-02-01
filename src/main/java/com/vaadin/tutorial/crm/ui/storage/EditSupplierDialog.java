package com.vaadin.tutorial.crm.ui.storage;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.entity.storage.SupplierEntity;
import com.vaadin.tutorial.crm.service.storage.SupplierService;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;
import com.vaadin.tutorial.crm.ui.layout.StorageLayout;

import java.util.Collection;
import java.util.Collections;
import java.util.WeakHashMap;

/**
 * Класс диалог реализующий редактирование поставщика
 */
@Route(value = "supplieredit", layout = StorageLayout.class)
@PageTitle("Редактирование поставщика | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class EditSupplierDialog extends Dialog {
    Grid<SupplierEntity> grid = new Grid<>();
    Grid.Column<SupplierEntity> colSupplierName, colContractNumber;
    VerticalLayout vMain = new VerticalLayout();
    ListDataProvider<SupplierEntity> dataProvider;
    Button cancel = new Button("Закрыть");
    private final SupplierService supplierService;

    public EditSupplierDialog(SupplierService supplierService) {
        this.supplierService = supplierService;
        this.open();
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        setDraggable(true);
        setWidth("955px");
        setHeight("555px");
        Icon icon = new Icon(VaadinIcon.CLOSE);
        cancel.setIcon(icon);
        cancel.getStyle().set("background-color", "#d3b342");
        cancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        configureGrid();
        updateGrid();

        vMain.add(new AnyComponent().labelTitle("Редактировать поставщика"), grid, cancel);
        vMain.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        vMain.setSizeFull();

        add(vMain);

        cancel.addClickListener(e -> {
            close();
            UI.getCurrent().navigate(StorageSearch.class);
        });
    }
    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS,
                GridVariant.LUMO_ROW_STRIPES);
        grid.setColumnReorderingAllowed(true);
        grid.addClassName("contact-grid");
        grid.setSizeFull();

        colSupplierName = grid.addColumn(supplierEntity -> supplierEntity.getSupplierName()).setHeader("Название поставщика");
        colContractNumber = grid.addColumn(supplierEntity -> supplierEntity.getContract()).setHeader("Номер контракта");

        Binder<SupplierEntity> binder = new Binder<>(SupplierEntity.class);
        Editor<SupplierEntity> editor = grid.getEditor();
        editor.setBinder(binder);
        editor.setBuffered(true);

        Div validationStatus = new Div();
        validationStatus.setId("validation");

        TextField supplierName = new TextField();
        binder.forField(supplierName)
                .withValidator(new StringLengthValidator("Название поставщика должно содержать минимум 1 символов", 1, 250))
                .withStatusLabel(validationStatus).bind("supplierName");
        colSupplierName.setEditorComponent(supplierName);
        TextField contractNumber = new TextField();
        binder.forField(contractNumber)
                .withValidator(new StringLengthValidator("Номер контракта должен содержать минимум 3 символа", 3, 250))
                .withStatusLabel(validationStatus).bind("contract");
        colContractNumber.setEditorComponent(contractNumber);

        Collection<Button> editButtons = Collections
                .newSetFromMap(new WeakHashMap<>());

        Grid.Column<SupplierEntity> editorColumn = grid.addComponentColumn(supplier -> {
            Icon icon = new Icon(VaadinIcon.EDIT);
            Button edit = new Button("Редактировать");
            edit.setIcon(icon);
            edit.getStyle().set("background-color", "#d3b342");
            edit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            edit.addClickListener(e -> {
                editor.editItem(supplier);
                supplierName.focus();
            });
            edit.setEnabled(!editor.isOpen());
            editButtons.add(edit);
            return edit;
        }).setFlexGrow(0).setWidth("310px");

        editor.addOpenListener(e -> editButtons.stream()
                .forEach(button -> button.setEnabled(!editor.isOpen())));
        editor.addCloseListener(e -> editButtons.stream()
                .forEach(button -> button.setEnabled(!editor.isOpen())));

        Icon icon = new Icon(VaadinIcon.DISC);
        Button save = new Button("Сохранить", e -> editor.save());
        save.setIcon(icon);
        save.getStyle().set("background-color", "#d3b342");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Icon icon1 = new Icon(VaadinIcon.CLOSE);
        Button cancel = new Button("Отмена", e -> editor.cancel());
        cancel.setIcon(icon1);
        cancel.getStyle().set("background-color", "#d3b342");
        cancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        grid.getElement().addEventListener("keyup", event -> editor.cancel())
                .setFilter("event.key === 'Escape' || event.key === 'Esc'");

        HorizontalLayout hSeparator = new HorizontalLayout();
        hSeparator.setWidth("3px");
        HorizontalLayout hDiv = new HorizontalLayout();
        hDiv.add(save, hSeparator, cancel);

        Div buttons = new Div(hDiv);
        editorColumn.setEditorComponent(buttons);

        editor.addSaveListener(event -> {
            try {
                SupplierEntity supplierEntity = new SupplierEntity();
                supplierEntity.setSupplierName(event.getItem().getSupplierName());
                supplierEntity.setContract(event.getItem().getContract());
                supplierEntity.setId(event.getItem().getId());
                supplierService.updateSupplier(supplierEntity);
                UI.getCurrent().navigate(StorageSearch.class);
                close();
            }
            catch (Exception ex ) {
                Notification.show("Не могу обновить локацию!" + ex.getMessage(), 5000, Notification.Position.MIDDLE);
                return;
            }
        });

        colSupplierName.setResizable(true);
        colContractNumber.setResizable(true);
    }
    private void updateGrid() {
        dataProvider = new ListDataProvider<>(
                supplierService.getAll());
        grid.setItems(dataProvider);
    }
}
