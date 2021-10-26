package me.danielml.logger.javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import me.danielml.logger.Recording;
import me.danielml.logger.graph.ChartDataConverter;
import me.danielml.logger.graph.GraphableColumn;


import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class GUIController implements Initializable {

    public GUIController() {}

    @FXML
    private MenuItem fileChooser;
    @FXML
    private LineChart mainChart;
    @FXML
    private ListView categoryList,valuesList;
    private Recording selectedRecording;
    private List<String> selectedColumns;

    private static final String RECORDINGS_PATH = System.getProperty("user.home") + File.separator + "Shuffleboard" + File.separator + "recordings";


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainChart.setTitle("Values over time");
        mainChart.getXAxis().setLabel("Time (in seconds)");
        fileChooser.setOnAction(event -> {
            FileChooser recordingSelector = new FileChooser();
            recordingSelector.setTitle("Open Recording CSV file");
            recordingSelector.setInitialDirectory(new File(RECORDINGS_PATH));
            recordingSelector.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV","*.csv"));
            File file = recordingSelector.showOpenDialog(new Stage());
            if(file == null) return;
            loadRecording(new Recording(new File(file.getPath())));
        });
    }

    public void loadRecording(Recording recording) {
        this.selectedRecording = recording;
        this.selectedColumns = new ArrayList<>();
        ObservableList<String> list = FXCollections.observableList(recording.getCategories());
        categoryList.getItems().clear();
        valuesList.getItems().clear();
        mainChart.getData().clear();
        categoryList.getItems().setAll(list);

        categoryList.setOnMouseClicked(event -> {
            if(event.getButton() == MouseButton.PRIMARY) {
                if(categoryList.getSelectionModel().isEmpty()) return;
                String category = categoryList.getSelectionModel().getSelectedItem().toString();
                ObservableList<String> values = FXCollections.observableList(recording.getGraphableColumns(category));
                valuesList.setItems(values);
                valuesList.setCellFactory((Callback<ListView, ListCell>) param -> new ValueCell(this,category));
                valuesList.refresh();
            }
        });
        categoryList.refresh();

    }

    public void addChartData(String category,String column) {
        if(selectedColumns.contains(category+"_"+column))
            return;
        ChartDataConverter chartConverter = new ChartDataConverter();
        Map<Double,Double> data = selectedRecording.loadData(category,column);
        selectedColumns.add(category+"_"+column);

        mainChart.getData().add(chartConverter.convert(data,category + " " + column));
    }
    public void removeChartData(String category, String column) {
        selectedColumns.remove(category+"_"+column);
        mainChart.getData().removeIf((Predicate<XYChart.Series>) series -> series.getName().equals(category + " " + column));
    }

    public boolean isSelected(String category, String column) {
        return selectedColumns.contains(category+"_"+column);
    }

    public LineChart getMainChart() {
        return mainChart;
    }


}
