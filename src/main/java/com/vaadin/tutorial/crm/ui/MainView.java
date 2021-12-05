package com.vaadin.tutorial.crm.ui;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.AttachNotifier;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.model.DataFromPlc;
import com.vaadin.tutorial.crm.service.plccontrollersservice.SchedulerService;
import com.vaadin.tutorial.crm.threads.FeederThread;
import com.vaadin.tutorial.crm.ui.chart.SplineUpdatingEachSecond;
import com.vaadin.tutorial.crm.ui.layout.MainLayout;
import org.springframework.scheduling.annotation.Scheduled;

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
    //SplineUpdatingEachSecond splineUpdatingEachSecond = new SplineUpdatingEachSecond();
    Chart chart = new Chart();
    public static List<DataFromPlc> arrayForChart = new ArrayList<>();

    public MainView() {
        SchedulerService.stopThread = true;

        labelUser.getStyle().set("color", "red");
        labelUser.getStyle().set("font-weight", "bold");
        labelUser.getStyle().set("font-size", "11pt");
        //labelUser.getStyle().set("border", "1px inset blue");

        hContent.add(labelUser);

        vContent.add(hContent, initDemo());
        vContent.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        vContent.setSizeFull();


        add(vContent);

    }

    public Component initDemo() {
       // final Random random = new Random();



        final Configuration configuration = chart.getConfiguration();
        configuration.getChart().setType(ChartType.SPLINE);
        configuration.getTitle().setText("Live data");

        XAxis xAxis = configuration.getxAxis();
        xAxis.setType(AxisType.DATETIME);
        xAxis.setTickPixelInterval(150);

        YAxis yAxis = configuration.getyAxis();
        yAxis.setTitle(new AxisTitle("Value"));

        configuration.getTooltip().setEnabled(false);
        configuration.getLegend().setEnabled(false);

        final DataSeries series = new DataSeries();
        series.setPlotOptions(new PlotOptionsSpline());
        series.setName("Random data");
        if (arrayForChart.size() != 0) {
            for (int i = 0; i <= arrayForChart.size(); i++) {
                series.add(new DataSeriesItem(System.currentTimeMillis() + i * 1000, arrayForChart.get(i).getValue()));
            }

            configuration.setSeries(series);
        }

       /* runWhileAttached(chart, () -> {
            final long x = System.currentTimeMillis();
            final double y = random.nextDouble();
            series.add(new DataSeriesItem(x, y), true, true);
        }, 1000, 1000);*/

        return chart;
    }

    public static void dataForControllerChart(List<DataFromPlc> array) {
        arrayForChart = array;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        // Start the data feed thread
        thread = new FeederThread(attachEvent.getUI(), labelUser);
        thread.start();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Cleanup
        thread.interrupt();
        thread = null;
    }
}