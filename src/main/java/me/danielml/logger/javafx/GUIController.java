package me.danielml.logger.javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import me.danielml.logger.networktables.UpdateLog;
import me.danielml.logger.recordings.FileRecording;
import me.danielml.logger.graph.ChartDataConverter;
import me.danielml.logger.javafx.events.HoverEventListener;
import me.danielml.logger.recordings.NetworkRecording;
import me.danielml.logger.recordings.Recording;
import me.danielml.logger.recordings.filerecording.FileRecordingType;


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
    @FXML private TextField trueNumericBox;
    @FXML private TextField falseNumericBox;
    @FXML private Button booleanUpdate;
    @FXML private Button loadFile; // alternate file uploading button

    // i hate having to do this but i am too lazy to think of a proper solution for now
    private double booleanTrueNumeric = 1;
    private double booleanFalseNumeric = -1;
    private List<String> booleanEntries;

    private Recording selectedRecording; // Currently loaded recording
    private List<String> selectedEntries; // Currently shown entries on the graph

    private boolean isNetworkLogging = false;

    private static GUIController instance;
    private Stage stage;

    public static GUIController getInstance() {
        return instance;
    }

    // File directory for shuffleboard recordings
    private static final String RECORDINGS_PATH = System.getProperty("user.home");

    /**
     * JavaFX Controller's Init, gets called before the screen is shown
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        booleanEntries = new ArrayList<>();
        if(instance == null)
            instance = this;
        mainChart.setTitle("Values over time");
        NumberAxis xAxis = (NumberAxis)mainChart.getXAxis();
        xAxis.setLabel("Time (in seconds)");
        xAxis.setAutoRanging(true);
        xAxis.setForceZeroInRange(true);
        NumberAxis yAxis = (NumberAxis)mainChart.getYAxis();
        yAxis.setAutoRanging(true);


        EventHandler<ActionEvent> fileLoader = event -> {
            FileChooser recordingSelector = new FileChooser();
            recordingSelector.setTitle("Open Recording CSV file");
            recordingSelector.setInitialDirectory(new File(RECORDINGS_PATH));
            recordingSelector.getExtensionFilters().add(new FileChooser.ExtensionFilter("WPI Data Logging CSV","*.csv"));
            recordingSelector.getExtensionFilters().add(new FileChooser.ExtensionFilter("Shuffleboard CSV","*.csv"));

            File file = recordingSelector.showOpenDialog(new Stage());
            if(file == null) return;
            if(recordingSelector.getSelectedExtensionFilter().getDescription().contains("WPI Data Logging CSV"))
                loadRecording(new FileRecording(new File(file.getPath()), FileRecordingType.WPI_CSV));
            else
                loadRecording(new FileRecording(new File(file.getPath())));
        };

       fileChooser.setOnAction(fileLoader);
       loadFile.setOnAction(fileLoader);

        mainChart.setOnMouseMoved(new HoverEventListener(this,hoverText));
//        node.setOnMouseMoved(new HoverEventListener(this,hoverText));

        tableList.setOnMouseClicked(event -> {
            if(event.getButton() == MouseButton.PRIMARY) {
                if(tableList.getSelectionModel().isEmpty()) return;
                String tableName = tableList.getSelectionModel().getSelectedItem().toString();
                ObservableList<String> values = FXCollections.observableList(this.selectedRecording.getLoadedEntriesNames(tableName));
                entryList.setItems(values);
                entryList.setCellFactory((Callback<ListView, ListCell>) param -> new ValueCell(this,tableName));
                entryList.refresh();
            }
        });

        booleanUpdate.setOnAction(event -> {

            if(trueNumericBox.getText().isEmpty() && falseNumericBox.getText().isEmpty())
                return;
            try {
                booleanTrueNumeric = Integer.parseInt(trueNumericBox.getText());
                booleanFalseNumeric = Integer.parseInt(falseNumericBox.getText());
            } catch (NumberFormatException ignored) {}
        });


    }

    /**
     * Loads recording onto the GUI elements:
     * updates the category listview to show all available shuffleboard tabs that can be put on a numeric graph
     * adds the listener to the table listview to show the values that can be logged from the table selected
     * @param recording recording to load onto the GUI
     */
    public void loadRecording(Recording recording) {
        this.selectedRecording = recording;
        this.selectedEntries = new ArrayList<>();
        ObservableList<String> list = FXCollections.observableList(recording.getLoadedTableNames());
        mainChart.getData().clear();
        updateGUIViews();
    }

    /**
     * Updates the ListViews in the GUI to the given recording data
     * mostly used to refresh GUI to new data coming from {@link NetworkRecording}
     */
    public void updateGUIViews() {
        ObservableList<String> list = FXCollections.observableList(this.selectedRecording.getLoadedTableNames());
        tableList.getItems().clear();
        entryList.getItems().clear();
        tableList.getItems().setAll(list);
        tableList.refresh();
    }

    /**
     * Adds the specified entry's values to the chart
     * @param tableName The entry's table it's apart from (e.g. Driver, Shooter, limelight)
     * @param entryName The entry's name (e.g. X position,setpoint,ty,tx);
     */
    public void addDataToChart(String tableName, String entryName) {
        if(selectedEntries.contains(tableName + Recording.TABLE_ENTRY_SEPARATOR + entryName))
            return;
        ChartDataConverter chartConverter = new ChartDataConverter();
        Map<Double, Number> data = selectedRecording.getData(tableName,entryName);

        selectedEntries.add(tableName + Recording.TABLE_ENTRY_SEPARATOR + entryName);

        mainChart.getData().add(chartConverter.convert(data,tableName + " " + entryName));
    }

    /**
     * Updates the entries' values to the chart
     * @see me.danielml.logger.recordings.NetworkRecording
     * @param updates New changes that happened to the entry values.
     */
    public void updateChartData(List<UpdateLog> updates) {
        System.out.println("Attempting to update chart data...");
        if(selectedEntries != null)
        for(String entryMapName : selectedEntries) {
            System.out.println("Updating chart for " + entryMapName);
            XYChart.Series<Double,Number> chartSeries = getChartSeries(entryMapName.replace("_"," "));
            if(chartSeries == null)
                continue;

            for(UpdateLog log: updates) {
                if(log.getMapName().equals(entryMapName)) {

                    int lastIndex = chartSeries.getData().size() - 1;
                    if(lastIndex != -1) {
                        XYChart.Data<Double, Number> lastDataPoint = chartSeries.getData().get(lastIndex);

                        if (lastDataPoint.getYValue().equals(log.getValue()))
                            chartSeries.getData().remove(lastIndex);
                    }
                    chartSeries.getData().add(new XYChart.Data<>(log.getTimestamp(), log.getValue()));
                }
            }
            System.out.println("Finished updating for " + entryMapName);
            mainChart.autosize();
        }

    }

    /**
     * Gets the Chart series for the specified chart requested
     * @param chartName Name of the chart in the graph. (Driver X, Driver Y)
     * NOTE: This name is not the same as the map name from getLoadedDataByRecording()
     */
    public XYChart.Series<Double,Number> getChartSeries(String chartName) {
        for(Object chartObj : mainChart.getData()) {
            XYChart.Series<Double,Number> series = (XYChart.Series<Double,Number>)chartObj;

            if(series.getName().equals(chartName))
                return series;
        }
        return null;
    }

    /**
     * Removes the specified entry's values from the chart
     * @param tableName The entry's table it's apart from (e.g. Driver, Shooter, limelight)
     * @param entryName The entry's name (e.g. X position,setpoint,ty,tx);
     */
    public void removeDataFromChart(String tableName, String entryName) {
        selectedEntries.remove(tableName+ Recording.TABLE_ENTRY_SEPARATOR + entryName);
        mainChart.getData().removeIf((Predicate<XYChart.Series>) series -> series.getName().equals(tableName + " " + entryName));
    }

    /**
     * Returns if the given value is selected/being shown on the chart
     * @param tableName The entry's table it's apart from (e.g. Driver, Shooter, limelight)
     * @param entryName The entry's name (e.g. X position,setpoint,ty,tx);
     * @return If the value is selected or not
     */
    public boolean isSelected(String tableName, String entryName) {
        return selectedEntries.contains(tableName+ Recording.TABLE_ENTRY_SEPARATOR + entryName);
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
        return selectedRecording.getData(mapName.split(Recording.TABLE_ENTRY_SEPARATOR)[0],mapName.split(Recording.TABLE_ENTRY_SEPARATOR)[1]);
    }

    public boolean isNetworkLogging() {
        return isNetworkLogging;
    }

    public double getBooleanFalseNumeric() {
        return booleanFalseNumeric;
    }

    public double getBooleanTrueNumeric() {
        return booleanTrueNumeric;
    }

    public List<String> getBooleanEntries() {
        return booleanEntries;
    }
}
