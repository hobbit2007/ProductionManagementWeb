package com.vaadin.tutorial.crm.threads;

import com.sourceforge.snap7.moka7.S7;
import com.sourceforge.snap7.moka7.S7Client;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.SignalList;
import com.vaadin.tutorial.crm.model.DataFromPlc;
import com.vaadin.tutorial.crm.service.plccontrollersservice.SchedulerService;

import java.util.ArrayList;
import java.util.List;

/**
* Класс поток - проверяет подключение к контроллерам каждые 10 сек.
*/
public class UpdateValueController extends Thread {
    private final UI ui;
    private  List<TextField> element;
    private List<DataFromPlc> array;

    public UpdateValueController(UI ui, List<TextField> element, List<DataFromPlc> array) {//int db, int pos, int offset, String controllerIpAddress
        this.ui = ui;
        this.element = element;
        this.array = array;
    }

    @Override
    public void run() {
        try {
            ui.access(() -> {
                for (int i = 0; i < element.size(); i++) {
                    element.get(i).setValue(String.valueOf(array.get(i).getValue()));//dataFromPlcList.get(0).getValue())
                    ui.push();
                }
            });
            interrupt();
            } catch (Exception e) { //Interrupted
                e.printStackTrace();
        }
    }
}
