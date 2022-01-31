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
import com.vaadin.tutorial.crm.entity.storage.LocationEntity;
import com.vaadin.tutorial.crm.service.storage.LocationService;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;
import com.vaadin.tutorial.crm.ui.layout.StorageLayout;

import java.util.Collection;
import java.util.Collections;
import java.util.WeakHashMap;

/**
 * Класс диалог реализующий редактирование имени и описания локации
 */
@Route(value = "locationedit", layout = StorageLayout.class)
@PageTitle("Редактировать локацию | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class EditorLocationName extends Dialog {
    Grid<LocationEntity> grid = new Grid<>();
    Grid.Column<LocationEntity> colLocationName, colDescriptionName;
    VerticalLayout vMain = new VerticalLayout();
    ListDataProvider<LocationEntity> dataProvider;
    Button cancel = new Button("Закрыть");
    private final LocationService locationService;

    public EditorLocationName(LocationService locationService) {
        this.locationService = locationService;
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

        vMain.add(new AnyComponent().labelTitle("Редактировать локацию"), grid, cancel);
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

        colLocationName = grid.addColumn(locationEntity -> locationEntity.getLocationName()).setHeader("Название локации");
        colDescriptionName = grid.addColumn(locationEntity -> locationEntity.getLocationDescription()).setHeader("Описание локации");

        Binder<LocationEntity> binder = new Binder<>(LocationEntity.class);
        Editor<LocationEntity> editor = grid.getEditor();
        editor.setBinder(binder);
        editor.setBuffered(true);

        Div validationStatus = new Div();
        validationStatus.setId("validation");

        TextField locationName = new TextField();
        binder.forField(locationName)
                .withValidator(new StringLengthValidator("Название локации должно содержать минимум 1 символов", 1, 250))
                .withStatusLabel(validationStatus).bind("locationName");
        colLocationName.setEditorComponent(locationName);
        TextField descriptionName = new TextField();
        binder.forField(descriptionName)
                .withValidator(new StringLengthValidator("Описание локации должно содержать минимум 5 символов", 5, 250))
                .withStatusLabel(validationStatus).bind("locationDescription");
        colDescriptionName.setEditorComponent(descriptionName);

        Collection<Button> editButtons = Collections
                .newSetFromMap(new WeakHashMap<>());

        Grid.Column<LocationEntity> editorColumn = grid.addComponentColumn(location -> {
            Icon icon = new Icon(VaadinIcon.EDIT);
            Button edit = new Button("Редактировать");
            edit.setIcon(icon);
            edit.getStyle().set("background-color", "#d3b342");
            edit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            edit.addClickListener(e -> {
                editor.editItem(location);
                locationName.focus();
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
                LocationEntity locationEntity = new LocationEntity();
                locationEntity.setLocationName(event.getItem().getLocationName());
                locationEntity.setLocationDescription(event.getItem().getLocationDescription());
                locationEntity.setId(event.getItem().getId());
                locationService.updateLocation(locationEntity);
                UI.getCurrent().navigate(StorageSearch.class);
                close();
            }
            catch (Exception ex ) {
                Notification.show("Не могу обновить локацию!" + ex.getMessage(), 5000, Notification.Position.MIDDLE);
                return;
            }
        });

        colLocationName.setResizable(true);
        colDescriptionName.setResizable(true);
    }
    private void updateGrid() {
        dataProvider = new ListDataProvider<>(
                locationService.getAll());
        grid.setItems(dataProvider);
    }
}
