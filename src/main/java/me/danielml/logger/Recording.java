package me.danielml.logger;

import me.danielml.logger.sheets.CSVConverter;
import me.danielml.logger.sheets.SheetsReader;

import java.util.HashMap;
import java.util.Map;

public class Recording {

    private SheetsReader reader;
    private HashMap<String,Map<Double,Double>> loadedData;

    public Recording(String fileName) {
        loadedData = new HashMap<>();
        try {
            CSVConverter.convertTOXLSX(fileName,fileName);
            reader = new SheetsReader(fileName);
            System.out.println("Loaded file");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<Double, Double> loadData(String category, String column) {
        Map<Double,Double> map = reader.getDataFromSheet(category, column);
        if(map == null) System.out.println("Invalid category or column: " + category + " " + column);
        loadedData.put(category+"_"+column,map);
        return map;
    }

    public HashMap<String, Map<Double, Double>> getLoadedData() {
        return loadedData;
    }

    public Map<Double,Double> getLoadedDataBy(String mapName) {
        return getLoadedDataBy(mapName.split("_")[0],mapName.split("_")[1]);
    }

    public Map<Double,Double> getLoadedDataBy(String category, String column) {
        return loadedData.get(category+"_"+column);
    }

}
