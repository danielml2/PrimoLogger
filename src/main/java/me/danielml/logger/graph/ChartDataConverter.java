package me.danielml.logger.graph;

import javafx.scene.chart.XYChart;

import java.util.Map;

public class ChartDataConverter {

    /**
     * Converts recording data to JavaFX chart data form
     * @param data Data loaded from recording of said value
     * @param displayName Display name for the graph in the GUI
     * @return JavaFX's Chart Data of the data from the recording.
     */
    public XYChart.Series<Number, Number> convert(Map<Double, Number> data, String displayName) {

       XYChart.Series series = new XYChart.Series();

       for(Map.Entry<Double, Number> entry : data.entrySet()) {
         series.getData().add(new XYChart.Data(entry.getKey(),entry.getValue()));
       }

       series.setName(displayName);
       return series;
    }
}
