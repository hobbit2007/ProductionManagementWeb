package com.vaadin.tutorial.crm.ui.for1c;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.entity.for1c.For1CSignalListEntity;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcControllers;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.SignalList;
import com.vaadin.tutorial.crm.security.SecurityUtils;
import com.vaadin.tutorial.crm.service.for1c.For1CSignalListService;
import com.vaadin.tutorial.crm.service.plccontrollersservice.PlcControllersService;
import com.vaadin.tutorial.crm.service.plccontrollersservice.SignalListService;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;
import com.vaadin.tutorial.crm.ui.layout.For1CLayout;
import java.util.Date;

/**
 * Класс диалог реализующий добавление переменной для передачи в 1С
 */
@Route(value = "addvar", layout = For1CLayout.class)
@PageTitle("Добавить новую переменную | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class AddVariable extends Dialog {
    VerticalLayout vMain = new VerticalLayout();
    HorizontalLayout hMain = new HorizontalLayout();
    HorizontalLayout hButton = new HorizontalLayout();
    ComboBox<PlcControllers> controller = new ComboBox<>("Выберите контроллер:");
    ComboBox<SignalList> variable = new ComboBox<>("Выберите переменную:");
    Grid<For1CSignalListEntity> grid;
    Grid.Column<For1CSignalListEntity> colVarName, colVarDescription;
    Button save = new Button("Добавить");
    Button cancel = new Button("Отмена");
    private ListDataProvider<For1CSignalListEntity> dataProvider;
    long controllerID;
    long variableID = 0;
    private final For1CSignalListService for1CSignalListService;
    private final PlcControllersService plcControllersService;
    private final SignalListService signalListService;

    public AddVariable(For1CSignalListService for1CSignalListService, PlcControllersService plcControllersService,
                       SignalListService signalListService) {
        this.for1CSignalListService = for1CSignalListService;
        this.plcControllersService = plcControllersService;
        this.signalListService = signalListService;
        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);
        setDraggable(true);
        setSizeFull();
        setWidth("755px");
        setHeight("599px");
        this.open();

        configureGrid();
        updateGrid();

        Icon icon = new Icon(VaadinIcon.DISC);
        save.setIcon(icon);
        save.getStyle().set("background-color", "#d3b342");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Icon icon1 = new Icon(VaadinIcon.CLOSE);
        cancel.setIcon(icon1);
        cancel.getStyle().set("background-color", "#d3b342");
        cancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        variable.setEnabled(false);
        variable.setRequired(true);

        controller.setItems(plcControllersService.getAll());
        controller.setItemLabelGenerator(PlcControllers::getControllerName);

        controller.addValueChangeListener(e -> {
           if (e.getValue() != null) {
               controllerID = e.getValue().getId();

               variable.setEnabled(true);
               variable.setItems(signalListService.findSignalList(controllerID));
               variable.setItemLabelGenerator(SignalList::getSignalName);
           }
        });
        variable.addValueChangeListener(e -> {
           if (e.getValue() != null)
               variableID = e.getValue().getId();
        });

        hMain.add(controller, variable);
        hMain.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);

        hButton.add(save, cancel);

        vMain.add(new AnyComponent().labelTitle("Добавить новую переменную"), hMain,
                new AnyComponent().labelTitle("Существующие переменные", "#d3b342", "13pt"), grid, hButton);
        vMain.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        vMain.setSizeFull();

        add(vMain);

        cancel.addClickListener(e -> close());
        save.addClickListener(e -> saveClick());
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
    }
    private void updateGrid() {
        dataProvider = new ListDataProvider<>(
            for1CSignalListService.getAll());
        grid.setItems(dataProvider);
    }
    private void saveClick() {
        if (variableID > 0) {
            if (for1CSignalListService.getAllByID(variableID).size() == 0) {
                For1CSignalListEntity for1CSignalListEntity = new For1CSignalListEntity();
                for1CSignalListEntity.setIdSignalName(variableID);
                for1CSignalListEntity.setIdUser(SecurityUtils.getAuthentication().getDetails().getId());
                for1CSignalListEntity.setDateCreate(new Date());
                for1CSignalListEntity.setDelete(0);

                try {
                    for1CSignalListService.saveAll(for1CSignalListEntity);
                    close();
                    UI.getCurrent().navigate(IntoToDB1C.class);
                }
                catch (Exception ex) {
                    Notification.show("Не могу выполнить запись, переменная не существует!" + ex.getMessage(), 5000, Notification.Position.MIDDLE);
                    return;
                }
            }
            else {
                Notification.show("Такая переменная уже была добавлена!", 3000, Notification.Position.MIDDLE);
                return;
            }
        }
        else {
            Notification.show("Не выбрана переменная!", 3000, Notification.Position.MIDDLE);
            return;
        }
    }
}
