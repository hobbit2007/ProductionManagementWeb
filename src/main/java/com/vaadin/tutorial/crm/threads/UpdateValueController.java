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
                    for (int i = 0; i < size; i++) {
                        if (SchedulerService.dataFromPlcList.size() != 0) {
                            element.setValue(String.valueOf(SchedulerService.dataFromPlcList.get(i).getValue()));
                            ui.push();
                            System.out.println(SchedulerService.dataFromPlcList.get(i).getSignalName() + "=" + SchedulerService.dataFromPlcList.get(i).getValue());
                        }
                    }
                });
                count--;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
