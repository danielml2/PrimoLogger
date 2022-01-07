package me.danielml.logger.javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import me.danielml.logger.recordings.FileRecording;
import me.danielml.logger.graph.ChartDataConverter;
import me.danielml.logger.javafx.events.HoverEventListener;
import me.danielml.logger.recordings.Recording;


import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;

public class GUIController implements Initializable {

    public GUIController() {}

    @FXML private MenuItem fileChooser;
    @FXML private LineChart mainChart;
    @FXML private ListView tableList, entryList;
    @FXML private Label hoverText;

    private Recording selectedRecording; // Currently loaded recording
    private List<String> selectedEntries; // Currently shown entries on the graph

    // File directory for shuffleboard recordings
    private static final String RECORDINGS_PATH = System.getProperty("user.home") + File.separator + "Shuffleboard" + File.separator + "recordings";

    /**
     * JavaFX Controller's Init, gets called before the screen is shown
     */
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
            loadRecording(new FileRecording(new File(file.getPath())));
        });
        Node node = mainChart.lookup(".chart-plot-background");
        mainChart.setOnMouseMoved(new HoverEventListener(this,hoverText));
        node.setOnMouseMoved(new HoverEventListener(this,hoverText));
    }

    /**
     * Loads recording onto the GUI elements:
     * updates the category listview to show all available shuffleboard tabs that can be put on a numeric graph
     * adds the listener to the table listview to show the values that can be logged from the table selected
     * @param recording recording to load onto the GUI
     */
    public void loadRecording(FileRecording recording) {
        this.selectedRecording = recording;
        this.selectedEntries = new ArrayList<>();
        ObservableList<String> list = FXCollections.observableList(recording.getLoadedTableNames());
        tableList.getItems().clear();
        entryList.getItems().clear();
        mainChart.getData().clear();
        tableList.getItems().setAll(list);

        tableList.setOnMouseClicked(event -> {
            if(event.getButton() == MouseButton.PRIMARY) {
                if(tableList.getSelectionModel().isEmpty()) return;
                String tableName = tableList.getSelectionModel().getSelectedItem().toString();
                ObservableList<String> values = FXCollections.observableList(recording.getLoadedEntriesNames(tableName));
                entryList.setItems(values);
                entryList.setCellFactory((Callback<ListView, ListCell>) param -> new ValueCell(this,tableName));
                entryList.refresh();
            }
        });
        tableList.refresh();

    }

    /**
     * Adds the specified entry's values to the chart
     * @param tableName The entry's table it's apart from (e.g. Driver, Shooter, limelight)
     * @param entryName The entry's name (e.g. X position,setpoint,ty,tx);
     */
    public void addDataToChart(String tableName, String entryName) {
        if(selectedEntries.contains(tableName+"_"+entryName))
            return;
        ChartDataConverter chartConverter = new ChartDataConverter();
        Map<Double, Number> data = selectedRecording.getData(tableName,entryName);
        selectedEntries.add(tableName+"_"+entryName);

        mainChart.getData().add(chartConverter.convert(data,tableName + " " + entryName));
    }

    /**
     * Removes the specified entry's values from the chart
     * @param tableName The entry's table it's apart from (e.g. Driver, Shooter, limelight)
     * @param entryName The entry's name (e.g. X position,setpoint,ty,tx);
     */
    public void removeDataFromChart(String tableName, String entryName) {
        selectedEntries.remove(tableName+"_"+entryName);
        mainChart.getData().removeIf((Predicate<XYChart.Series>) series -> series.getName().equals(tableName + " " + entryName));
    }

    /**
     * Returns if the given value is selected/being shown on the chart
     * @param tableName The entry's table it's apart from (e.g. Driver, Shooter, limelight)
     * @param entryName The entry's name (e.g. X position,setpoint,ty,tx);
     * @return If the value is selected or not
     */
    public boolean isSelected(String tableName, String entryName) {
        return selectedEntries.contains(tableName+"_"+entryName);
    }

    public LineChart getMainChart() {
        return mainChart;
    }

    public List<String> getSelectedEntries() {
        return selectedEntries;
    }

    /**
     * Returns data from the recording if it already loaded it
     * @param mapName The table's name and the entry's name combined, the way it's saved onto the hashmap
     * @return Map containing the data loaded from the recording, (timestamp,value)
     */
    public Map<Double, Number> getLoadedDataByRecording(String mapName) {
        return selectedRecording.getData(mapName.split("_")[0],mapName.split("_")[1]);
    }
}
