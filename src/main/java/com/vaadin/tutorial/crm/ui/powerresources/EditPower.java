package com.vaadin.tutorial.crm.ui.powerresources;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.entity.powerresources.PowerResourceDict;
import com.vaadin.tutorial.crm.entity.powerresources.PowerResources;
import com.vaadin.tutorial.crm.service.powerresources.PowerResourceDictService;
import com.vaadin.tutorial.crm.service.powerresources.PowerResourcesService;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;
import com.vaadin.tutorial.crm.ui.layout.UserLayout;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс реализующий обновление введенных показаний по энергоресурсам
 */
@Route(value = "poweredit", layout = UserLayout.class)
@PageTitle("Редактирование показаний | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class EditPower extends VerticalLayout {
    VerticalLayout vMain = new VerticalLayout();
    HorizontalLayout hSort = new HorizontalLayout();
    TreeGrid<PowerResources> grid;
    Grid.Column<PowerResources> colDesc, colValue, colDate, colTime;
    DatePicker dateBegin = new DatePicker("с:");
    DatePicker dateEnd = new DatePicker("по:");
    Button sortButton = new Button("Сортировка");
    Button allViewButton = new Button("Показать все");
    private final PowerResourcesService powerResourcesService;
    private final PowerResourceDictService powerResourceDictService;
    public EditPower(PowerResourcesService powerResourcesService, PowerResourceDictService powerResourceDictService) {
        this.powerResourcesService = powerResourcesService;
        this.powerResourceDictService = powerResourceDictService;

        setSizeFull();
        configureGrid();
        updateList();

        dateBegin.setI18n(new AnyComponent().datePickerRus());
        dateEnd.setI18n(new AnyComponent().datePickerRus());
        //dateEnd.setValue(LocalDate.now());

        hSort.add(new AnyComponent().labelTitle("Сортировка по дате:"), dateBegin, dateEnd, sortButton, allViewButton);
        hSort.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        vMain.add(new AnyComponent().labelTitle("Редактирование показаний"), hSort, grid);
        vMain.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        vMain.setSizeFull();
        add(vMain);

        sortButton.addClickListener(e -> {
            if (!dateBegin.isEmpty() && !dateEnd.isEmpty()) {
                if (Date.valueOf(dateBegin.getValue()).getTime() <= Date.valueOf(dateEnd.getValue()).getTime()) {
                    if (Date.valueOf(dateEnd.getValue()).getTime() <= new java.util.Date().getTime()) {
                        TreeData<PowerResources> treeData = new TreeData<>();
                        TreeDataProvider<PowerResources> dataProvider = new TreeDataProvider<>(treeData);
                        grid.setDataProvider(dataProvider);
                        fillTreeGrid(powerResourcesService.getResourceBySearch(Date.valueOf(dateBegin.getValue().toString()), Date.valueOf(dateEnd.getValue().toString())));
                        dataProvider.refreshAll();
                    }
                    else {
                        Notification.show("Дата окончания не может быть больше текущей!", 3000, Notification.Position.MIDDLE);
                        return;
                    }
                }
                else {
                    Notification.show("Дата начала не может быть больше даты окончания!", 3000, Notification.Position.MIDDLE);
                    return;
                }
            }
            else {
                Notification.show("Не выбраны даты для сортировки!", 3000, Notification.Position.MIDDLE);
                return;
            }
        });
        allViewButton.addClickListener(e -> {
            TreeData<PowerResources> treeData = new TreeData<>();
            TreeDataProvider<PowerResources> dataProvider = new TreeDataProvider<>(treeData);
            grid.setDataProvider(dataProvider);
            fillTreeGrid(powerResourcesService.getAll());
            dataProvider.refreshAll();
        });
    }
    private void fillTreeGrid(List<PowerResources> data) {
        boolean parentFlag = false;//Проверяем пустая родительская группа или нет, если нет, то группу в таблице не показываем
        List<PowerResources> childData = new ArrayList<>();
        List<PowerResourceDict> parentData;
        parentData = powerResourceDictService.getAll();

        int index;

        for (int g = 0; g < parentData.size(); g++) {
            childData.add(new PowerResources(parentData.get(g).getResourceName(), null));
            index = childData.size() - 1;
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getIdPowerResource() == parentData.get(g).getId()) {
                    childData.add(new PowerResources(data.get(i).getValue(), data.get(i).getDateCreate(), data.get(i).getTimeCreate(), childData.get(index)));
                    parentFlag = true;
                }
            }
            if (!parentFlag)
                childData.remove(index);
            else
                parentFlag = false;
        }
        childData.forEach(p -> grid.getTreeData().addItem(p.getParent(), p));
    }
    private void updateList() {
        fillTreeGrid(powerResourcesService.getAll());
    }
    private void configureGrid() {
        grid = new TreeGrid<>();
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS,
                GridVariant.LUMO_ROW_STRIPES);
        grid.setColumnReorderingAllowed(true);
        grid.addClassName("contact-grid");
        grid.setSizeFull();

        colDesc = grid.addHierarchyColumn(powerResources -> powerResources.getDescription() == null ? "" : powerResources.getDescription()).setHeader("Группы энергоресурсов");
        colValue = grid.addColumn(powerResources -> powerResources.getValue() == 0.0 ? "" : powerResources.getValue()).setHeader("Показания");
        colDate = grid.addColumn(powerResources -> powerResources.getDateCreate()).setHeader("Дата снятия");
        colTime = grid.addColumn(powerResources -> powerResources.getTimeCreate()).setHeader("Время снятия");

        colDesc.setResizable(true);
        colValue.setResizable(true);
        colDate.setResizable(true);
        colTime.setResizable(true);
    }
}
