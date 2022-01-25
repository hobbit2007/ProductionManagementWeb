package com.vaadin.tutorial.crm.threads;

import com.sourceforge.snap7.moka7.S7;
import com.sourceforge.snap7.moka7.S7Client;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.PlcFermentationValue;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.SignalList;
import com.vaadin.tutorial.crm.service.plccontrollersservice.PlcFermentationService;

import java.util.Date;
import java.util.List;

/**
 * Класс поток запускает запись в БД значений ПЛК ферментация
 */
public class StartRecordForFermentation extends Thread{
    private List<SignalList> array;
    S7Client s7Client;
    byte[] buffer = new byte[65536];
    Long timeRepeat;
    private final PlcFermentationService plcFermentationService;

    public StartRecordForFermentation(List<SignalList> array, S7Client s7Client, Long timeRepeat, PlcFermentationService plcFermentationService) {
        this.array = array;
        this.s7Client = s7Client;
        this.timeRepeat = timeRepeat;
        this.plcFermentationService = plcFermentationService;
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
                        PlcFermentationValue plcFermentationValue = new PlcFermentationValue();
                        plcFermentationValue.setIdOrderNum(1L);
                        plcFermentationValue.setDelete(0L);
                        plcFermentationValue.setValue((float) (Math.ceil(readData * scale) / scale)); //Precision.round(readData, 2) - округляем до двух знаков после запятой
                        plcFermentationValue.setDateCreate(new Date());
                        plcFermentationValue.setIdSignal(array.get(i).getId());
                        plcFermentationValue.setAlarm(0L);

                        try {
                            plcFermentationService.saveAll(plcFermentationValue);
                            //System.out.println("RECORD ADD TO DB FERMENTATION");
                        }
                        catch (Exception ex) {
                            Notification.show("Не могу записать в БД значения из ПЛК ферментация" + ex.getMessage(), 5000, Notification.Position.MIDDLE);
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
