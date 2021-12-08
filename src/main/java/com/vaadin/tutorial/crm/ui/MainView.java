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
    long controllerID = 4L;
    private Thread updateChart = new Thread();
    String controllerIP;

    private SignalListService signalListService;
    private PlcControllersService plcControllersService;

    @Autowired
    public MainView(SignalListService signalListService, PlcControllersService plcControllersService) {
        this.signalListService = signalListService;
        this.plcControllersService = plcControllersService;

        //labelUser.setText("Идет подключение к ПЛК...");
        //labelUser.getStyle().set("color", "green");
        //labelUser.getStyle().set("font-weight", "bold");
        //labelUser.getStyle().set("font-size", "11pt");

        controllerSignalList = signalListService.findSignalList(controllerID);
        controllerIP = plcControllersService.getAllByID(controllerID).get(0).getIp();

        configuration = chart.getConfiguration();
        configuration.getChart().setType(ChartType.SPLINE);
        configuration.getTitle().setText("Live data");

        //hContent.add(labelUser);

        vContent.add(initDemo());//, initDemo()
        //vContent.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        vContent.setSizeFull();


        add(vContent);

    }

    public Component initDemo() {

        final Random random = new Random();
        //if (PLCConnect.controllerStatus(controllerIP)) {
            chart.setWidth("800px");
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
            series.setName("Random data");
            for (int i = -19; i <= 0; i++) {
                series.add(new DataSeriesItem(System.currentTimeMillis() + i * 1000, i));
            }

            configuration.setSeries(series);
        //}
        return chart;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        //thread = new FeederThread(attachEvent.getUI(), labelUser);
        //thread.start();

        updateChart = new UpdateValueChart(attachEvent.getUI(), configuration, series, controllerSignalList, PLCConnect.clientForStatus);
        updateChart.start();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {

        //PLCConnect.controllerDisconnect();

        //thread.interrupt();
        //thread = null;

        updateChart.interrupt();
        updateChart = null;
    }
}