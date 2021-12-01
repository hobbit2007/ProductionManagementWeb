package com.vaadin.tutorial.crm.ui.plccontrollersui;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.OLDPlcWashing;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcControllers;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcValue;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.SignalList;
import com.vaadin.tutorial.crm.service.plccontrollersservice.PlcControllersService;
import com.vaadin.tutorial.crm.service.plccontrollersservice.PlcValueService;
import com.vaadin.tutorial.crm.service.plccontrollersservice.SchedulerService;
import com.vaadin.tutorial.crm.service.plccontrollersservice.SignalListService;
import com.vaadin.tutorial.crm.threads.FeederThread;
import com.vaadin.tutorial.crm.threads.UpdateValueController;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;
import com.vaadin.tutorial.crm.ui.layout.MainLayout;
import com.vaadin.tutorial.crm.ui.layout.PlcLayout;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс содержащий реализацию визуальных элементов и логики для plc контроллера
 * в реальном времени и из базы
 */
@Route(value = "plcvalue", layout = PlcLayout.class)
@PageTitle("Визуализация значений контроллера | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class PlcValueController extends VerticalLayout {
    private Grid<PlcValue> grid;
    private Grid.Column<PlcValue> colSignalName, colSignalDesc, colValue, colDate, colOrder;
    private VerticalLayout vContent = new VerticalLayout();
    private VerticalLayout vLabel = new VerticalLayout();
    private HorizontalLayout hTitleContent = new HorizontalLayout();
    private AnyComponent anyComponent = new AnyComponent();
    private RadioButtonGroup choose = new RadioButtonGroup();
    private ComboBox<PlcControllers> selectController = new ComboBox<>("Выберите контроллер:");
    private TextField[] controllerValue = new TextField[100];
    private List<SignalList> controllerSignalList = new ArrayList<>();
    private ListDataProvider<PlcValue> dataProvider;
    private boolean radioButtonFlag = true;//Указывает какое положение было выбрано: реальное время или БД. По умолчанию: реальное время
    Thread[] textFieldUpdate = new Thread[10000];
    PlcControllersService plcControllersService;
    SignalListService signalListService;
    PlcValueService plcValueService;

    @Autowired
    public PlcValueController(PlcControllersService plcControllersService, SignalListService signalListService, PlcValueService plcValueService) {
        this.plcControllersService = plcControllersService;
        this.signalListService = signalListService;
        this.plcValueService = plcValueService;

        addClassName("list-view");
        setSizeFull();

        vLabel.add(anyComponent.labelTitle("Визуализация значений контроллеров"));
        vLabel.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        List<String> items = new ArrayList<>();
        items.add("В реальном времени");
        items.add("Из базы");
        choose.setItems(items);
        choose.setValue(items.get(0));

        choose.addValueChangeListener(e -> {
           if (e.getValue() != null) {
               if (e.getValue().equals(items.get(0)))
                   radioButtonFlag = true;
               else
                   radioButtonFlag = false;
           }
        });

        selectController.setItems(plcControllersService.getAll());
        selectController.setItemLabelGenerator(PlcControllers::getControllerName);

        hTitleContent.add(choose, selectController);
        hTitleContent.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        configureGrid();

        vContent.add(vLabel, hTitleContent);
        vContent.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(vContent);

        selectController.addValueChangeListener(e -> {
            comboBoxSelected(e, radioButtonFlag);
        });
    }

    private void comboBoxSelected(AbstractField.ComponentValueChangeEvent<ComboBox<PlcControllers>, PlcControllers> e, boolean flag) {
        if(e.getValue() != null) {
            Component compLabel;

            if (flag)
                compLabel = anyComponent.labelTitle(selectController.getValue().getControllerName() + " (реальное время)");
            else
                compLabel = anyComponent.labelTitle(selectController.getValue().getControllerName() + " (БД)");

            controllerSignalList = signalListService.findSignalList(e.getValue().getId());
            SchedulerService.controllerParam(controllerSignalList);
            removeAll();
            add(vContent, compLabel, initController(radioButtonFlag, e.getValue().getId()));
            setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        }
    }

    public Component initController(boolean flag, Long controllerId) {
        FormLayout fContent = new FormLayout();
        VerticalLayout verticalLayout = new VerticalLayout();
        if (flag) {
            if (SchedulerService.dataFromPlcList.size() != 0) {
                for (int i = 0; i < SchedulerService.dataFromPlcList.size(); i++) { //i < controllerSignalList.size()
                    controllerValue[i] = new TextField(); //controllerSignalList.get(i).getSignalName()
                    //controllerValue[i].setWidth("55px");
                    //controllerValue[i].setValue("0.00");

                    fContent.add(controllerValue[i]);
                    verticalLayout.add(fContent);
                }
            }
        }
        else {
            verticalLayout.add(grid);
            verticalLayout.setSizeFull();
            updateList(controllerId);
        }
        return verticalLayout;
    }

    private void configureGrid() {
        grid = new Grid<>();
        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS,
                GridVariant.LUMO_ROW_STRIPES);
        grid.setColumnReorderingAllowed(true);
        grid.addClassName("contact-grid");
        grid.setSizeFull();

        colSignalName = grid.addColumn(plcValue -> plcValue.getInfo().getSignalName()).setHeader("Имя переменной");//getInfo().getSignalName()
        colSignalDesc = grid.addColumn(plcValue -> plcValue.getInfo().getSignalDescription()).setHeader("Описание переменной");//getInfo().getSignalDescription()
        colValue = grid.addColumn(plcValue -> plcValue.getValue()).setHeader("Значение");
        colDate = grid.addColumn(plcValue -> plcValue.getDateCreate()).setHeader("Дата");
        colOrder = grid.addColumn(plcValue -> plcValue.getIdOrderNum()).setHeader("Заказ");

        colSignalName.setResizable(true);
        colSignalDesc.setResizable(true);
        colValue.setResizable(true);
        colDate.setResizable(true);
        colOrder.setResizable(true);

        colSignalName.setSortable(true);
        colSignalDesc.setSortable(true);
        colValue.setSortable(true);
        colDate.setSortable(true);
        colOrder.setSortable(true);
    }

    private void updateList(Long controllerId) {
        dataProvider = new ListDataProvider<>(
          plcValueService.getSignalOnController(controllerId));
        grid.setItems(dataProvider);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        // Start the data feed thread
        if (SchedulerService.dataFromPlcList.size() != 0) {
            for (int i = 0; i < SchedulerService.dataFromPlcList.size(); i++) {
                textFieldUpdate[i] = new UpdateValueController(attachEvent.getUI(), controllerValue[i], SchedulerService.dataFromPlcList.size());
                textFieldUpdate[i].start();
            }
        }
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Cleanup
        if (SchedulerService.dataFromPlcList.size() != 0) {
            for (int i = 0; i < SchedulerService.dataFromPlcList.size(); i++) {
                textFieldUpdate[i].interrupt();
                textFieldUpdate[i] = null;
            }
        }
    }
}
