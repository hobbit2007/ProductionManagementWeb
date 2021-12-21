package com.vaadin.tutorial.crm.ui.plccontrollersui;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.SignalGroup;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.SignalList;
import com.vaadin.tutorial.crm.service.plccontrollersservice.PLCConnect;
import com.vaadin.tutorial.crm.service.plccontrollersservice.PlcControllersService;
import com.vaadin.tutorial.crm.service.plccontrollersservice.SignalGroupsService;
import com.vaadin.tutorial.crm.service.plccontrollersservice.SignalListService;
import com.vaadin.tutorial.crm.threads.UpdateValueController;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;
import com.vaadin.tutorial.crm.ui.layout.PlcLayout;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс содержащий реализацию визуальных элементов и логики для plc контроллера мойки
 * в реальном времени
 */
@Route(value = "plcwasing", layout = PlcLayout.class)
@PageTitle("Визуализация значений контроллера Мойки | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class PlcValueWashing extends VerticalLayout{
    private VerticalLayout vContent = new VerticalLayout();
    private VerticalLayout vLabel = new VerticalLayout();
    private HorizontalLayout hTitleContent = new HorizontalLayout();
    private HorizontalLayout horizontalLayout = new HorizontalLayout();
    private Scroller scroller = new Scroller();
    private HorizontalLayout hSignalGroup[] = new HorizontalLayout[100];
    private VerticalLayout vSignalGroup[] = new VerticalLayout[100];
    private Label groupSignalName[] = new Label[100];
    private FormLayout[] fContent = new FormLayout[100];
    private AnyComponent anyComponent = new AnyComponent();
    private List<SignalList> controllerSignalList = new ArrayList<>();
    private List<SignalGroup> controllerSignalGroup = new ArrayList<>();
    private Label controllerStatus = new Label();
    long controllerID = 1L;
    Thread updateFields = new Thread();
    List<TextField> sigFieldList = new ArrayList<>();
    String controllerIP;
    TextField[] controllerValue;
    PlcControllersService plcControllersService;
    SignalListService signalListService;
    SignalGroupsService signalGroupsService;

    @Autowired
    public PlcValueWashing(PlcControllersService plcControllersService, SignalListService signalListService, SignalGroupsService signalGroupsService) {
        this.plcControllersService = plcControllersService;
        this.signalListService = signalListService;
        this.signalGroupsService = signalGroupsService;

        addClassName("list-view");
        setSizeFull();

        controllerStatus.getStyle().set("color", "red");
        controllerStatus.getStyle().set("font-weight", "bold");
        controllerStatus.getStyle().set("font-size", "11pt");

        controllerSignalList = signalListService.findSignalList(controllerID);
        controllerSignalGroup = signalGroupsService.getAll();
        controllerIP = plcControllersService.getAllByID(controllerID).get(0).getIp();

        for (int i = 0; i < controllerSignalGroup.size(); i++) {
            hSignalGroup[i] = new HorizontalLayout();
            vSignalGroup[i] = new VerticalLayout();
            fContent[i] = new FormLayout();
            groupSignalName[i] = new Label();
            groupSignalName[i].getStyle().set("color", "#d3b342");
            groupSignalName[i].getStyle().set("font-weight", "bold");
            groupSignalName[i].getStyle().set("font-size", "16pt");
            groupSignalName[i].getStyle().set("margin-left", "30px");
        }

        vLabel.add(anyComponent.labelTitle("Визуализация значений " + plcControllersService.getAllByID(controllerID).get(0).getControllerName()));
        vLabel.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        hTitleContent.add(controllerStatus); //choose, selectController,
        hTitleContent.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);

        horizontalLayout.setSizeFull();
        scroller.setSizeFull();

        vContent.add(vLabel, hTitleContent, initController());
        vContent.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        add(vContent);
    }

    public Component initController() {

        if (PLCConnect.contrConnectedWashing) {
            controllerStatus.setVisible(false);
            controllerValue = new TextField[controllerSignalList.size()];
            sigFieldList.removeAll(sigFieldList);
            for (int i = 0; i < controllerSignalList.size(); i++) {
                for (int j = 0; j < controllerSignalGroup.size(); j++) {
                    if (controllerSignalList.get(i).getIdGroup() == controllerSignalGroup.get(j).getId()) {
                        groupSignalName[j].setText(this.controllerSignalGroup.get(j).getShortSignalDescription());
                        controllerValue[i] = new TextField(controllerSignalList.get(i).getSignalName());
                        controllerValue[i].setWidth("50px");
                        controllerValue[i].setValue("0.00");
                        fContent[j].add(controllerValue[i]);
                        fContent[j].setResponsiveSteps(new FormLayout.ResponsiveStep("50px", 2));
                        vSignalGroup[j].add(groupSignalName[j], fContent[j]);
                        vSignalGroup[j].setPadding(false);
                        vSignalGroup[j].setMargin(false);
                        vSignalGroup[j].getStyle().set("border", "1px outset black");
                        horizontalLayout.add(vSignalGroup[j]);
                        scroller.setContent(horizontalLayout);
                    }
                }
                sigFieldList.add(controllerValue[i]);
                controllerValue[i].getElement().setAttribute("data-title", controllerSignalList.get(i).getSignalDescription());
                controllerValue[i].setClassName("tooltip");
            }
        }
        else {
           controllerStatus.setVisible(true);
           controllerStatus.setText("Контроллер " + controllerIP + " - " + plcControllersService.getAllByID(controllerID).get(0).getControllerName() + " не доступен!");
        }

        return scroller;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        // Start the data feed thread
        updateFields = new UpdateValueController(attachEvent.getUI(), sigFieldList, controllerSignalList, PLCConnect.clientForStatusWashing);
        updateFields.start();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Cleanup
        //PLCConnect.controllerDisconnect();
        updateFields.interrupt();
        updateFields = null;
    }
}
