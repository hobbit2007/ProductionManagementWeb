package com.vaadin.tutorial.crm.ui.chart;
import com.sourceforge.snap7.moka7.S7;
import com.sourceforge.snap7.moka7.S7Client;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.SignalList;
import java.util.List;

/**
 * Класс поток - обновляет значение переменных прочитанных из ПЛК Мойки, для построения графиков в реальном времени каждые 2 сек.
 */
public class UpdateValueChartWashing extends Thread{
    private final UI ui;
    private Configuration configuration;
    private DataSeries series;
    private List<SignalList> array;
    private S7Client s7Client;
    byte[] buffer = new byte[65536];


    public UpdateValueChartWashing(UI ui, Configuration configuration, DataSeries series, List<SignalList> array, S7Client s7Client) {
        this.ui = ui;
        this.configuration = configuration;
        this.series = series;
        this.array = array;
        this.s7Client = s7Client;
    }
    @Override
    public void run() {

        while (!isInterrupted()) {
            try {
                if (s7Client == null) {
                    return;
                }
               ui.access(() -> {
                    if (array.size() != 0) {
                        for (int i = 0; i < array.size(); i++) {
                            s7Client.ReadArea(S7.S7AreaDB, array.get(i).getDbValue(), 0, array.get(i).getPosition() + array.get(i).getOffset(), buffer);
                            float readData = S7.GetFloatAt(buffer, array.get(i).getPosition());
                            double scale = Math.pow(10, 2);
                            series.setName(array.get(i).getSignalName());
                            series.add(new DataSeriesItem(System.currentTimeMillis(), (Math.ceil(readData * scale) / scale)), true, true);

                            configuration.setSeries(series);

                            ui.push();
                            // System.out.println("FROM THREAD [" + getId() + " - " + getName()+ "] =" + readData + "I = " + i);
                        }
                    }
                });
                sleep(3000);
            } catch (Exception e) { //Interrupted
                break;
            }
        }
    }
}
