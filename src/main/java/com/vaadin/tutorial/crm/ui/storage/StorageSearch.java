package com.vaadin.tutorial.crm.ui.storage;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.entity.storage.CellEntity;
import com.vaadin.tutorial.crm.entity.storage.MaterialInfoEntity;
import com.vaadin.tutorial.crm.entity.storage.StorageEntity;
import com.vaadin.tutorial.crm.service.storage.CellService;
import com.vaadin.tutorial.crm.service.storage.MaterialInfoService;
import com.vaadin.tutorial.crm.service.storage.StorageService;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;
import com.vaadin.tutorial.crm.ui.layout.StorageLayout;

/**
 * Класс реализующий поиск по складу
 */
@Route(value = "storagesearch", layout = StorageLayout.class)
@PageTitle("Поиск по складу | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class StorageSearch extends VerticalLayout {
    VerticalLayout vMain = new VerticalLayout();
    VerticalLayout vSearch = new VerticalLayout();
    HorizontalLayout hSearch = new HorizontalLayout();
    HorizontalLayout hSelect = new HorizontalLayout();
    private TextField articleNumber;
    private Button btnArticleSearch, btnMaterialSearch;
    private ComboBox<MaterialInfoEntity> materialName;
    private ComboBox<StorageEntity> storageSelect;
    private ComboBox<CellEntity> cellSelect;
    private Grid<MaterialInfoEntity> grid;
    private Grid.Column<MaterialInfoEntity> colMaterialName, colStorage, colCell, colArticle, colQuantity,
            colExpense, colBalance, colMeas;
    private final MaterialInfoService materialInfoService;
    private final StorageService storageService;
    private final CellService cellService;
    private ListDataProvider<MaterialInfoEntity> dataProvider;

    public StorageSearch(MaterialInfoService materialInfoService, StorageService storageService, CellService cellService) {
        this.materialInfoService = materialInfoService;
        this.storageService = storageService;
        this.cellService = cellService;
        addClassName("list-view");
        setSizeFull();

        vMain.add(new AnyComponent().labelTitle("Поиск по складу"));
        vMain.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        articleNumber = new TextField();
        articleNumber.setLabel("Поиск по артикулу:");
        articleNumber.setEnabled(false);

        btnArticleSearch = new Button("Поиск");
        btnArticleSearch.getStyle().set("background-color", "#d3b342");
        btnArticleSearch.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnArticleSearch.setEnabled(false);

        materialName = new ComboBox<>();
        materialName.setLabel("Поиск по материалу:");
        materialName.setWidth("399px");
        materialName.setItems(materialInfoService.getAll());
        materialName.setItemLabelGenerator(MaterialInfoEntity::getMaterialName);
        materialName.setEnabled(false);

        storageSelect = new ComboBox<>();
        storageSelect.setLabel("Выберите склад:");
        storageSelect.setWidth("270px");
        storageSelect.setItems(storageService.getAll());
        storageSelect.setItemLabelGenerator(StorageEntity::getStorageName);

        cellSelect = new ComboBox<>();
        cellSelect.setLabel("Выберите ячейку:");
        cellSelect.setWidth("135px");
        cellSelect.setItems(cellService.getAll());
        cellSelect.setItemLabelGenerator(CellEntity::getCellName);
        cellSelect.setEnabled(false);

        btnMaterialSearch = new Button("Поиск");
        btnMaterialSearch.getStyle().set("background-color", "#d3b342");
        btnMaterialSearch.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnMaterialSearch.setEnabled(false);

        configureGrid();
        updateGrid();

        hSearch.add(articleNumber, btnArticleSearch, materialName, btnMaterialSearch);
        hSearch.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        hSelect.add(storageSelect, cellSelect);

        vSearch.add(hSelect, hSearch);
        vSearch.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        vSearch.setPadding(false);
        vMain.setPadding(false);

        add(vMain, vSearch, grid);

        storageSelect.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                articleNumber.setEnabled(true);
                btnArticleSearch.setEnabled(true);
                materialName.setEnabled(true);
                cellSelect.setEnabled(true);
                btnMaterialSearch.setEnabled(true);
            }
        });
    }
    private void configureGrid() {
        grid = new Grid<>();
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS,
                GridVariant.LUMO_ROW_STRIPES);
        grid.setColumnReorderingAllowed(true);
        grid.addClassName("contact-grid");
        grid.setSizeFull();

        colMaterialName = grid.addColumn(materialInfoEntity -> materialInfoEntity.getMaterialName()).setHeader("Наименование");
        colStorage = grid.addColumn(materialInfoEntity -> materialInfoEntity.getStorage().getStorageName()).setHeader("Склад");
        colCell = grid.addColumn(materialInfoEntity -> materialInfoEntity.getCell().getCellName()).setHeader("Ячейка");
        colArticle = grid.addColumn(materialInfoEntity -> materialInfoEntity.getArticle()).setHeader("Артикул");
        colMeas = grid.addColumn(materialInfoEntity -> materialInfoEntity.getMeas().getMeasName()).setHeader("Ед. изм.");
        colQuantity = grid.addColumn(materialInfoEntity -> materialInfoEntity.getQuantity()).setHeader("Приход");
        colExpense = grid.addColumn(materialInfoEntity -> materialInfoEntity.getExpense()).setHeader("Расход");
        colBalance = grid.addColumn(materialInfoEntity -> materialInfoEntity.getBalance()).setHeader("Остаток");

        colMaterialName.setResizable(true);
        colStorage.setResizable(true);
        colCell.setResizable(true);
        colArticle.setResizable(true);
        colMeas.setResizable(true);
        colQuantity.setResizable(true);
        colExpense.setResizable(true);
        colBalance.setResizable(true);
    }
    private void updateGrid() {
        dataProvider = new ListDataProvider<>(
                materialInfoService.getAll());
        grid.setItems(dataProvider);
    }
}
