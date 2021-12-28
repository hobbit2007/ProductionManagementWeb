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
    List<PowerResourceDict> powerResourceDictList = new ArrayList<>();
    Button update = new Button("Обновить");
    Label labelChart1 = new Label();
    Label labelChart2 = new Label();
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

        vMain.add(new AnyComponent().labelTitle("Статистика по показаниям энергоресурсов"), initChart(), initChartElectric(), update);
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

        if (powerResourcesService.getAll().size() != 0) {
            chart.setTimeline(true);

            Configuration configuration = chart.getConfiguration();
            configuration.getTitle().setText("Газ (ежедневно)");

            YAxis yAxis = new YAxis();
            Labels label = new Labels();
            label.setFormatter("function() { return (this.value > 0 ? ' + ' : '') + this.value + '%'; }");
            yAxis.setLabels(label);

            PlotLine plotLine = new PlotLine();
            plotLine.setValue(2);
            yAxis.setPlotLines(plotLine);
            configuration.addyAxis(yAxis);

            Tooltip tooltip = new Tooltip();
            tooltip.setPointFormat("<span>{series.name}</span>: <b>{point.y}</b> ({point.change}%)<br/>");
            tooltip.setValueDecimals(2);
            configuration.setTooltip(tooltip);

            DataSeries[] waterWellSeries = new DataSeries[100];
            DataSeriesItem[] item = new DataSeriesItem[100];

            for (int i = 0; i < powerResourceDictList.size(); i++) {
                List<PowerResources> powerResourcesList = powerResourcesService.getAllByResourceId(powerResourceDictList.get(i).getId());
                waterWellSeries[i] = new DataSeries();
                waterWellSeries[i].setName(powerResourceDictList.get(i).getResourceName());
                for (int j = 0; j < powerResourcesList.size(); j++) {
                    item[i] = new DataSeriesItem();
                    item[i].setX(powerResourcesList.get(j).getDateCreate());
                    item[i].setY(powerResourcesList.get(j).getValueDaily());
                    waterWellSeries[i].add(item[i]);
                }
            }
            configuration.setSeries(waterWellSeries[1]);
            PlotOptionsSeries plotOptionsSeries = new PlotOptionsSeries();
            plotOptionsSeries.setCompare(Compare.PERCENT);
            configuration.setPlotOptions(plotOptionsSeries);

            RangeSelector rangeSelector = new RangeSelector();
            rangeSelector.setSelected(9);
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
        final Chart chart = new Chart();
        powerResourceDictList = powerResourceDictService.getAll();

        if (powerResourcesService.getAll().size() != 0) {
            chart.setTimeline(true);

            Configuration configuration = chart.getConfiguration();
            configuration.getTitle().setText("Вода, газ (еженедельно)");

            YAxis yAxis = new YAxis();
            Labels label = new Labels();
            label.setFormatter("function() { return (this.value > 0 ? ' + ' : '') + this.value + '%'; }");
            yAxis.setLabels(label);

            PlotLine plotLine = new PlotLine();
            plotLine.setValue(2);
            yAxis.setPlotLines(plotLine);
            configuration.addyAxis(yAxis);

            Tooltip tooltip = new Tooltip();
            tooltip.setPointFormat("<span>{series.name}</span>: <b>{point.y}</b> ({point.change}%)<br/>");
            tooltip.setValueDecimals(2);
            configuration.setTooltip(tooltip);
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
            configuration.setSeries(waterWellSeries[0], waterWellSeries[1]);
            PlotOptionsSeries plotOptionsSeries = new PlotOptionsSeries();
            plotOptionsSeries.setCompare(Compare.PERCENT);
            configuration.setPlotOptions(plotOptionsSeries);

            RangeSelector rangeSelector = new RangeSelector();
            rangeSelector.setSelected(9);
            configuration.setRangeSelector(rangeSelector);

            return chart;
        }
        else {
            labelChart1.setVisible(true);
            labelChart1.setText("Нет данных для отображения!");
            return labelChart1;
        }
    }

    private Component initChartElectric() {
        final Chart chartElectric = new Chart();
        powerResourceDictList = powerResourceDictService.getAll();
        if (powerResourcesService.getAll().size() != 0) {
            chartElectric.setTimeline(true);

            Configuration configuration = chartElectric.getConfiguration();
            configuration.getTitle().setText("Электроэнергия (еженедельно)");

            YAxis yAxisElectric = new YAxis();
            Labels label = new Labels();
            label.setFormatter("function() { return (this.value > 0 ? ' + ' : '') + this.value + '%'; }");
            yAxisElectric.setLabels(label);

            PlotLine plotLine = new PlotLine();
            plotLine.setValue(2);
            yAxisElectric.setPlotLines(plotLine);
            configuration.addyAxis(yAxisElectric);

            Tooltip tooltip = new Tooltip();
            tooltip.setPointFormat("<span>{series.name}</span>: <b>{point.y}</b> ({point.change}%)<br/>");
            tooltip.setValueDecimals(2);
            configuration.setTooltip(tooltip);

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
                    item[i].setY(powerResourcesList.get(j).getValue());
                    waterWellSeries[i].add(item[i]);
                }
            }
            configuration.setSeries(waterWellSeries[2], waterWellSeries[3], waterWellSeries[4]); //waterWellSeries[6], waterWellSeries[7],waterWellSeries[8],
            PlotOptionsSeries plotOptionsSeries = new PlotOptionsSeries();
            plotOptionsSeries.setCompare(Compare.PERCENT);
            configuration.setPlotOptions(plotOptionsSeries);

            RangeSelector rangeSelector = new RangeSelector();
            rangeSelector.setSelected(9);
            configuration.setRangeSelector(rangeSelector);

            return chartElectric;
        }
        else {
            labelChart2.setVisible(true);
            labelChart2.setText("Нет данных для отображения!");
            return labelChart2;
        }
    }

}
