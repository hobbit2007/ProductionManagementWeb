package com.vaadin.tutorial.crm.ui.storage;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.tutorial.crm.entity.storage.MaterialMoveEntity;
import com.vaadin.tutorial.crm.service.storage.MaterialMoveService;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;

/**
 * Класс диалог отображающий историю движения объектов хранения при внутреннем перемещении
 */
public class MoveInsideHistory extends Dialog {
    Grid<MaterialMoveEntity> grid;
    Grid.Column<MaterialMoveEntity> colMaterialName, colStorageNew, colCellNew, colMeas, colExpense, colDescription, colShop, colDepartment,
            colUser, colDateCreate;
    private ListDataProvider<MaterialMoveEntity> dataProvider;
    Button close = new Button("Закрыть");
    VerticalLayout vMain = new VerticalLayout();
    private final MaterialMoveService materialMoveService;
    Long materialID;

    public MoveInsideHistory(MaterialMoveService materialMoveService, Long materialID) {
        this.materialMoveService = materialMoveService;
        this.materialID = materialID;
        setSizeFull();
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        setDraggable(true);
        configureGrid();
        updateGrid();

        close.getStyle().set("background-color", "#d3b342");
        close.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        vMain.add(new AnyComponent().labelTitle("История внутреннего перемещения"), grid, close);
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
        colStorageNew = grid.addColumn(materialMoveEntity -> materialMoveEntity.getStorageNew().getStorageName()).setHeader("Склад");
        colCellNew = grid.addColumn(materialMoveEntity -> materialMoveEntity.getCellNew().getCellName()).setHeader("Ячейка");
        colExpense = grid.addColumn(materialMoveEntity -> materialMoveEntity.getExpense()).setHeader("Количество");
        colMeas = grid.addColumn(materialMoveEntity -> materialMoveEntity.getMaterial().getMeas().getMeasName()).setHeader("Ед. измерения");
        colDescription = grid.addColumn(materialMoveEntity -> materialMoveEntity.getDescription()).setHeader("Примечание");
        colShop = grid.addColumn(materialMoveEntity -> materialMoveEntity.getShop().getShopName()).setHeader("Подразделение");
        colDepartment = grid.addColumn(materialMoveEntity -> materialMoveEntity.getDepartment().getDepartmentName()).setHeader("Отдел");
        colUser = grid.addColumn(materialMoveEntity -> materialMoveEntity.getUser().getFio()).setHeader("ФИО");
        colDateCreate = grid.addColumn(materialMoveEntity -> materialMoveEntity.getDateCreate()).setHeader("Дата перемещения");

        //Группировка колонок
        HeaderRow headerRow = grid.prependHeaderRow();
        headerRow.join(colShop, colDepartment, colUser).setText("Куда перемещен");

        colMaterialName.setResizable(true);
        colStorageNew.setResizable(true);
        colCellNew.setResizable(true);
        colExpense.setResizable(true);
        colMeas.setResizable(true);
        colShop.setResizable(true);
        colDepartment.setResizable(true);
        colUser.setResizable(true);
        colDateCreate.setResizable(true);
    }
    private void updateGrid() {
        dataProvider = new ListDataProvider<>(
                materialMoveService.getAllByID(materialID, "внутреннее перемещение"));
        grid.setItems(dataProvider);
    }
}
