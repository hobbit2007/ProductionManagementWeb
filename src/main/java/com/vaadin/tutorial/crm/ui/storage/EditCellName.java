package com.vaadin.tutorial.crm.ui.storage;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
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
import com.vaadin.tutorial.crm.entity.storage.CellEntity;
import com.vaadin.tutorial.crm.entity.storage.LocationEntity;
import com.vaadin.tutorial.crm.entity.storage.StorageEntity;
import com.vaadin.tutorial.crm.service.storage.CellService;
import com.vaadin.tutorial.crm.service.storage.LocationService;
import com.vaadin.tutorial.crm.service.storage.StorageService;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;
import com.vaadin.tutorial.crm.ui.layout.StorageLayout;

import java.util.Collection;
import java.util.Collections;
import java.util.WeakHashMap;

/**
 * Класс диалог реализующий обновление имени ячейка
 */
@Route(value = "ctlledit", layout = StorageLayout.class)
@PageTitle("Редактирование имени ячейки | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class EditCellName extends Dialog {
    Grid<CellEntity> grid = new Grid<>();
    Grid.Column<CellEntity> colCellName;
    VerticalLayout vMain = new VerticalLayout();
    HorizontalLayout hMain = new HorizontalLayout();
    ListDataProvider<CellEntity> dataProvider;
    Button cancel = new Button("Закрыть");
    ComboBox<LocationEntity> location = new ComboBox<>("Выберите локацию:");
    ComboBox<StorageEntity> store = new ComboBox<>("Выберите склад:");
    long storeID;
    long locationID = 0;
    private final CellService cellService;
    private final StorageService storageService;
    private final LocationService locationService;

    public EditCellName(CellService cellService, StorageService storageService, LocationService locationService) {
        this.cellService = cellService;
        this.storageService = storageService;
        this.locationService = locationService;

        this.open();
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        setDraggable(true);
        setWidth("755px");
        setHeight("555px");
        grid.setEnabled(false);
        configureGrid();

        Icon icon = new Icon(VaadinIcon.CLOSE);
        cancel.setIcon(icon);
        cancel.getStyle().set("background-color", "#d3b342");
        cancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        store.setEnabled(false);
        location.setWidth("255px");
        location.setRequired(true);
        location.setItems(this.locationService.getAll());
        location.setItemLabelGenerator(LocationEntity::getLocationName);
        location.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                store.setEnabled(true);
                locationID = e.getValue().getId();
                store.setItems(storageService.getAll());
                store.setItemLabelGenerator(StorageEntity::getStorageName);
            }
            else
                store.setEnabled(false);
        });
        store.addValueChangeListener(e -> {
           if (e.getValue() != null) {
               storeID = e.getValue().getId();
               grid.setEnabled(true);
               updateGrid(storeID);
           }
        });

        hMain.add(location, store);
        vMain.add(new AnyComponent().labelTitle("Редактирование имени ячейки"), hMain, grid, cancel);
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

        colCellName = grid.addColumn(cellEntity -> cellEntity.getCellName()).setHeader("Название ячейки");

        Binder<CellEntity> binder = new Binder<>(CellEntity.class);
        Editor<CellEntity> editor = grid.getEditor();
        editor.setBinder(binder);
        editor.setBuffered(true);

        Div validationStatus = new Div();
        validationStatus.setId("validation");

        TextField cellName = new TextField();
        binder.forField(cellName)
                .withValidator(new StringLengthValidator("Название ячейки должно содержать минимум 2 символа", 2, 250))
                .withStatusLabel(validationStatus).bind("cellName");
        colCellName.setEditorComponent(cellName);

        Collection<Button> editButtons = Collections
                .newSetFromMap(new WeakHashMap<>());

        Grid.Column<CellEntity> editorColumn = grid.addComponentColumn(cell -> {
            Icon icon = new Icon(VaadinIcon.EDIT);
            Button edit = new Button("Редактировать");
            edit.setIcon(icon);
            edit.getStyle().set("background-color", "#d3b342");
            edit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            edit.addClickListener(e -> {
                editor.editItem(cell);
                cellName.focus();
            });
            edit.setEnabled(!editor.isOpen());
            editButtons.add(edit);
            return edit;
        }).setFlexGrow(0).setWidth("330px");

        editor.addOpenListener(e -> editButtons.stream()
                .forEach(button -> button.setEnabled(!editor.isOpen())));
        editor.addCloseListener(e -> editButtons.stream()
                .forEach(button -> button.setEnabled(!editor.isOpen())));

        Icon icon = new Icon(VaadinIcon.DISC);
        Button save = new Button("Сохранить", e -> editor.save());
        save.setIcon(icon);
        save.getStyle().set("background-color", "#d3b342");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Icon icon1 = new Icon(VaadinIcon.EDIT);
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
                CellEntity cellEntity = new CellEntity();
                cellEntity.setCellName(event.getItem().getCellName());
                cellEntity.setId(event.getItem().getId());
                cellService.updateCellName(cellEntity);
                UI.getCurrent().navigate(StorageSearch.class);
                close();
            }
            catch (Exception ex ) {
                Notification.show("Не могу обновить имя ячейки!" + ex.getMessage(), 5000, Notification.Position.MIDDLE);
                return;
            }
        });

        colCellName.setResizable(true);
    }
    private void updateGrid(Long storeID) {
        dataProvider = new ListDataProvider<>(
                cellService.getAll(storeID));
        grid.setItems(dataProvider);
    }
}
