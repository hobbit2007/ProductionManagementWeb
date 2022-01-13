package com.vaadin.tutorial.crm.ui.storage;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.tutorial.crm.entity.storage.MaterialMoveEntity;
import com.vaadin.tutorial.crm.service.storage.MaterialMoveService;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;

/**
 * Класс диалог показывающий историю перемещения объекта хранения между склад/ячейка
 */
public class MoveSCHistory extends Dialog {
    Grid<MaterialMoveEntity> grid;
    Grid.Column<MaterialMoveEntity> colMaterialName, colStorageOld, colStorageNew, colCellOld, colCellNew, colMeas, colExpense, colDateCreate;
    private ListDataProvider<MaterialMoveEntity> dataProvider;
    Button close = new Button("Закрыть");
    VerticalLayout vMain = new VerticalLayout();
    private final MaterialMoveService materialMoveService;
    Long materialID;

    public MoveSCHistory(MaterialMoveService materialMoveService, Long materialID) {
        this.materialMoveService = materialMoveService;
        this.materialID = materialID;
        setSizeFull();
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        setDraggable(true);
        configureGrid();
        updateGrid();

        Icon icon = new Icon(VaadinIcon.CLOSE);
        close.setIcon(icon);
        close.getStyle().set("background-color", "#d3b342");
        close.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        vMain.add(new AnyComponent().labelTitle("История перемещений склад/ячейка"), grid, close);
        vMain.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        vMain.setSizeFull();
        add(vMain);

        close.addClickListener(e -> {
            this.close();
        });
    }
    private void configureGrid() {
        grid = new Grid<>();
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS,
                GridVariant.LUMO_ROW_STRIPES);
        grid.setColumnReorderingAllowed(true);
        grid.addClassName("contact-grid");
        grid.setSizeFull();

        colMaterialName = grid.addColumn(materialMoveEntity -> materialMoveEntity.getMaterial().getMaterialName()).setHeader("Наименование");
        colStorageOld = grid.addColumn(materialMoveEntity -> materialMoveEntity.getStorageOld().getStorageName()).setHeader("Из склада");
        colStorageNew = grid.addColumn(materialMoveEntity -> materialMoveEntity.getStorageNew().getStorageName()).setHeader("Склад новый");
        colCellOld = grid.addColumn(materialMoveEntity -> materialMoveEntity.getCellOld().getCellName()).setHeader("Из ячейки");
        colCellNew = grid.addColumn(materialMoveEntity -> materialMoveEntity.getCellNew().getCellName()).setHeader("Ячейка новая");
        colExpense = grid.addColumn(materialMoveEntity -> materialMoveEntity.getExpense()).setHeader("Количество");
        colMeas = grid.addColumn(materialMoveEntity -> materialMoveEntity.getMaterial().getMeas().getMeasName()).setHeader("Ед. измерения");
        colDateCreate = grid.addColumn(materialMoveEntity -> materialMoveEntity.getDateCreate()).setHeader("Дата перемещения");

        colMaterialName.setResizable(true);
        colStorageOld.setResizable(true);
        colStorageNew.setResizable(true);
        colCellOld.setResizable(true);
        colCellNew.setResizable(true);
        colExpense.setResizable(true);
        colMeas.setResizable(true);
        colDateCreate.setResizable(true);
    }
    private void updateGrid() {
        dataProvider = new ListDataProvider<>(
                materialMoveService.getAllByID(materialID, "перемещение склад/ячейка"));
        grid.setItems(dataProvider);
    }
}
