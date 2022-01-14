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
import com.vaadin.tutorial.crm.entity.storage.StorageEntity;
import com.vaadin.tutorial.crm.service.storage.StorageService;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;
import com.vaadin.tutorial.crm.ui.layout.StorageLayout;

import java.util.Collection;
import java.util.Collections;
import java.util.WeakHashMap;

/**
 * Класс реализующий редактирование имени склада
 */
@Route(value = "storageedit", layout = StorageLayout.class)
@PageTitle("Редактирование имени склада | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class EditStorageName extends Dialog {
    Grid<StorageEntity> grid = new Grid<>();
    Grid.Column<StorageEntity> colStorageName;
    VerticalLayout vMain = new VerticalLayout();
    ListDataProvider<StorageEntity> dataProvider;
    Button cancel = new Button("Закрыть");
    private final StorageService storageService;

    public EditStorageName(StorageService storageService) {
        this.storageService = storageService;
        this.open();
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        setDraggable(true);
        setWidth("755px");
        setHeight("555px");

        Icon icon = new Icon(VaadinIcon.CLOSE);
        cancel.setIcon(icon);
        cancel.getStyle().set("background-color", "#d3b342");
        cancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        configureGrid();
        updateGrid();

        vMain.add(new AnyComponent().labelTitle("Редактирование имени склада"), grid, cancel);
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

        colStorageName = grid.addColumn(storageEntity -> storageEntity.getStorageName()).setHeader("Название склада");

        Binder<StorageEntity> binder = new Binder<>(StorageEntity.class);
        Editor<StorageEntity> editor = grid.getEditor();
        editor.setBinder(binder);
        editor.setBuffered(true);

        Div validationStatus = new Div();
        validationStatus.setId("validation");

        TextField storeName = new TextField();
        binder.forField(storeName)
                .withValidator(new StringLengthValidator("Название склада должно содержать минимум 5 символа", 5, 250))
                .withStatusLabel(validationStatus).bind("storageName");
        colStorageName.setEditorComponent(storeName);

        Collection<Button> editButtons = Collections
                .newSetFromMap(new WeakHashMap<>());

        Grid.Column<StorageEntity> editorColumn = grid.addComponentColumn(store -> {
            Button edit = new Button("Редактировать");
            edit.getStyle().set("background-color", "#d3b342");
            edit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            edit.addClickListener(e -> {
                editor.editItem(store);
                storeName.focus();
            });
            edit.setEnabled(!editor.isOpen());
            editButtons.add(edit);
            return edit;
        }).setFlexGrow(0).setWidth("280px");

        editor.addOpenListener(e -> editButtons.stream()
                .forEach(button -> button.setEnabled(!editor.isOpen())));
        editor.addCloseListener(e -> editButtons.stream()
                .forEach(button -> button.setEnabled(!editor.isOpen())));

        Button save = new Button("Сохранить", e -> editor.save());
        save.getStyle().set("background-color", "#d3b342");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancel = new Button("Отмена", e -> editor.cancel());
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
                StorageEntity storageEntity = new StorageEntity();
                storageEntity.setStorageName(event.getItem().getStorageName());
                storageEntity.setId(event.getItem().getId());
                storageService.updateStorageName(storageEntity);
                UI.getCurrent().navigate(StorageSearch.class);
                close();
            }
            catch (Exception ex ) {
                Notification.show("Не могу обновить имя склада!" + ex.getMessage(), 5000, Notification.Position.MIDDLE);
                return;
            }
        });

        colStorageName.setResizable(true);
    }
    private void updateGrid() {
        dataProvider = new ListDataProvider<>(
                storageService.getAll());
        grid.setItems(dataProvider);
    }
}
