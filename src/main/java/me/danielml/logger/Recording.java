package me.danielml.logger;

import me.danielml.logger.graph.GraphableColumn;
import me.danielml.logger.sheets.CSVConverter;
import me.danielml.logger.sheets.SheetsReader;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;

public class Recording {

    private SheetsReader reader;
    private HashMap<String,Map<Double,Double>> loadedData;

    /**
     * Constructs recording from a Shuffleboard CSV Export file.
     * @param file - Shuffleboard CSV export file
     */
    public Recording(File file) {
        loadedData = new HashMap<>();
        try {
            CSVConverter.convertTOXLSX(file);
            reader = new SheetsReader(file.getName().substring(0,file.getName().length()-4));
            System.out.println("Loaded file");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads data from the Sheet file & Saves it onto the data map
     * @param category Value's category/Shuffleboard tab name (e.g. Driver, Shooter, Limelight)
     * @param column Value's category/Shuffleboard entry name (e.g. X position, setpoint, tx,ty)
     * @return Map containing the data, in a format of (Timestamp, Value)
     */
    public Map<Double, Double> loadData(String category, String column) {
        Map<Double,Double> map = reader.getDataFromSheet(category, column);
        if(map == null) System.out.println("Invalid category or column: " + category + " " + column);
        loadedData.put(category+"_"+column,map);
        return map;
    }

    public HashMap<String, Map<Double, Double>> getLoadedData() {
        return loadedData;
    }

    /**
     * Returns the loaded data by the recording, if it exists
     * @param mapName -
     * @return
     */
    public Map<Double,Double> getLoadedDataBy(String mapName) {
        return getLoadedDataBy(mapName.split("_")[0],mapName.split("_")[1]);
    }

    public Map<Double,Double> getLoadedDataBy(String category, String column) {
        return loadedData.get(category+"_"+column);
    }

    public List<String> getGraphableColumns(String category) {
        List<String> nameList = new ArrayList<>();
        reader.getGraphCategories().get(category).getSubColumns().forEach(graphableColumn -> nameList.add(graphableColumn.getColumnName()));
        return nameList;
    }

    public List<String> getCategories() {
        List<String> nameList = new ArrayList<>();
        return new ArrayList<>(reader.getGraphCategories().keySet());
    }

}
