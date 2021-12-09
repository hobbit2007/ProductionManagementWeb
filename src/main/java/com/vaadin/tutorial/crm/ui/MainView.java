package com.vaadin.tutorial.crm.ui;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.entity.plccontrollersentity.SignalList;
import com.vaadin.tutorial.crm.model.DataFromPlc;
import com.vaadin.tutorial.crm.service.plccontrollersservice.PLCConnect;
import com.vaadin.tutorial.crm.service.plccontrollersservice.PlcControllersService;
import com.vaadin.tutorial.crm.service.plccontrollersservice.SignalListService;
import com.vaadin.tutorial.crm.threads.FeederThread;
import com.vaadin.tutorial.crm.threads.UpdateValueController;
import com.vaadin.tutorial.crm.ui.chart.UpdateValueChart;
import com.vaadin.tutorial.crm.ui.chart.UpdateValueChartWashing;
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

    Chart chart = new Chart();
    Chart chartWashing = new Chart();
    public static List<DataFromPlc> arrayForChart = new ArrayList<>();
    private List<SignalList> controllerSignalList = new ArrayList<>();
    private List<SignalList> controllerSignalListWashing = new ArrayList<>();
    private static Configuration configuration;
    private static DataSeries series;
    private static Configuration configurationWashing;
    private static DataSeries seriesWashing;
    private static Thread uploadChart = new Thread();
    long controllerID = 4L;
    long controllerIDWashing = 1L;
    private Thread updateChart = new Thread();
    private Thread updateChartWashing = new Thread();
    String controllerIP;
    String controllerIPWashing;

    private SignalListService signalListService;
    private PlcControllersService plcControllersService;

    @Autowired
    public MainView(SignalListService signalListService, PlcControllersService plcControllersService) {
        this.signalListService = signalListService;
        this.plcControllersService = plcControllersService;

        labelUser.setText("");
        labelUser.getStyle().set("color", "red");
        labelUser.getStyle().set("font-weight", "bold");
        labelUser.getStyle().set("font-size", "11pt");
        labelUser.setSizeUndefined();
        labelUser.setVisible(false);

        controllerSignalList = signalListService.findSignalList(controllerID);
        controllerIP = plcControllersService.getAllByID(controllerID).get(0).getIp();

        controllerSignalListWashing = signalListService.findSignalList(controllerIDWashing);
        controllerIPWashing = plcControllersService.getAllByID(controllerIDWashing).get(0).getIp();

        hContent.add(initDemo(), initDemoWashing());

        vContent.add(hContent);//, initDemo()
        //vContent.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        vContent.setSizeFull();


        add(vContent);

    }

    public Component initDemo() {
        HorizontalLayout hChart = new HorizontalLayout();
        final Random random = new Random();
        if (PLCConnect.contrConnected) {
            chart.setWidth("500px");
            configuration = chart.getConfiguration();
            configuration.getChart().setType(ChartType.SPLINE);
            configuration.getTitle().setText("Live data - PLC Выпарка");

            XAxis xAxis = configuration.getxAxis();
            xAxis.setType(AxisType.DATETIME);
            xAxis.setTickPixelInterval(150);

            YAxis yAxis = configuration.getyAxis();
            yAxis.setTitle(new AxisTitle("Значения"));

            configuration.getTooltip().setEnabled(true);
            configuration.getLegend().setEnabled(true);

            series = new DataSeries();
            series.setPlotOptions(new PlotOptionsSpline());
            ///series.setName("Random data");
            for (int i = -19; i <= 0; i++) {
                series.add(new DataSeriesItem(System.currentTimeMillis() + i * 1000, i));
            }

            configuration.setSeries(series);

            hChart.add(chart);
            hChart.setWidth("405px");
            hChart.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            return new HorizontalLayout(hChart);
        }
        else {
            labelUser.setVisible(true);
            labelUser.setText("Контроллер " + controllerIP + " - " + plcControllersService.getAllByID(controllerID).get(0).getControllerName() + " не доступен!");
            return labelUser;
        }
    }

    public Component initDemoWashing() {
        HorizontalLayout hChart = new HorizontalLayout();
        final Random random = new Random();
        if (PLCConnect.contrConnectedWashing) {
            chartWashing.setWidth("500px");
            configurationWashing = chartWashing.getConfiguration();
            configurationWashing.getChart().setType(ChartType.SPLINE);
            configurationWashing.getTitle().setText("Live data - PLC Мойка");

            XAxis xAxis = configurationWashing.getxAxis();
            xAxis.setType(AxisType.DATETIME);
            xAxis.setTickPixelInterval(150);

            YAxis yAxis = configurationWashing.getyAxis();
            yAxis.setTitle(new AxisTitle("Значения"));

            configurationWashing.getTooltip().setEnabled(true);
            configurationWashing.getLegend().setEnabled(true);

            seriesWashing = new DataSeries();
            seriesWashing.setPlotOptions(new PlotOptionsSpline());
            ///series.setName("Random data");
            for (int i = -19; i <= 0; i++) {
                seriesWashing.add(new DataSeriesItem(System.currentTimeMillis() + i * 1000, i));
            }

            configurationWashing.setSeries(seriesWashing);

            hChart.add(chartWashing);
            hChart.setWidth("405px");
            hChart.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            return new HorizontalLayout(hChart);
        }
        else {
            labelUser.setVisible(true);
            labelUser.setText("Контроллер " + controllerIPWashing + " - " + plcControllersService.getAllByID(controllerIDWashing).get(0).getControllerName() + " не доступен!");
            return labelUser;
        }

    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {

        if (PLCConnect.contrConnected) {
            updateChart = new UpdateValueChart(attachEvent.getUI(), configuration, series, controllerSignalList, PLCConnect.clientForStatus);
            updateChart.start();
        }

        if (PLCConnect.contrConnectedWashing) {
            updateChartWashing = new UpdateValueChartWashing(attachEvent.getUI(), configurationWashing, seriesWashing, controllerSignalListWashing, PLCConnect.clientForStatusWashing);
            updateChartWashing.start();
        }
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {

        updateChart.interrupt();
        updateChart = null;

        updateChartWashing.interrupt();
        updateChartWashing = null;
    }
}