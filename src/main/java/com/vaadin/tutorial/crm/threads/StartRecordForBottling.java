package com.vaadin.tutorial.crm.threads;

import com.sourceforge.snap7.moka7.S7;
import com.sourceforge.snap7.moka7.S7Client;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcBottlingValue;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.SignalList;
import com.vaadin.tutorial.crm.service.plccontrollersservice.PlcBottlingService;

import java.util.Date;
import java.util.List;

/**
 * Класс поток запускает запись в БД значений ПЛК розлив
 */
public class StartRecordForBottling extends Thread{
    private List<SignalList> array;
    S7Client s7Client;
    byte[] buffer = new byte[65536];
    Long timeRepeat;
    private final PlcBottlingService plcBottlingService;

    public StartRecordForBottling(List<SignalList> array, S7Client s7Client, Long timeRepeat, PlcBottlingService plcBottlingService) {
        this.array = array;
        this.s7Client = s7Client;
        this.timeRepeat = timeRepeat;
        this.plcBottlingService = plcBottlingService;
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
                                array.get(i).getPosition() + array.get(i).getFOffset(), buffer);
                        readData = S7.GetFloatAt(buffer, array.get(i).getPosition());

                        double scale = Math.pow(10, 2);
                        PlcBottlingValue plcBottlingValue = new PlcBottlingValue();
                        plcBottlingValue.setIdOrderNum(1L);
                        plcBottlingValue.setDelete(0L);
                        plcBottlingValue.setValue((float) (Math.ceil(readData * scale) / scale)); //Precision.round(readData, 2) - округляем до двух знаков после запятой
                        plcBottlingValue.setDateCreate(new Date());
                        plcBottlingValue.setIdSignal(array.get(i).getId());
                        plcBottlingValue.setAlarm(0L);

                        try {
                            plcBottlingService.saveAll(plcBottlingValue);
                            //System.out.println("RECORD ADD TO DB BOTTLING");
                        }
                        catch (Exception ex) {
                            Notification.show("Не могу записать в БД значения из ПЛК розлив" + ex.getMessage(), 5000, Notification.Position.MIDDLE);
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
