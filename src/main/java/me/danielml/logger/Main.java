package me.danielml.logger;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import me.danielml.logger.graph.ChartDataConverter;
import me.danielml.logger.javafx.GUIController;

import java.util.ArrayList;
import java.util.Map;

public class Main extends Application {

    private Recording recording;
    private ArrayList<String> selectedCategories;
    private GUIController controller;


    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("logger.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        Scene scene = new Scene(root,800,700);


        recording = new Recording("recording");
        controller.loadRecording(recording);
        selectedCategories = new ArrayList<>();


//        addData("Driver","left position meters","Driver: left");
//        addData("Driver","right position meters","Driver: right");
//        addData("Driver","X position","Driver: X Position");
//        addData("Driver","Y position","Driver: Y Position");

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
       launch(args);
    }

    public LineChart getMainChart() {
        return controller.getMainChart();
    }

    public Recording getRecording() {
        return recording;
    }


    public ArrayList<String> getSelectedCategories() {
        return selectedCategories;
    }
}
