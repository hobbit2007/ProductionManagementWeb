package com.vaadin.tutorial.crm.ui.storage;

import com.helger.commons.codec.ICodec;
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
import com.vaadin.tutorial.crm.entity.storage.ChangePriceEntity;
import com.vaadin.tutorial.crm.service.storage.ChangePriceService;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;

/**
 * Класс диалог реализующий визуализацию истории изменения цены объекта хранения
 */
public class ChangePriceHistory extends Dialog {
    Grid<ChangePriceEntity> grid;
    Grid.Column<ChangePriceEntity> colMaterialName, colCostPriceOld, colCostPriceNew, colMarketPriceOld, colMarketPriceNew,
            colDiffPriceOld, colDiffPriceNew, colDateCreate;
    private ListDataProvider<ChangePriceEntity> dataProvider;
    Button close = new Button("Закрыть");
    VerticalLayout vMain = new VerticalLayout();
    private final ChangePriceService changePriceService;
    Long materialID;

    public ChangePriceHistory(ChangePriceService changePriceService, Long materialID) {
        this.changePriceService = changePriceService;
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

        vMain.add(new AnyComponent().labelTitle("История изменения цены"), grid, close);
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

        colMaterialName = grid.addColumn(changePriceEntity -> changePriceEntity.getMaterial().getMaterialName()).setHeader("Наименование");
        colCostPriceOld = grid.addColumn(changePriceEntity -> changePriceEntity.getCostPriceOld()).setHeader("Себестоимость");
        colCostPriceNew = grid.addColumn(changePriceEntity -> changePriceEntity.getCostPriceNew()).setHeader("Себестоимость новая");
        colMarketPriceOld = grid.addColumn(changePriceEntity -> changePriceEntity.getMarketPriceOld()).setHeader("Цена продажи");
        colMarketPriceNew = grid.addColumn(changePriceEntity -> changePriceEntity.getMarketPriceNew()).setHeader("Цена продажи новая");
        colDiffPriceOld = grid.addColumn(changePriceEntity -> changePriceEntity.getDiffPriceOld()).setHeader("Разница цен");
        colDiffPriceNew = grid.addColumn(changePriceEntity -> changePriceEntity.getDiffPriceNew()).setHeader("Разница цен новая");
        colDateCreate = grid.addColumn(changePriceEntity -> changePriceEntity.getDateCreate()).setHeader("Дата изменения");

        colMaterialName.setResizable(true);
        colCostPriceOld.setResizable(true);
        colCostPriceNew.setResizable(true);
        colMarketPriceOld.setResizable(true);
        colMarketPriceNew.setResizable(true);
        colDiffPriceOld.setResizable(true);
        colDiffPriceNew.setResizable(true);
        colDateCreate.setResizable(true);
    }
    private void updateGrid() {
        dataProvider = new ListDataProvider<>(
                changePriceService.getAllByIdMaterial(materialID));
        grid.setItems(dataProvider);
    }
}
