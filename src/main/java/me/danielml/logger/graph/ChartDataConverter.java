package me.danielml.logger.graph;

import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.HashMap;
import java.util.Map;

public class ChartDataConverter {

    public XYChart.Series<Number, Number> convert(Map<Double, Double> data, String categoryName) {

       XYChart.Series series = new XYChart.Series();

       for(Map.Entry<Double, Double> entry : data.entrySet()) {
         series.getData().add(new XYChart.Data(entry.getKey(),entry.getValue()));
       }

       series.setName(categoryName);
       return series;
    }
}