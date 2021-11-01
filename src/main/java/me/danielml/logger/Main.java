package me.danielml.logger;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.danielml.logger.javafx.GUIController;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Main extends Application {

    private GUIController controller;


    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("logger.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        Scene scene = new Scene(root,800,700);

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
       launch(args);
    }

    public LineChart getMainChart() {
        return controller.getMainChart();
    }

}
