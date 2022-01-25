package com.vaadin.tutorial.crm.threads;

import com.sourceforge.snap7.moka7.S7;
import com.sourceforge.snap7.moka7.S7Client;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcDiffusionValue;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.SignalList;
import com.vaadin.tutorial.crm.service.plccontrollersservice.PlcDiffusioService;

import java.util.Date;
import java.util.List;

/**
 * Класс поток запускает запись в БД значений ПЛК диффузия
 */
public class StartRecordForDiffusion extends Thread{
    private List<SignalList> array;
    S7Client s7Client;
    byte[] buffer = new byte[65536];
    Long timeRepeat;
    private final PlcDiffusioService plcDiffusioService;

    public StartRecordForDiffusion(List<SignalList> array, S7Client s7Client, Long timeRepeat, PlcDiffusioService plcDiffusioService) {
        this.array = array;
        this.s7Client = s7Client;
        this.timeRepeat = timeRepeat;
        this.plcDiffusioService = plcDiffusioService;
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
                        PlcDiffusionValue plcDiffusionValue = new PlcDiffusionValue();
                        plcDiffusionValue.setIdOrderNum(1L);
                        plcDiffusionValue.setDelete(0L);
                        plcDiffusionValue.setValue((float) (Math.ceil(readData * scale) / scale)); //Precision.round(readData, 2) - округляем до двух знаков после запятой
                        plcDiffusionValue.setDateCreate(new Date());
                        plcDiffusionValue.setIdSignal(array.get(i).getId());
                        plcDiffusionValue.setAlarm(0L);

                        try {
                            plcDiffusioService.saveAll(plcDiffusionValue);
                            //System.out.println("RECORD ADD TO DB DIFFUSION");
                        }
                        catch (Exception ex) {
                            Notification.show("Не могу записать в БД значения из ПЛК диффузия" + ex.getMessage(), 5000, Notification.Position.MIDDLE);
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
