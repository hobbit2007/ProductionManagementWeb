package com.vaadin.tutorial.crm.ui.plccontrollersui;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.OLDPlcWashing;

/**
 * Класс содержащий реализацию визуальных элементов и логики для plc контроллера мойка
 * в реальном времени
 */
public class PlcWashingController extends VerticalLayout {
    private TreeGrid<OLDPlcWashing> grid;
    private Grid.Column<OLDPlcWashing> colSignalName, colSignalDesc;
    private VerticalLayout vContent = new VerticalLayout();

    public PlcWashingController() {
        addClassName("list-view");
        setSizeFull();
    }

    private void configureGrid() {
        grid = new TreeGrid<>();
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS,
                GridVariant.LUMO_ROW_STRIPES);
        grid.setColumnReorderingAllowed(true);
        grid.addClassName("contact-grid");
        grid.setSizeFull();

        //colSignalName = grid.addHierarchyColumn(plcWashing -> plcWashing.)
    }
}
