package me.danielml.logger;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import me.danielml.logger.javafx.ChartDataConverter;
import me.danielml.logger.javafx.HoverEventListener;

import java.util.ArrayList;
import java.util.Map;

public class Main extends Application {

    private Recording recording;
    private ArrayList<String> selectedCategories;
    private LineChart chart;

    @Override
    public void start(Stage primaryStage) throws Exception{

        recording = new Recording("recording");

        selectedCategories = new ArrayList<>();

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Time (in seconds)");
        NumberAxis yAxis = new NumberAxis();
        chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Values over time");


        addData("Driver","left position meters","Driver: left");
        addData("Driver","right position meters","Driver: right");
        addData("Driver","X position","Driver: X Position");
        addData("Driver","Y position","Driver: Y Position");

        Label graphText = new Label("");
        chart.setOnMouseMoved(new HoverEventListener(this,graphText));
        chart.setPadding(new Insets(10));

        primaryStage.setScene(new Scene(layout(chart,graphText), 800, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {
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

    public Recording getRecording() {
        return recording;
    }

    public void addData(String category, String columnName, String displayName) {
        ChartDataConverter chartConverter = new ChartDataConverter();
        Map<Double,Double> data = recording.loadData(category,columnName);

        selectedCategories.add(category+"_"+columnName);
        chart.getData().add(chartConverter.convert(data,displayName));
    }

    public ArrayList<String> getSelectedCategories() {
        return selectedCategories;
    }
}
