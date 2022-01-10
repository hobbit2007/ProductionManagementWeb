package com.vaadin.tutorial.crm.ui.storage;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.tutorial.crm.entity.storage.StorageComingEntity;
import com.vaadin.tutorial.crm.service.storage.StorageComingService;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;

/**
 * Класс диалог реализующий визуальное отображение истории приходов
 */
public class PrihodHistoryDialog extends Dialog {
    Grid<StorageComingEntity> grid;
    Grid.Column<StorageComingEntity> colMaterialName, colPrihodOld, colPrihodNew, colBalanceOld, colBalanceNew, colMeas, colDateCreate;
    private ListDataProvider<StorageComingEntity> dataProvider;
    Button close = new Button("Закрыть");
    VerticalLayout vMain = new VerticalLayout();
    private final StorageComingService storageComingService;

    public PrihodHistoryDialog(StorageComingService storageComingService) {
        this.storageComingService = storageComingService;
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        setDraggable(true);
        configureGrid();
        updateGrid();

        vMain.add(new AnyComponent().labelTitle("История приход"), grid, close);
        vMain.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
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

        colMaterialName = grid.addColumn(storageComingEntity -> storageComingEntity.getMaterialInfo().getMaterialName()).setHeader("Наименование");
        colPrihodOld = grid.addColumn(storageComingEntity -> storageComingEntity.getQtyOldCome()).setHeader("Приход");
        colPrihodNew = grid.addColumn(storageComingEntity -> storageComingEntity.getQtyCome()).setHeader("Приход новый");
        colBalanceOld = grid.addColumn(storageComingEntity -> storageComingEntity.getBalanceOld()).setHeader("Баланс");
        colBalanceNew = grid.addColumn(storageComingEntity -> storageComingEntity.getBalanceNew()).setHeader("Баланс новый");
        colMeas = grid.addColumn(storageComingEntity -> storageComingEntity.getMeas().getMeasName()).setHeader("Ед. измерения");
        colDateCreate = grid.addColumn(storageComingEntity -> storageComingEntity.getDateCreate()).setHeader("Дата изменения");

        colMaterialName.setResizable(true);
        colPrihodOld.setResizable(true);
        colPrihodNew.setResizable(true);
        colBalanceOld.setResizable(true);
        colBalanceNew.setResizable(true);
        colMeas.setResizable(true);
        colDateCreate.setResizable(true);
    }
    private void updateGrid() {
        dataProvider = new ListDataProvider<>(
                storageComingService.getAll());
        grid.setItems(dataProvider);
    }
}
