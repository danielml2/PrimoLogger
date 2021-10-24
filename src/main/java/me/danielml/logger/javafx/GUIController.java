package me.danielml.logger.javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import me.danielml.logger.Recording;
import me.danielml.logger.graph.ChartDataConverter;
import me.danielml.logger.graph.GraphableColumn;


import java.net.URL;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class GUIController implements Initializable {

    public GUIController() {}

    @FXML
    private LineChart mainChart;
    @FXML
    private ListView categoryList,valuesList;
    private Recording selectedRecording;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainChart.setTitle("Values over time");
        mainChart.getXAxis().setLabel("Time (in seconds)");

    }

    public void loadRecording(Recording recording) {
        this.selectedRecording = recording;
        ObservableList<String> list = FXCollections.observableList(recording.getCategories());
        categoryList.setItems(list);
        System.out.println("a");
        categoryList.setOnMouseClicked(event -> {
            if(event.getButton() == MouseButton.PRIMARY) {
                String category = categoryList.getSelectionModel().getSelectedItem().toString();
                ObservableList<String> values = FXCollections.observableList(recording.getGraphableColumns(category));
                valuesList.setItems(values);
                valuesList.setCellFactory((Callback<ListView, ListCell>) param -> new ValueCell(this,category));
            }
        });

    }

    public void addChartData(String category,String column) {
        ChartDataConverter chartConverter = new ChartDataConverter();
        Map<Double,Double> data = selectedRecording.loadData(category,column);

        mainChart.getData().add(chartConverter.convert(data,category + " " + column));
    }
    public void removeChartData(String category, String column) {
        mainChart.getData().removeIf((Predicate<XYChart.Series>) series -> series.getName().equals(category + " " + column));
    }

    public LineChart getMainChart() {
        return mainChart;
    }


}
