package com.vaadin.tutorial.crm.ui.plccontrollersui;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.SignalList;
import com.vaadin.tutorial.crm.service.plccontrollersservice.PLCConnect;
import com.vaadin.tutorial.crm.service.plccontrollersservice.PlcControllersService;
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
    private AnyComponent anyComponent = new AnyComponent();
    private List<SignalList> controllerSignalList = new ArrayList<>();
    private Label controllerStatus = new Label();
    long controllerID = 1L;
    Thread updateFields = new Thread();
    List<TextField> sigFieldList = new ArrayList<>();
    String controllerIP;
    TextField[] controllerValue;
    PlcControllersService plcControllersService;
    SignalListService signalListService;

    @Autowired
    public PlcValueWashing(PlcControllersService plcControllersService, SignalListService signalListService) {
        this.plcControllersService = plcControllersService;
        this.signalListService = signalListService;

        addClassName("list-view");
        setSizeFull();

        controllerStatus.getStyle().set("color", "red");
        controllerStatus.getStyle().set("font-weight", "bold");
        controllerStatus.getStyle().set("font-size", "11pt");

        controllerSignalList = signalListService.findSignalList(controllerID);
        controllerIP = plcControllersService.getAllByID(controllerID).get(0).getIp();

        vLabel.add(anyComponent.labelTitle("Визуализация значений контроллера " + plcControllersService.getAllByID(controllerID).get(0).getControllerName()));
        vLabel.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        hTitleContent.add(controllerStatus); //choose, selectController,
        hTitleContent.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);

        vContent.add(vLabel, hTitleContent, initController());
        vContent.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        add(vContent);
    }

    public Component initController() {

        FormLayout fContent = new FormLayout();
        VerticalLayout verticalLayout = new VerticalLayout();
        if (PLCConnect.contrConnectedWashing) {
            controllerStatus.setVisible(false);
            controllerValue = new TextField[controllerSignalList.size()];
            sigFieldList.removeAll(sigFieldList);
            for (int i = 0; i < controllerSignalList.size(); i++) {
                controllerValue[i] = new TextField(controllerSignalList.get(i).getSignalName() + "-" + controllerSignalList.get(i).getGroupName().getShortSignalDescription());
                controllerValue[i].setWidth("55px");
                controllerValue[i].setValue("0.00");

                fContent.add(controllerValue[i]);
                fContent.setResponsiveSteps(new FormLayout.ResponsiveStep("45px", 10));
                verticalLayout.add(fContent);
                sigFieldList.add(controllerValue[i]);
                controllerValue[i].getElement().setAttribute("data-title", controllerSignalList.get(i).getSignalDescription());
                controllerValue[i].setClassName("tooltip");
            }
        }
        else {
           controllerStatus.setVisible(true);
           controllerStatus.setText("Контроллер " + controllerIP + " - " + plcControllersService.getAllByID(controllerID).get(0).getControllerName() + " не доступен!");
        }

        return verticalLayout;
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
