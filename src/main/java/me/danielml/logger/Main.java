package me.danielml.logger;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import me.danielml.logger.javafx.HoverEventListener;
import me.danielml.logger.javafx.ChartDataConverter;
import me.danielml.logger.sheets.CSVConverter;
import me.danielml.logger.sheets.SheetsReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

public class Main extends Application {

    private final List<Map<Double,Double>> currentData = new ArrayList<>();
    private final SheetsReader sheetsReader = new SheetsReader("xlsxrecording.xlsx");
    private LineChart chart;

    public Main() throws IOException { }

    @Override
    public void start(Stage primaryStage) throws Exception{
        CSVConverter converter = new CSVConverter();
        converter.convertTOXLSX("recording","xlsxrecording");


        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Time (in seconds)");
        NumberAxis yAxis = new NumberAxis();
        chart = new LineChart<Number,Number>(xAxis,yAxis);
        chart.setTitle("Values over time");

        addData("Driver","left position meters","Driver: left");
        addData("Driver","right position meters","Driver: right");
        addData("Driver","X position","Driver: X Position");
        addData("Driver","Y position","Driver: Y Position");
//        addData("limelight","tx","Limelight: X Angle");


        Node chartBackground = chart.lookup(".chart-plot-background");

        Label graphText = new Label("");

        chartBackground.setOnMouseMoved(new HoverEventListener(this,graphText));


        chart.setPadding(new Insets(10));

        primaryStage.setScene(new Scene(layout(chart,graphText), 800, 600));
        primaryStage.show();
    }


    public static void main(String[] args) throws Exception {

       launch(args);
    }



    private VBox layout(LineChart chart, Label graphText) {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));
        box.getChildren().setAll(chart,graphText);

        return box;
    }

    public LineChart getChart() {
        return chart;
    }

    public List<Map<Double, Double>> getCurrentData() {
        return currentData;
    }

    public void addData(String category, String columnName, String displayName) throws IOException {

        ChartDataConverter chartConverter = new ChartDataConverter();

        SortedMap<Double,Double> data = sheetsReader.getDataFromSheet(category,columnName);
        currentData.add(data);
        chart.getData().add(chartConverter.convert(data,displayName));
    }
}
