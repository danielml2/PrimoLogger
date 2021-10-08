package me.danielml.logger.javafx;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.Map;
import java.util.SortedMap;

public class ChartDataConverter {

    public XYChart.Series<Number, Number> convert(SortedMap<Double, Double> data, String categoryName) {

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Time (in seconds)");
        NumberAxis yAxis = new NumberAxis();

       XYChart.Series series = new XYChart.Series();


       for(Map.Entry<Double, Double> entry : data.entrySet()) {
         series.getData().add(new XYChart.Data(entry.getKey(),entry.getValue()));
       }

       series.setName(categoryName);
       return series;
    }
}
