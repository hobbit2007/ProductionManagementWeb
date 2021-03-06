package com.vaadin.tutorial.crm.ui.storage;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.entity.storage.CellEntity;
import com.vaadin.tutorial.crm.entity.storage.LocationEntity;
import com.vaadin.tutorial.crm.entity.storage.MaterialInfoEntity;
import com.vaadin.tutorial.crm.entity.storage.StorageEntity;
import com.vaadin.tutorial.crm.service.DepartmentService;
import com.vaadin.tutorial.crm.service.ShopService;
import com.vaadin.tutorial.crm.service.UserService;
import com.vaadin.tutorial.crm.service.storage.*;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;
import com.vaadin.tutorial.crm.ui.layout.StorageLayout;

/**
 * Класс реализующий поиск по складу
 */
@Route(value = "storagesearch", layout = StorageLayout.class)
@PageTitle("Поиск по складу | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class StorageSearch extends Scroller {
    VerticalLayout vMain = new VerticalLayout();
    VerticalLayout vSearch = new VerticalLayout();
    HorizontalLayout hSearch = new HorizontalLayout();
    HorizontalLayout hSelect = new HorizontalLayout();
    private TextField articleNumber;
    private Button btnArticleSearch, btnMaterialSearch;
    private ComboBox<MaterialInfoEntity> materialName;
    private ComboBox<LocationEntity> locationSelect;
    private ComboBox<StorageEntity> storageSelect;
    private ComboBox<CellEntity> cellSelect;
    private Grid<MaterialInfoEntity> grid;
    private Grid.Column<MaterialInfoEntity> colMaterialName, colDescription, colSupplier, colStorage, colCell, colArticle, colQuantity,
            colExpense, colBalance, colMeas;
    private final MaterialInfoService materialInfoService;
    private final StorageService storageService;
    private final CellService cellService;
    private final StorageComingService storageComingService;
    private final MaterialMoveService materialMoveService;
    private final ShopService shopService;
    private final DepartmentService departmentService;
    private final UserService userService;
    private final ChangePriceService changePriceService;
    private final LocationService locationService;
    private ListDataProvider<MaterialInfoEntity> dataProvider;
    Div content;
    FormMaterialDetail formMaterialDetail;
    long storageID = 0;
    long cellID = 0;
    long locationID = 0;

    public StorageSearch(MaterialInfoService materialInfoService, StorageService storageService, CellService cellService,
                         StorageComingService storageComingService, MaterialMoveService materialMoveService, ShopService shopService,
                         DepartmentService departmentService, UserService userService, ChangePriceService changePriceService,
                         LocationService locationService) {
        this.materialInfoService = materialInfoService;
        this.storageService = storageService;
        this.cellService = cellService;
        this.storageComingService = storageComingService;
        this.materialMoveService = materialMoveService;
        this.shopService = shopService;
        this.departmentService = departmentService;
        this.userService = userService;
        this.changePriceService = changePriceService;
        this.locationService = locationService;
        addClassName("list-view");
        setSizeFull();

        configureGrid();

        vMain.add(new AnyComponent().labelTitle("Поиск по складу"));
        vMain.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        articleNumber = new TextField();
        articleNumber.setLabel("Поиск по артикулу:");
        articleNumber.setEnabled(false);

        Icon icon = new Icon(VaadinIcon.SEARCH);
        btnArticleSearch = new Button("Поиск");
        btnArticleSearch.setIcon(icon);
        btnArticleSearch.getStyle().set("background-color", "#d3b342");
        btnArticleSearch.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnArticleSearch.setEnabled(false);

        materialName = new ComboBox<>();
        materialName.setLabel("Поиск по объекту хранения:");
        materialName.setWidth("299px");
        materialName.setEnabled(false);

        locationSelect = new ComboBox<>();
        locationSelect.setLabel("Выберите локацию:");
        locationSelect.setWidth("270px");
        locationSelect.setEnabled(false);

        storageSelect = new ComboBox<>();
        storageSelect.setLabel("Выберите склад:");
        storageSelect.setWidth("270px");
        storageSelect.setEnabled(true);
        storageSelect.setItems(storageService.getAll());
        storageSelect.setItemLabelGenerator(StorageEntity::getStorageName);
        locationSelect.addValueChangeListener(e -> {
        if (e.getValue() != null) {
            locationID = e.getValue().getId();
            cellSelect.setEnabled(true);
            cellSelect.setEnabled(true);
            cellSelect.setItems(cellService.getAll(storageID, locationID));
            cellSelect.setItemLabelGenerator(CellEntity::getCellName);
            updateGridStoreLocation();
        }
        else
            cellSelect.setEnabled(false);
        });



        cellSelect = new ComboBox<>();
        cellSelect.setLabel("Выберите ячейку:");
        cellSelect.setWidth("135px");
        cellSelect.setEnabled(false);

        Icon icon1 = new Icon(VaadinIcon.SEARCH);
        btnMaterialSearch = new Button("Поиск");
        btnMaterialSearch.setIcon(icon1);
        btnMaterialSearch.getStyle().set("background-color", "#d3b342");
        btnMaterialSearch.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnMaterialSearch.setEnabled(false);

        formMaterialDetail = new FormMaterialDetail(materialInfoService, storageComingService, storageService, cellService, materialMoveService,
                shopService, departmentService, userService, changePriceService, locationService);
        formMaterialDetail.addListener(FormMaterialDetail.ContactFormEvent.CloseEvent.class, e -> btnClose());

        content = new Div(grid, formMaterialDetail);
        content.addClassName("content");
        content.setSizeFull();

        hSearch.add(articleNumber, btnArticleSearch, materialName, btnMaterialSearch);
        hSearch.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);

        hSelect.add(storageSelect, locationSelect, cellSelect);

        vSearch.add(hSelect, hSearch);
        vSearch.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        //vSearch.setPadding(false);
        //vMain.setPadding(false);
        vMain.add(vSearch, content);
        vMain.setSizeFull();

        setContent(vMain);
        close();
        storageSelect.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                storageID = e.getValue().getId();
                articleNumber.setEnabled(true);
                btnArticleSearch.setEnabled(true);
                materialName.setEnabled(true);
                locationSelect.setItems(locationService.getFindLocationByStorageID(storageID));
                ItemLabelGenerator<LocationEntity> itemLabelGenerator = locationEntity -> locationEntity.getLocationName() + " - " + locationEntity.getLocationDescription();
                locationSelect.setItemLabelGenerator(itemLabelGenerator);
                locationSelect.setEnabled(true);


                btnMaterialSearch.setEnabled(true);

                updateGridStore();
                materialName.setItems(materialInfoService.getAllByStorage(storageID));
                materialName.setItemLabelGenerator(MaterialInfoEntity::getMaterialName);
            }
        });
        cellSelect.addValueChangeListener(e -> {
           if (e.getValue() != null) {
               cellID = e.getValue().getId();
               updateGridStoreCell();
               materialName.setItems(materialInfoService.getAllByStorageCell(storageID, cellID, locationID));
               materialName.setItemLabelGenerator(MaterialInfoEntity::getMaterialName);
           }
        });
        btnArticleSearch.addClickListener(e -> {
            if (!articleNumber.isEmpty())
                updateGridArticle();
            else {
                Notification.show("Введите артикул!", 2000, Notification.Position.MIDDLE);
                return;
            }
        });
        btnMaterialSearch.addClickListener(e -> {
           if (!materialName.isEmpty())
               updateGridMaterialName();
           else {
               Notification.show("Выберите название объекта хранения!", 3000, Notification.Position.MIDDLE);
               return;
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
        colDescription = grid.addColumn(materialInfoEntity -> materialInfoEntity.getDescription()).setHeader("Описание");
        colSupplier = grid.addColumn(materialInfoEntity -> materialInfoEntity.getSupplierEntity().getSupplierName()).setHeader("Поставщик");
        colStorage = grid.addColumn(materialInfoEntity -> materialInfoEntity.getStorage().getStorageName()).setHeader("Склад");
        colCell = grid.addColumn(materialInfoEntity -> materialInfoEntity.getCell().getCellName()).setHeader("Ячейка");
        colArticle = grid.addColumn(materialInfoEntity -> materialInfoEntity.getArticle()).setHeader("Артикул");
        colMeas = grid.addColumn(materialInfoEntity -> materialInfoEntity.getMeas().getMeasName()).setHeader("Ед. изм.");
        colQuantity = grid.addColumn(materialInfoEntity -> materialInfoEntity.getQuantity()).setHeader("Приход");
        colExpense = grid.addColumn(materialInfoEntity -> materialInfoEntity.getExpense()).setHeader("Расход");
        colBalance = grid.addColumn(materialInfoEntity -> materialInfoEntity.getBalance()).setHeader("Остаток");

        colMaterialName.setResizable(true);
        colDescription.setResizable(true);
        colSupplier.setResizable(true);
        colStorage.setResizable(true);
        colCell.setResizable(true);
        colArticle.setResizable(true);
        colMeas.setResizable(true);
        colQuantity.setResizable(true);
        colExpense.setResizable(true);
        colBalance.setResizable(true);

        grid.asSingleSelect().addValueChangeListener(e -> {
            if (e.getValue() != null) {
                editForm(e.getValue());
            }
            else
                close();
        });
    }
    private void updateGridStore() {
        dataProvider = new ListDataProvider<>(
                materialInfoService.getAllByStorage(storageID));
        grid.setItems(dataProvider);
    }
    private void updateGridStoreLocation() {
        dataProvider = new ListDataProvider<>(
                materialInfoService.getAllByStorageLocation(storageID, locationID));
        grid.setItems(dataProvider);
    }
    private void updateGridStoreCell() {
        dataProvider = new ListDataProvider<>(
                materialInfoService.getAllByStorageCell(storageID, cellID, locationID));
        grid.setItems(dataProvider);
    }
    private void updateGridArticle() {
        dataProvider = new ListDataProvider<>(
                materialInfoService.getAllByArticle(articleNumber.getValue(), storageID));
        grid.setItems(dataProvider);
    }
    private void updateGridMaterialName() {
        dataProvider = new ListDataProvider<>(
                materialInfoService.getAllByMaterialName(materialName.getValue().getMaterialName(), storageID));
        grid.setItems(dataProvider);
    }
    private void editForm(MaterialInfoEntity materialInfoEntity) {
        if (materialInfoEntity == null)
            close();
        formMaterialDetail.setMaterialInfo(materialInfoEntity);
        formMaterialDetail.setVisible(true);
        addClassName("editing");
    }
    private void close() {
        formMaterialDetail.setMaterialInfo(null);
        formMaterialDetail.setVisible(false);
    }
    private void btnClose() {
        formMaterialDetail.setMaterialInfo(null);
        formMaterialDetail.setVisible(false);
        updateGridStore();
    }
}
