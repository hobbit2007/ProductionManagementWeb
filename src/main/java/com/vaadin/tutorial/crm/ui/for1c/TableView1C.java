package com.vaadin.tutorial.crm.ui.for1c;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.entity.for1c.For1CEntity;
import com.vaadin.tutorial.crm.entity.for1c.For1CSignalListEntity;
import com.vaadin.tutorial.crm.model.DataFor1C;
import com.vaadin.tutorial.crm.service.for1c.For1CService;
import com.vaadin.tutorial.crm.service.for1c.For1CSignalListService;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;
import com.vaadin.tutorial.crm.ui.layout.For1CLayout;;import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Класс реализующий просмотр значений переменных еще не переданных в 1С
 */
@Route(value = "tableviewone", layout = For1CLayout.class)
@PageTitle("Просмотр переменных для 1С | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class TableView1C extends VerticalLayout {
    VerticalLayout vMain = new VerticalLayout();
    TreeGrid<For1CEntity> grid;
    Grid.Column<For1CEntity> colDesc, colValue, colDate;
    private final For1CSignalListService for1CSignalListService;
    private final For1CService for1CService;

    public TableView1C(For1CSignalListService for1CSignalListService, For1CService for1CService) {
        this.for1CSignalListService = for1CSignalListService;
        this.for1CService = for1CService;
        setSizeFull();
        configureGrid();
        updateList();

        vMain.add(new AnyComponent().labelTitle("Просмотр, еще не переданных в 1С, переменных"), grid);
        vMain.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        vMain.setSizeFull();
        add(vMain);
    }
    private void fillTreeGrid(List<For1CEntity> data) {
        boolean parentFlag = false;//Проверяем пустая родительская группа или нет, если нет, то группу в таблице не показываем
        List<For1CEntity> childData = new ArrayList<>();
        List<For1CSignalListEntity> parentData;
        parentData = for1CSignalListService.getAll();

        int index;

        for (int g = 0; g < parentData.size(); g++) {
            childData.add(new For1CEntity(parentData.get(g).getSignalList().getSignalDescription(), null));
            index = childData.size() - 1;
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getIdSignal() == parentData.get(g).getId()) {
                    childData.add(new For1CEntity(data.get(i).getValue(), data.get(i).getDatetime(), data.get(i).getId(), childData.get(index)));
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
        fillTreeGrid(for1CService.getAll());
    }
    private void configureGrid() {
        grid = new TreeGrid<>();
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS,
                GridVariant.LUMO_ROW_STRIPES);
        grid.setColumnReorderingAllowed(true);
        grid.addClassName("contact-grid");
        grid.setSizeFull();

        colDesc = grid.addHierarchyColumn(for1CEntity -> for1CEntity.getDescription() == null ? "" : for1CEntity.getDescription()).setHeader("Группы переменных");
        colValue = grid.addColumn(for1CEntity -> for1CEntity.getValue()).setHeader("Значение");
        colDate = grid.addColumn(for1CEntity -> for1CEntity.getDatetime()).setHeader("Дата создания");

        colDesc.setResizable(true);
        colValue.setResizable(true);
        colDate.setResizable(true);
    }
    private List<DataFor1C> middleValue() {
        List<DataFor1C> totalList = new ArrayList<>();
        for (int i = 0; i < for1CSignalListService.getAll().size(); i++) {
            float variable = 0;
            int count = 0;

            Date dateCreate = new Date();
            for (int j = 0; j < for1CService.getAll().size(); j++) {
                if (for1CSignalListService.getAll().get(i).getId() == for1CService.getAll().get(j).getIdSignal()) {
                    variable += for1CService.getAll().get(j).getValue();
                    count = count + 1;
                    dateCreate = for1CService.getAll().get(j).getDatetime();
                }
            }
            DataFor1C dataFor1C = new DataFor1C(for1CSignalListService.getAll().get(i).getSignalList().getSignalName(),
                    for1CSignalListService.getAll().get(i).getSignalList().getSignalDescription(),
                    variable / count, dateCreate);
            totalList.add(dataFor1C);
        }
        return totalList;
    }
}
