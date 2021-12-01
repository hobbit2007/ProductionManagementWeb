package com.vaadin.tutorial.crm.threads;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.tutorial.crm.service.plccontrollersservice.SchedulerService;
/**
* Класс поток - проверяет подключение к контроллерам каждые 10 сек.
*/
public class UpdateValueController extends Thread {
    private final UI ui;
    private  final TextField element;
    private final int size;

    private int count = 30;


    public UpdateValueController(UI ui, TextField element, int size) {
        this.ui = ui;
        this.element = element;
        this.size = size;
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(1000);
                ui.access(()-> {
                    for (int i = size; i < size; i++) {
                        element.setValue(String.valueOf(SchedulerService.controllerConnected));
                        ui.push();
                    }
                });
                count--;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
