package com.vaadin.tutorial.crm.ui.powerresources;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.entity.powerresources.PowerResourceDict;
import com.vaadin.tutorial.crm.entity.powerresources.PowerResources;
import com.vaadin.tutorial.crm.service.powerresources.PowerResourceDictService;
import com.vaadin.tutorial.crm.service.powerresources.PowerResourcesService;
import com.vaadin.tutorial.crm.ui.component.AnyComponent;
import com.vaadin.tutorial.crm.ui.layout.PowerLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс реализующий показ статистику энергоресурсов
 */
@Route(value = "statistics", layout = PowerLayout.class)
@PageTitle("Статистика энергоресурсов | Система управления производством")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class PowerStatistic extends VerticalLayout {
    VerticalLayout vMain = new VerticalLayout();
    HorizontalLayout hMain = new HorizontalLayout();
    HorizontalLayout hMain1 = new HorizontalLayout();
    List<PowerResourceDict> powerResourceDictList = new ArrayList<>();
    List<PowerResourceDict> powerResourceDictList1 = new ArrayList<>();
    List<PowerResourceDict> powerResourceDictList2 = new ArrayList<>();
    Button update = new Button("Обновить");
    Label labelChart1 = new Label();
    Label labelChart2 = new Label();
    Label labelChart3 = new Label();
    Label labelGas = new Label();
    private final PowerResourcesService powerResourcesService;
    private final PowerResourceDictService powerResourceDictService;

    public PowerStatistic(PowerResourcesService powerResourcesService, PowerResourceDictService powerResourceDictService) {
        this.powerResourcesService = powerResourcesService;
        this.powerResourceDictService = powerResourceDictService;

        labelChart1.setVisible(false);
        labelChart2.setVisible(false);
        labelGas.setVisible(false);

        labelChart1.setText("");
        labelChart1.getStyle().set("color", "red");
        labelChart1.getStyle().set("font-weight", "bold");
        labelChart1.getStyle().set("font-size", "11pt");
        labelChart1.setSizeUndefined();
        labelChart1.setVisible(false);

        labelChart2.setText("");
        labelChart2.getStyle().set("color", "red");
        labelChart2.getStyle().set("font-weight", "bold");
        labelChart2.getStyle().set("font-size", "11pt");
        labelChart2.setSizeUndefined();
        labelChart2.setVisible(false);

        labelChart3.setText("");
        labelChart3.getStyle().set("color", "red");
        labelChart3.getStyle().set("font-weight", "bold");
        labelChart3.getStyle().set("font-size", "11pt");
        labelChart3.setSizeUndefined();
        labelChart3.setVisible(false);

        labelGas.setText("");
        labelGas.getStyle().set("color", "red");
        labelGas.getStyle().set("font-weight", "bold");
        labelGas.getStyle().set("font-size", "11pt");
        labelGas.setSizeUndefined();
        labelGas.setVisible(false);

        hMain.add(initChartGas(), initChart());
        hMain.setVerticalComponentAlignment(Alignment.CENTER);

        hMain1.add(initChartElectric(), initChartStock());
        hMain1.setVerticalComponentAlignment(Alignment.CENTER);

        vMain.add(new AnyComponent().labelTitle("Статистика по показаниям энергоресурсов"), hMain, hMain1, update);
        vMain.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(vMain);

        Icon icon1 = new Icon(VaadinIcon.REFRESH);
        update.setIcon(icon1);
        update.getStyle().set("background-color", "#d3b342");
        update.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        update.addClickListener(e -> {
            UI.getCurrent().getPage().reload();
        });
    }

    private Component initChartGas() {
        final Chart chart = new Chart();
        powerResourceDictList = powerResourceDictService.getAll();
        chart.setWidth("777px");
        if (powerResourcesService.getAll().size() != 0) {
            Configuration configuration = chart.getConfiguration();
            //configuration.getChart().setType(ChartType.LINE);
            configuration.getTitle().setText("Газ (ежедневно)");

            YAxis yAxis = new YAxis();
            Labels label = new Labels();
            label.setFormatter("function() { return (this.value > 0 ? ' + ' : '') + this.value; }"); //"function() { return (this.value > 0 ? ' + ' : '') + this.value + '%'; }"
            yAxis.setLabels(label);
            yAxis.setTitle("Значение");

            XAxis xAxis = configuration.getxAxis();
            xAxis.setType(AxisType.DATETIME);
            xAxis.setTickPixelInterval(150);

            PlotLine plotLine = new PlotLine();
            plotLine.setValue(2);
            yAxis.setPlotLines(plotLine);
            configuration.addyAxis(yAxis);

            Tooltip tooltip = new Tooltip();
            tooltip.setPointFormat("<span>{series.name}</span>: <b>{point.y}</b><br/>"); //"<span>{series.name}</span>: <b>{point.y}</b> ({point.change}%)<br/>"
            tooltip.setValueDecimals(2);
            configuration.setTooltip(tooltip);

            chart.setTimeline(true);

            DataSeries[] waterWellSeries = new DataSeries[100];
            DataSeriesItem[] item = new DataSeriesItem[100];

            for (int i = 0; i < powerResourceDictList.size(); i++) {
                List<PowerResources> powerResourcesList = powerResourcesService.getAllByResourceId(powerResourceDictList.get(i).getId());
                waterWellSeries[i] = new DataSeries();
                waterWellSeries[i].setName(powerResourceDictList.get(i).getResourceName());
                for (int j = 0; j < powerResourcesList.size(); j++) {
                    item[i] = new DataSeriesItem();
                    item[i].setX(powerResourcesList.get(j).getDateCreate().toInstant());
                    item[i].setY(powerResourcesList.get(j).getValue());
                    waterWellSeries[i].add(item[i]); //new DataSeriesItem(powerResourcesList.get(j).getDateCreate(), powerResourcesList.get(j).getValueDaily())
                }
            }
            configuration.setSeries(waterWellSeries[1]);

            RangeSelector rangeSelector = new RangeSelector();
            rangeSelector.setSelected(4);
            configuration.setRangeSelector(rangeSelector);

            return chart;
        }
        else {
            labelGas.setVisible(true);
            labelGas.setText("Нет данных для отображения!");
            return labelGas;
        }
    }

    private Component initChart() {
        final Chart chart1 = new Chart();
        powerResourceDictList1 = powerResourceDictService.getAll();
        chart1.setWidth("777px");
        if (powerResourcesService.getAll().size() != 0) {
            chart1.setTimeline(true);

            Configuration configuration1 = chart1.getConfiguration();
            //configuration.getChart().setType(ChartType.LINE);
            configuration1.getTitle().setText("Вода, газ (еженедельно)");

            YAxis yAxis1 = new YAxis();
            Labels label1 = new Labels();
            label1.setFormatter("function() { return (this.value > 0 ? ' + ' : '') + this.value; }"); //"function() { return (this.value > 0 ? ' + ' : '') + this.value + '%'; }"
            yAxis1.setLabels(label1);
            yAxis1.setTitle("Значение");

            //XAxis xAxis = configuration.getxAxis();
            //xAxis.setType(AxisType.DATETIME);
            //xAxis.setTickPixelInterval(150);

            PlotLine plotLine1 = new PlotLine();
            plotLine1.setValue(2);
            yAxis1.setPlotLines(plotLine1);
            configuration1.addyAxis(yAxis1);

            Tooltip tooltip1 = new Tooltip();
            tooltip1.setPointFormat("<span>{series.name}</span>: <b>{point.y}</b><br/>"); //"<span>{series.name}</span>: <b>{point.y}</b> ({point.change}%)<br/>"
            tooltip1.setValueDecimals(2);
            configuration1.setTooltip(tooltip1);
            DataSeries[] waterWellSeries = new DataSeries[100];
            DataSeriesItem[] item = new DataSeriesItem[100];

            for (int i = 0; i < powerResourceDictList.size(); i++) {
                List<PowerResources> powerResourcesList = powerResourcesService.getAllByResourceId(powerResourceDictList.get(i).getId());
                waterWellSeries[i] = new DataSeries();
                waterWellSeries[i].setName(powerResourceDictList.get(i).getResourceName());
                for (int j = 0; j < powerResourcesList.size(); j++) {
                    item[i] = new DataSeriesItem();
                    item[i].setX(powerResourcesList.get(j).getDateCreate().toInstant());
                    item[i].setY(powerResourcesList.get(j).getValueWeekly());
                    waterWellSeries[i].add(item[i]); //new DataSeriesItem(powerResourcesList.get(j).getDateCreate().toInstant(), powerResourcesList.get(j).getValueWeekly())
                }
            }
            configuration1.setSeries(waterWellSeries[0], waterWellSeries[1]); //, waterWellSeries[1]
            //PlotOptionsSeries plotOptionsSeries = new PlotOptionsSeries();
            //plotOptionsSeries.setCompare(Compare.PERCENT);
            //configuration.setPlotOptions(plotOptionsSeries);

            RangeSelector rangeSelector1 = new RangeSelector();
            rangeSelector1.setSelected(4);
            configuration1.setRangeSelector(rangeSelector1);

            return chart1;
        }
        else {
            labelChart1.setVisible(true);
            labelChart1.setText("Нет данных для отображения!");
            return labelChart1;
        }
    }

    private Component initChartElectric() {
        final Chart chartElectric = new Chart();
        powerResourceDictList2 = powerResourceDictService.getAll();
        chartElectric.setWidth("777px");
        if (powerResourcesService.getAll().size() != 0) {
            chartElectric.setTimeline(true);

            Configuration configuration2 = chartElectric.getConfiguration();
            //configuration.getChart().setType(ChartType.LINE);
            configuration2.getTitle().setText("Электроэнергия (еженедельно)");

            YAxis yAxisElectric = new YAxis();
            Labels label2 = new Labels();
            label2.setFormatter("function() { return (this.value > 0 ? ' + ' : '') + this.value; }"); //"function() { return (this.value > 0 ? ' + ' : '') + this.value + '%'; }"
            yAxisElectric.setLabels(label2);
            yAxisElectric.setTitle("Значение");

            //XAxis xAxis = configuration.getxAxis();
            //xAxis.setType(AxisType.DATETIME);
            //xAxis.setTickPixelInterval(150);

            PlotLine plotLine2 = new PlotLine();
            plotLine2.setValue(2);
            yAxisElectric.setPlotLines(plotLine2);
            configuration2.addyAxis(yAxisElectric);

            Tooltip tooltip2 = new Tooltip();
            tooltip2.setPointFormat("<span>{series.name}</span>: <b>{point.y}</b><br/>"); //"<span>{series.name}</span>: <b>{point.y}</b> ({point.change}%)<br/>"
            tooltip2.setValueDecimals(2);
            configuration2.setTooltip(tooltip2);

        /*DataSeries waterWellSeries = new DataSeries();
        waterWellSeries.setName("Вода из скважины");
        for (int i = 0; i < powerResourcesList.size(); i++) {
            DataSeriesItem item = new DataSeriesItem();
            item.setX(powerResourcesList.get(i).getDateCreate());
            item.setY(powerResourcesList.get(i).getValue());
            waterWellSeries.add(item);
        }*/

            DataSeries[] waterWellSeries = new DataSeries[100];
            DataSeriesItem[] item = new DataSeriesItem[100];

            for (int i = 0; i < powerResourceDictList.size(); i++) {
                List<PowerResources> powerResourcesList = powerResourcesService.getAllByResourceId(powerResourceDictList.get(i).getId());
                waterWellSeries[i] = new DataSeries();
                waterWellSeries[i].setName(powerResourceDictList.get(i).getResourceName());
                for (int j = 0; j < powerResourcesList.size(); j++) {
                    item[i] = new DataSeriesItem();
                    item[i].setX(powerResourcesList.get(j).getDateCreate());
                    item[i].setY(powerResourcesList.get(j).getValueWeekly());
                    waterWellSeries[i].add(item[i]);
                }
            }
            configuration2.setSeries(waterWellSeries[2], waterWellSeries[3], waterWellSeries[4]); //waterWellSeries[6], waterWellSeries[7],waterWellSeries[8],

            RangeSelector rangeSelector2 = new RangeSelector();
            rangeSelector2.setSelected(4);
            configuration2.setRangeSelector(rangeSelector2);

            return chartElectric;
        }
        else {
            labelChart2.setVisible(true);
            labelChart2.setText("Нет данных для отображения!");
            return labelChart2;
        }
    }

    private Component initChartStock() {
        final Chart chartStock = new Chart();
        powerResourceDictList2 = powerResourceDictService.getAll();
        chartStock.setWidth("777px");
        if (powerResourcesService.getAll().size() != 0) {
            chartStock.setTimeline(true);

            Configuration configuration2 = chartStock.getConfiguration();
            //configuration.getChart().setType(ChartType.LINE);
            configuration2.getTitle().setText("Стоки (еженедельно)");

            YAxis yAxisStock = new YAxis();
            Labels label2 = new Labels();
            label2.setFormatter("function() { return (this.value > 0 ? ' + ' : '') + this.value; }"); //"function() { return (this.value > 0 ? ' + ' : '') + this.value + '%'; }"
            yAxisStock.setLabels(label2);
            yAxisStock.setTitle("Значение");

            //XAxis xAxis = configuration.getxAxis();
            //xAxis.setType(AxisType.DATETIME);
            //xAxis.setTickPixelInterval(150);

            PlotLine plotLine2 = new PlotLine();
            plotLine2.setValue(2);
            yAxisStock.setPlotLines(plotLine2);
            configuration2.addyAxis(yAxisStock);

            Tooltip tooltip2 = new Tooltip();
            tooltip2.setPointFormat("<span>{series.name}</span>: <b>{point.y}</b><br/>"); //"<span>{series.name}</span>: <b>{point.y}</b> ({point.change}%)<br/>"
            tooltip2.setValueDecimals(2);
            configuration2.setTooltip(tooltip2);

        /*DataSeries waterWellSeries = new DataSeries();
        waterWellSeries.setName("Вода из скважины");
        for (int i = 0; i < powerResourcesList.size(); i++) {
            DataSeriesItem item = new DataSeriesItem();
            item.setX(powerResourcesList.get(i).getDateCreate());
            item.setY(powerResourcesList.get(i).getValue());
            waterWellSeries.add(item);
        }*/

            DataSeries[] waterWellSeries = new DataSeries[100];
            DataSeriesItem[] item = new DataSeriesItem[100];

            for (int i = 0; i < powerResourceDictList.size(); i++) {
                List<PowerResources> powerResourcesList = powerResourcesService.getAllByResourceId(powerResourceDictList.get(i).getId());
                waterWellSeries[i] = new DataSeries();
                waterWellSeries[i].setName(powerResourceDictList.get(i).getResourceName());
                for (int j = 0; j < powerResourcesList.size(); j++) {
                    item[i] = new DataSeriesItem();
                    item[i].setX(powerResourcesList.get(j).getDateCreate());
                    item[i].setY(powerResourcesList.get(j).getValueWeekly());
                    waterWellSeries[i].add(item[i]);
                }
            }
            configuration2.setSeries(waterWellSeries[5]); //waterWellSeries[6], waterWellSeries[7],waterWellSeries[8],

            RangeSelector rangeSelector2 = new RangeSelector();
            rangeSelector2.setSelected(4);
            configuration2.setRangeSelector(rangeSelector2);

            return chartStock;
        } else {
            labelChart3.setVisible(true);
            labelChart3.setText("Нет данных для отображения!");
            return labelChart3;
        }
    }

}
