package com.vaadin.tutorial.crm.ui.powerresources;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.entity.powerresources.PowerResourceDict;
import com.vaadin.tutorial.crm.entity.powerresources.PowerResources;
import com.vaadin.tutorial.crm.service.powerresources.PowerResourceDictService;
import com.vaadin.tutorial.crm.service.powerresources.PowerResourcesService;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;
import com.vaadin.tutorial.crm.ui.layout.PowerLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс реализующий просмотр показаний энергоресурсов в табличном варианте и
 * возможность сортировки по дате
 */
@Route(value = "tableview", layout = PowerLayout.class)
@PageTitle("Просмотр показаний энергоресурсов | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class TableView extends VerticalLayout {
    VerticalLayout vMain = new VerticalLayout();
    TreeGrid<PowerResources> grid;
    Grid.Column<PowerResources> colDesc, colValue, colDate;
    private final PowerResourcesService powerResourcesService;
    private final PowerResourceDictService powerResourceDictService;
    public TableView(PowerResourcesService powerResourcesService, PowerResourceDictService powerResourceDictService) {
        this.powerResourcesService = powerResourcesService;
        this.powerResourceDictService = powerResourceDictService;
        setSizeFull();
        configureGrid();
        updateList();
        vMain.add(new AnyComponent().labelTitle("Таблица показаний"), grid);
        vMain.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(vMain);
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
                    childData.add(new PowerResources(data.get(i).getValue(), data.get(i).getDateCreate(), childData.get(index)));
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

        colDesc.setResizable(true);
        colValue.setResizable(true);
        colDate.setResizable(true);
    }
}
