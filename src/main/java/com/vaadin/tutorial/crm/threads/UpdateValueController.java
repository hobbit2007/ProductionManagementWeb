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


    public static List<DataFromPlc> dataFromPlcList = new ArrayList<>(); //Массив в котором сохраняем данные после чтения из контроллера
    private boolean controllerConnect = true;
    private int db;
    private int pos;
    private int offset;
    private String controllerIpAddress;
    private float readData;
    private List<DataFromPlc> array;
    int count = 10;


    public UpdateValueController(UI ui, List<TextField> element, List<DataFromPlc> array) {//int db, int pos, int offset, String controllerIpAddress
        this.ui = ui;
        this.element = element;
        this.array = array;
        //this.db = db;
        //this.pos = pos;
        //this.offset = offset;
        //this.controllerIpAddress = controllerIpAddress;



    }

    @Override
    public void run() {
        //S7Client clientRT = new S7Client();
        //clientRT.SetConnectionType(S7.OP);
        //clientRT.ConnectTo(controllerIpAddress, 0, 1);
        //byte[] buffer = new byte[65536];
       // while (true){ //count > 0
            try {



                //sleep(1000);
                //DataFromPlc dataFromPlc = new DataFromPlc();
                //for (int i = 0; i < numDbPosOffset.size(); i++) {
                //if (clientRT.Connected) {
                //clientRT.ReadArea(S7.S7AreaDB, db, 0, pos + offset, buffer);
                //    readData = S7.GetFloatAt(buffer, pos);
                    //dataFromPlc.setSignalName("Signal");
                    //dataFromPlc.setValue(readData);
                    //dataFromPlcList.add(dataFromPlc);
                    //}
                    //Thread.sleep(3000);
                    //System.out.println("VALUE"+getId()+" - " + getName() + " = " + readData);
                    //sleep(3000);
                int test = element.size();
                int test1 = array.size();
                    ui.access(() -> {
                        //if (element.size() == SchedulerService.dataFromPlcList.size()) {
                        for (int i = 0; i < element.size(); i++) {
                            element.get(i).setValue(String.valueOf(array.get(i).getValue()));//dataFromPlcList.get(0).getValue())
                            ui.push();
                        }
                        //System.out.println("VALUE"+getId()+" - " + getName() + " = " + readData);
                        //}
                    });

                    //System.out.println("------------------------NEW COUNT----------------------------");
                //}
                //else {
                //    return;
                //}
                //dataFromPlcList.removeAll(dataFromPlcList);
           //     sleep(3000);
                count--;
            } catch (Exception e) { //Interrupted
                e.printStackTrace();
         //   }
        }
    }
}
