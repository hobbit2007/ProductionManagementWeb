package com.vaadin.tutorial.crm.ui.chart;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.tutorial.crm.model.DataFromPlc;

import java.util.List;

/**
 * Класс поток - обновляет значение переменных прочитанных из ПЛК, для построения графиков в реальном времени каждые 2 сек.
 */
public class UpdateValueChart extends Thread{
    private final UI ui;
    private Configuration configuration;
    private DataSeries series;
    private List<DataFromPlc> array;

    public UpdateValueChart(UI ui, Configuration configuration, DataSeries series, List<DataFromPlc> array) {
        this.ui = ui;
        this.configuration = configuration;
        this.series = series;
        this.array = array;
    }
    @Override
    public void run() {
        try {
            ui.access(() -> {
                for (int i = 0; i < array.size(); i++) {
                    series.add(new DataSeriesItem(System.currentTimeMillis() + i * 1000, array.get(i).getValue()));
                    //ui.push();
                }
                //System.out.println("ARRAYSIZE = " + array.size());
                configuration.setSeries(series);
                ui.push();
            });
            interrupt();
        } catch (Exception e) { //Interrupted
            e.printStackTrace();
        }
    }
}
