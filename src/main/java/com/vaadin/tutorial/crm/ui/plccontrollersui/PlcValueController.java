package com.vaadin.tutorial.crm.ui.plccontrollersui;

import com.sourceforge.snap7.moka7.S7;
import com.sourceforge.snap7.moka7.S7Client;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Label;
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
import com.vaadin.tutorial.crm.model.DataFromPlc;
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
import java.util.Arrays;
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

    private List<SignalList> controllerSignalList = new ArrayList<>();
    private ListDataProvider<PlcValue> dataProvider;
    private boolean radioButtonFlag = true;//Указывает какое положение было выбрано: реальное время или БД. По умолчанию: реальное время
    private Label controllerStatus = new Label();
    S7Client client = new S7Client();
    public static byte[] buffer = new byte[65536];
    public static Thread uploadFields = new Thread();
    public static AttachEvent attachEvent;
    public static List<TextField> sigFieldList = new ArrayList<>();
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

        controllerStatus.getStyle().set("color", "red");
        controllerStatus.getStyle().set("font-weight", "bold");
        controllerStatus.getStyle().set("font-size", "11pt");

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

        hTitleContent.add(choose, selectController, controllerStatus);
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

            onAttach(this.attachEvent);
            removeAll();
            add(vContent, compLabel, initController(radioButtonFlag, e.getValue().getId()));
            setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        }
    }

    public Component initController(boolean flag, Long controllerId) {
        FormLayout fContent = new FormLayout();
        VerticalLayout verticalLayout = new VerticalLayout();
        List<SignalList> controllerSignalListTemp = new ArrayList<>();//временный массив для хранения сигналов, нужен чтоб избежать переполнения при обновлении страницы

        if (flag) {
            //Проверяем доступен контроллер или нет
            //client.SetConnectionType(S7.OP);
            //String test = selectController.getValue().getIp();
            //client.ConnectTo(selectController.getValue().getIp(), 0, 1);
            SchedulerService.controllerDisconnect();
            if (SchedulerService.controllerStatus(selectController.getValue().getIp())) {
                SchedulerService.stopThread = false;
                controllerStatus.setVisible(false);
                controllerSignalListTemp.removeAll(controllerSignalListTemp);
                controllerSignalListTemp = controllerSignalList;
                SchedulerService.controllerParam(controllerSignalListTemp);
                TextField[] controllerValue = new TextField[controllerSignalListTemp.size()];
                sigFieldList.removeAll(sigFieldList);
                for (int i = 0; i < controllerSignalListTemp.size(); i++) { //i < controllerSignalList.size()
                    controllerValue[i] = new TextField(controllerSignalListTemp.get(i).getSignalName()); //controllerSignalList.get(i).getSignalName()
                    controllerValue[i].setWidth("55px");
                    controllerValue[i].setValue("0.00");

                    fContent.add(controllerValue[i]);
                    fContent.setResponsiveSteps(new FormLayout.ResponsiveStep("45px", 10));
                    verticalLayout.add(fContent);
                    sigFieldList.add(controllerValue[i]);
                    //textFieldUpdate[i] = new UpdateValueController(attachEvent.getUI(), controllerValue[i], controllerSignalList.get(i).getDbValue(),
                    //        controllerSignalList.get(i).getPosition(), controllerSignalList.get(i).getOffset(), selectController.getValue().getIp());
                    //textFieldUpdate[i].start();

                    controllerValue[i].getElement().setAttribute("data-title", controllerSignalListTemp.get(i).getSignalDescription());
                    controllerValue[i].setClassName("tooltip");
                }
                //for (int i = 0; i < controllerSignalListTemp.size(); i++) {
                //    client.ReadArea(S7.S7AreaDB, controllerSignalListTemp.get(i).getDbValue(), 0, controllerSignalListTemp.get(i).getPosition() + controllerSignalListTemp.get(i).getOffset(), buffer);
                //    float readData = S7.GetFloatAt(buffer, controllerSignalListTemp.get(i).getPosition());
                //    System.out.println("READDATA = " + readData);
                //}

            }
            else {
                controllerStatus.setVisible(true);
                controllerStatus.setText("Контроллер " + selectController.getValue().getIp() + " - " + selectController.getValue().getControllerName() + " не доступен!");
            }
            //uploadFields = new UpdateValueController(attachEvent.getUI(), sigFieldList);
            //uploadFields.start();
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

    public static void startThread(List<DataFromPlc> array) {
        uploadFields = new UpdateValueController(attachEvent.getUI(), sigFieldList, array);
        uploadFields.start();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        // Start the data feed thread
        this.attachEvent = attachEvent;
        //for (int i = 0; i < controllerSignalList.size(); i++) {
        //    textFieldUpdate[i] = new UpdateValueController(attachEvent.getUI(), controllerValue[i], SchedulerService.dataFromPlcList.size());
        //    textFieldUpdate[i].start();
       // }
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Cleanup
        SchedulerService.stopThread = true;
        SchedulerService.controllerDisconnect();
    }
}
