package com.vaadin.tutorial.crm.ui.plccontrollersui;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.SortOrderProvider;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcControllers;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.SignalList;
import com.vaadin.tutorial.crm.service.HistoryService;
import com.vaadin.tutorial.crm.service.plccontrollersservice.PlcControllersService;
import com.vaadin.tutorial.crm.service.plccontrollersservice.SignalGroupsService;
import com.vaadin.tutorial.crm.service.plccontrollersservice.SignalListService;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;
import com.vaadin.tutorial.crm.ui.layout.PlcSignalLayout;

/**
 * Класс позволяющий редактировать переменные ПЛК контроллеров
 */
@Route(value = "editsignal", layout = PlcSignalLayout.class)
@PageTitle("Просмотр/Редактирование переменной ПЛК | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class EditSignal extends VerticalLayout {
    private Grid<SignalList> grid;
    private Grid.Column<SignalList> colSignalName, colSignalDescription, colDbValue, colPosition, colOffset, colGroup;
    private ComboBox<PlcControllers> controllersComboBox = new ComboBox<>("Выберите контроллер:");
    private VerticalLayout vMain = new VerticalLayout();
    private final SignalListService signalListService;
    private ListDataProvider<SignalList> dataProvider;
    private Div content;
    private FormSignalDetail formSignalDetail;
    private long controllerID;

    public EditSignal(PlcControllersService plcControllersService, SignalGroupsService signalGroupsService, SignalListService signalListService,
                      HistoryService historyService) {
        this.signalListService = signalListService;
        addClassName("list-view");
        setSizeFull();

        configureGrid();

        formSignalDetail = new FormSignalDetail(signalListService, signalGroupsService, historyService);
        formSignalDetail.addListener(FormSignalDetail.ContactFormEvent.CloseEvent.class, e -> btnClose());
        content = new Div(grid, formSignalDetail);
        content.addClassName("content");
        content.setSizeFull();

        controllersComboBox.setItems(plcControllersService.getAll());
        controllersComboBox.setItemLabelGenerator(PlcControllers::getControllerName);
        controllersComboBox.addValueChangeListener(e -> {
           if (e.getValue() != null) {
               controllerID = e.getValue().getId();
               updateGrid();
           }
        });

        vMain.add(new AnyComponent().labelTitle("Просмотр/Редактирование переменной ПЛК"), controllersComboBox, content);
        vMain.setSizeFull();
        vMain.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(vMain);
        close();
    }
    private void configureGrid() {
        grid = new Grid<>();
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS,
                GridVariant.LUMO_ROW_STRIPES);
        grid.setColumnReorderingAllowed(true);
        grid.addClassName("contact-grid");
        grid.setSizeFull();

        colSignalName = grid.addColumn(signalList -> signalList.getSignalName()).setHeader("Имя переменной").setSortable(true);
        colSignalDescription = grid.addColumn(signalList -> signalList.getSignalDescription()).setHeader("Описание переменной");
        colDbValue = grid.addColumn(signalList -> signalList.getDbValue()).setHeader("База");
        colPosition = grid.addColumn(signalList -> signalList.getPosition()).setHeader("Позиция");
        colOffset = grid.addColumn(signalList -> signalList.getFOffset()).setHeader("Длина, байты");
        colGroup = grid.addColumn(signalList -> signalList.getGroupName().getShortSignalName()).setHeader("Группа переменной");

        colSignalName.setResizable(true);
        colSignalDescription.setResizable(true);
        colDbValue.setResizable(true);
        colPosition.setResizable(true);
        colOffset.setResizable(true);
        colGroup.setResizable(true);

        grid.asSingleSelect().addValueChangeListener(e -> {
            if (e.getValue() != null) {
                editForm(e.getValue());
            }
            else
                close();
        });
    }
    private void updateGrid() {
        dataProvider = new ListDataProvider<>(
          signalListService.findSignalList(controllerID));
        grid.setItems(dataProvider);
    }
    private void editForm(SignalList signalList) {
        if (signalList == null)
            close();
        formSignalDetail.setSignalInfo(signalList);
        formSignalDetail.setVisible(true);
        addClassName("editing");
    }
    private void close() {
        formSignalDetail.setSignalInfo(null);
        formSignalDetail.setVisible(false);
    }
    private void btnClose() {
        formSignalDetail.setSignalInfo(null);
        formSignalDetail.setVisible(false);
        updateGrid();
    }
}
