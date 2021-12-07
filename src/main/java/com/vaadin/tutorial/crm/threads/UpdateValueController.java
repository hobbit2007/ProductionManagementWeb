package com.vaadin.tutorial.crm.threads;

import com.sourceforge.snap7.moka7.S7;
import com.sourceforge.snap7.moka7.S7Client;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.SignalList;

import java.util.List;

/**
* Класс поток - обновляет значение переменных прочитанных из ПЛК, для визуализации данных в TextField каждые 2 сек.
*/
public class UpdateValueController extends Thread {
    private final UI ui;
    private  List<TextField> element;
    private List<SignalList> array;
    private S7Client s7Client;
    byte[] buffer = new byte[65536];

    public UpdateValueController(UI ui, List<TextField> element, List<SignalList> array, S7Client s7Client) {//int db, int pos, int offset, String controllerIpAddress
        this.ui = ui;
        this.element = element;
        this.array = array;
        this.s7Client = s7Client;
    }

    @Override
    public void run() {

        while (!isInterrupted()) {
            try {
                ui.access(() -> {
                    for (int i = 0; i < element.size(); i++) {
                        s7Client.ReadArea(S7.S7AreaDB, array.get(i).getDbValue(), 0, array.get(i).getPosition() + array.get(i).getOffset(), buffer);
                        float readData = S7.GetFloatAt(buffer, array.get(i).getPosition());
                        element.get(i).setValue(String.valueOf(readData));
                        //System.out.println("FROM THREAD [" + getId() + " - " + getName()+ "] =" + readData);
                        ui.push();
                    }
                });
                sleep(2000);
            } catch (InterruptedException e) { //Interrupted
                break;
                //e.printStackTrace();
            }
        }
    }
}
