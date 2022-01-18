package com.vaadin.tutorial.crm.ui.for1c;

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
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.entity.for1c.For1CSignalListEntity;
import com.vaadin.tutorial.crm.service.for1c.For1CSignalListService;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;
import com.vaadin.tutorial.crm.ui.layout.For1CLayout;
import java.util.Collection;
import java.util.Collections;
import java.util.WeakHashMap;

/**
 * Класс диалог реализующий удаление переменной из списка отправки в 1С
 */
@Route(value = "delvar", layout = For1CLayout.class)
@PageTitle("Удалить переменную | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class DeleteVariable extends Dialog {
    VerticalLayout vMain = new VerticalLayout();
    Grid<For1CSignalListEntity> grid;
    Grid.Column<For1CSignalListEntity> colVarName, colVarDescription;
    private ListDataProvider<For1CSignalListEntity> dataProvider;
    private final For1CSignalListService for1CSignalListService;

    public DeleteVariable(For1CSignalListService for1CSignalListService) {
        this.for1CSignalListService = for1CSignalListService;
        this.open();
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        setDraggable(true);
        setWidth("755px");
        setHeight("555px");

        configureGrid();
        updateGrid();

        vMain.add(new AnyComponent().labelTitle("Удаление переменной"), grid);
        vMain.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        vMain.setSizeFull();

        add(vMain);
    }
    private void configureGrid() {
        grid = new Grid<>();
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS,
                GridVariant.LUMO_ROW_STRIPES);
        grid.setColumnReorderingAllowed(true);
        grid.addClassName("contact-grid");
        grid.setSizeFull();

        colVarName = grid.addColumn(for1CSignalListEntity -> for1CSignalListEntity.getSignalList().getSignalName()).setHeader("Наименование");
        colVarDescription = grid.addColumn(for1CSignalListEntity -> for1CSignalListEntity.getSignalList().getSignalDescription()).setHeader("Описание");

        colVarName.setResizable(true);
        colVarDescription.setResizable(true);

        Binder<For1CSignalListEntity> binder = new Binder<>(For1CSignalListEntity.class);
        Editor<For1CSignalListEntity> editor = grid.getEditor();
        editor.setBinder(binder);
        editor.setBuffered(true);

        Collection<Button> editButtons = Collections
                .newSetFromMap(new WeakHashMap<>());

        Grid.Column<For1CSignalListEntity> editorColumn = grid.addComponentColumn(cell -> {
            Icon icon = new Icon(VaadinIcon.DEL_A);
            Button edit = new Button("Удалить");
            edit.setIcon(icon);
            edit.getStyle().set("background-color", "#d3b342");
            edit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            edit.addClickListener(e -> {
                editor.editItem(cell);
            });
            edit.setEnabled(!editor.isOpen());
            editButtons.add(edit);
            return edit;
        }).setFlexGrow(0).setWidth("330px");

        editor.addOpenListener(e -> editButtons.stream()
                .forEach(button -> button.setEnabled(!editor.isOpen())));
        editor.addCloseListener(e -> editButtons.stream()
                .forEach(button -> button.setEnabled(!editor.isOpen())));

        Icon icon = new Icon(VaadinIcon.DEL_A);
        Button save = new Button("Удалить?", e -> editor.save());
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
                for1CSignalListService.updateDeleteRecord(event.getItem().getId());
                UI.getCurrent().navigate(IntoToDB1C.class);
                close();
            } catch (Exception ex) {
                Notification.show("Не могу удалить переменную!" + ex.getMessage(), 5000, Notification.Position.MIDDLE);
                return;
            }
        });
    }
    private void updateGrid() {
        dataProvider = new ListDataProvider<>(
                for1CSignalListService.getAll());
        grid.setItems(dataProvider);
    }
}
