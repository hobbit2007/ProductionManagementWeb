package com.vaadin.tutorial.crm.threads;

import com.sourceforge.snap7.moka7.S7;
import com.sourceforge.snap7.moka7.S7Client;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcValue;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.SignalList;
import com.vaadin.tutorial.crm.service.plccontrollersservice.PlcValueService;

import java.util.Date;
import java.util.List;

/**
 * Класс поток запускает запись в БД значений ПЛК контроллеров
 */
public class StartRecordForPLC extends Thread{
    private List<SignalList> array;
    S7Client s7Client;
    Long controllerID;
    byte[] buffer = new byte[65536];
    Long timeRepeat;
    private final PlcValueService plcValueService;
    public StartRecordForPLC(List<SignalList> array, S7Client s7Client, Long timeRepeat, PlcValueService plcValueService, Long controllerID) {
        this.array = array;
        this.s7Client = s7Client;
        this.timeRepeat = timeRepeat;
        this.plcValueService = plcValueService;
        this.controllerID = controllerID;
    }
    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                Thread.sleep(timeRepeat);
                synchronized (this) {
                    float readData = -1.00f;
                    for (int i = 0; i < array.size(); i++) {
                        s7Client.ReadArea(S7.S7AreaDB, array.get(i).getDbValue(), 0,
                                array.get(i).getPosition() + array.get(i).getOffset(), buffer);
                        readData = S7.GetFloatAt(buffer, array.get(i).getPosition());

                        double scale = Math.pow(10, 2);
                        PlcValue plcValue = new PlcValue();
                        plcValue.setIdOrderNum(1L);
                        plcValue.setDelete(0L);
                        plcValue.setValue((float) (Math.ceil(readData * scale) / scale)); //Precision.round(readData, 2) - округляем до двух знаков после запятой
                        plcValue.setIdController(controllerID);
                        plcValue.setDateCreate(new Date());
                        plcValue.setIdSignalName(array.get(i).getId());
                        plcValue.setAlarm(0L);

                        try {
                            plcValueService.saveAll(plcValue);
                            //System.out.println("RECORD ADD TO DB WASHING");
                        }
                        catch (Exception ex) {
                            Notification.show("Не могу записать в БД значения из ПЛК" + ex.getMessage(), 5000, Notification.Position.MIDDLE);
                            return;
                        }
                    }
                }
            }
            catch (InterruptedException e) {
                break;
            }
        }
    }
}
