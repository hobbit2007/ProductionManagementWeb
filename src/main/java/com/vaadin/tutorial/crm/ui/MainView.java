package com.vaadin.tutorial.crm.ui;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.SignalList;
import com.vaadin.tutorial.crm.model.DataFromPlc;
import com.vaadin.tutorial.crm.service.plccontrollersservice.SignalListService;
import com.vaadin.tutorial.crm.threads.FeederThread;
import com.vaadin.tutorial.crm.ui.layout.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Главный класс приложения
 */
@Route(value = "", layout = MainLayout.class)
@PageTitle("Dashboard | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends VerticalLayout {

    private Label labelUser  = new Label();
    VerticalLayout vContent = new VerticalLayout();
    HorizontalLayout hContent = new HorizontalLayout();
    FeederThread thread;

    Chart chart = new Chart();
    public static List<DataFromPlc> arrayForChart = new ArrayList<>();
    private List<SignalList> controllerSignalList = new ArrayList<>();
    private static Configuration configuration;
    private static DataSeries series;
    private static Thread uploadChart = new Thread();
    public static AttachEvent attachEvent;

    private SignalListService signalListService;

    @Autowired
    public MainView(SignalListService signalListService) {
        this.signalListService = signalListService;

        labelUser.getStyle().set("color", "red");
        labelUser.getStyle().set("font-weight", "bold");
        labelUser.getStyle().set("font-size", "11pt");
        //labelUser.getStyle().set("border", "1px inset blue");

        configuration = chart.getConfiguration();
        configuration.getChart().setType(ChartType.SPLINE);
        configuration.getTitle().setText("Live data");

        hContent.add(labelUser);

        vContent.add(hContent, initDemo());
        vContent.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        vContent.setSizeFull();


        add(vContent);

    }

    public Component initDemo() {
        //controllerSignalList = signalListService.findSignalList(4L);
        //PLCConnect.controllerParam(controllerSignalList);
        //PLCConnect.controllerStatus("10.100.10.106");

        final Random random = new Random();
        chart.setWidth("800px");
        configuration = chart.getConfiguration();
        configuration.getChart().setType(ChartType.SPLINE);
        configuration.getTitle().setText("Live data - PLC 106");

        XAxis xAxis = configuration.getxAxis();
        xAxis.setType(AxisType.DATETIME);
        xAxis.setTickPixelInterval(150);

        YAxis yAxis = configuration.getyAxis();
        yAxis.setTitle(new AxisTitle("Значения"));

        configuration.getTooltip().setEnabled(true);
        configuration.getLegend().setEnabled(true);

        series = new DataSeries();
        series.setPlotOptions(new PlotOptionsSpline());
        series.setName("Random data");
        for (int i = -19; i <= 0; i++) {
            series.add(new DataSeriesItem(System.currentTimeMillis() + i * 1000, i));
        }
        //final long x = System.currentTimeMillis();
        //final double y = random.nextDouble();
        //series.add(new DataSeriesItem(x, y), true, true);
        configuration.setSeries(series);
        //if (arrayForChart.size() != 0) {
        //    for (int i = 0; i <= arrayForChart.size(); i++) {
        //        series.add(new DataSeriesItem(System.currentTimeMillis() + i * 1000, arrayForChart.get(i).getValue()));
        //    }

        //    configuration.setSeries(series);
       // }

       /* runWhileAttached(chart, () -> {
            final long x = System.currentTimeMillis();
            final double y = random.nextDouble();
            series.add(new DataSeriesItem(x, y), true, true);
        }, 1000, 1000);*/

        return chart;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        thread = new FeederThread(attachEvent.getUI(), labelUser);
        thread.start();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        thread.interrupt();
        thread = null;
    }
}