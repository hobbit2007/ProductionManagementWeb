package com.vaadin.tutorial.crm.ui.plccontrollersui;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.OLDPlcWashing;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;
import com.vaadin.tutorial.crm.ui.layout.MainLayout;

/**
 * Класс содержащий реализацию визуальных элементов и логики для plc контроллера мойка
 * в реальном времени
 */
@Route(value = "plcwashingrt", layout = MainLayout.class)
@PageTitle("Контроллер Мойка(реальное время) | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class PlcWashingController extends VerticalLayout {
    private TreeGrid<OLDPlcWashing> grid;
    private Grid.Column<OLDPlcWashing> colSignalName, colSignalDesc;
    private VerticalLayout vContent = new VerticalLayout();
    private AnyComponent anyComponent = new AnyComponent();

    public PlcWashingController() {
        addClassName("list-view");
        setSizeFull();

        vContent.add(anyComponent.labelTitle("Контроллер Мойки(реальное время - 1секунда)"), grid);
        add(vContent);
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
