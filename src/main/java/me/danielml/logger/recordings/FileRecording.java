package me.danielml.logger.recordings;

import me.danielml.logger.sheets.CSVConverter;
import me.danielml.logger.sheets.SheetsReader;

import java.io.File;
import java.util.*;

/**
 * A Recording loaded from Shuffleboard CSV export files, that all its values are STATIC.
 */
public class FileRecording implements Recording{

    private SheetsReader reader;
    private HashMap<String,Map<Double,Number>> loadedData;
    private String fileName;

    /**
     * Constructs a recording from a Shuffleboard CSV Export file.
     * @param file - Shuffleboard CSV export file
     */
    public FileRecording(File file) {
        loadedData = new HashMap<>();
        this.fileName = file.getName();
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
    public Map<Double, Number> loadData(String category, String column) {
        Map<Double,Number> map = reader.getDataFromSheet(category, column);
        if(map == null) System.out.println("Invalid category or column: " + category + " " + column);
        loadedData.put(category+"_"+column,map);
        return map;
    }

    @Override
    public Map<Double, Number> getData(String table, String entry) {
        if(isLoaded(table,entry))
            return getLoadedDataBy(table,entry);
        return loadData(table,entry);
    }

    @Override
    public List<String> getLoadedEntriesNames(String table) {
        List<String> nameList = new ArrayList<>();
        reader.getLoadableTables().get(table).getEntries().forEach(graphableColumn -> nameList.add(graphableColumn.getEntryName()));
        return nameList;
    }

    @Override
    public List<String> getLoadedTableNames() {
        return new ArrayList<>(reader.getLoadableTables().keySet());
    }


    public Map<Double,Number> getLoadedDataBy(String category, String column) {
        return loadedData.get(category+"_"+column);
    }

    private boolean isLoaded(String category, String entry) {
        return loadedData.containsKey(category+"_"+entry);
    }

    public String getFileName() {
        return fileName;
    }
}
