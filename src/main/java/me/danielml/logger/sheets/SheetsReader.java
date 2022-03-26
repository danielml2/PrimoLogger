package me.danielml.logger.sheets;

import edu.wpi.first.util.datalog.DataLogReader;
import edu.wpi.first.util.datalog.DataLogRecord;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.util.datalog.IntegerLogEntry;
import me.danielml.logger.javafx.GUIController;
import me.danielml.logger.recordings.filerecording.FileLogTableData;
import me.danielml.logger.recordings.filerecording.FileLogEntryData;
import me.danielml.logger.recordings.filerecording.FileRecordingType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class SheetsReader {

    private HashMap<String, FileLogTableData> loadableTables;
    private FileRecordingType type;
    private Sheet recordingSheet;

    /**
     * Holds the data from XLSX file generated from the converter {@link CSVConverter}
     * @param file XLSX file name (Converted files go in the data directory)
     */
    public SheetsReader(File file, FileRecordingType type) {
        loadableTables = new HashMap<>();
       try {
           loadableTables = readFile(file, type);
       } catch (Exception exception) {
           exception.printStackTrace();
       }
    }

    /**
     * Reads the file and saves it into the hashmaps
     * @param file XLSX file name
     * @throws IOException If the file doesn't exist, or other IOExceptions
     */
    public HashMap<String,FileLogTableData> readFile(File file, FileRecordingType type) throws Exception {
        this.type = type;
        if(type == FileRecordingType.SHUFFLEBOARD_CSV)
            return readShuffleboardCSVFile(file);
        else if(type == FileRecordingType.WPI_CSV)
            return readWPICSVLog(file);

        return loadableTables;
    }

    private HashMap<String,FileLogTableData> readShuffleboardCSVFile(File file) throws Exception {

        CSVConverter.convertTOXLSX(file);

        File xlsxFile = new File("data/" + file.getName().substring(0, file.getName().length()-4) + ".xlsx");
        System.out.println(xlsxFile.getPath());
        if(!xlsxFile.exists()) return null;

        FileInputStream stream = new FileInputStream(xlsxFile);
        Workbook workbook = new XSSFWorkbook(stream);
        System.out.println(workbook.getNumberOfSheets());
        recordingSheet = workbook.getSheetAt(0);

        for(Cell cell : recordingSheet.getRow(0)) {
            if(cell.getCellType() == CellType.STRING)
            {
                String value = cell.getStringCellValue();


                if(value.contains("Shuffleboard") || value.contains("limelight"))
                {
                    if(!value.contains("network_table") || value.contains("CameraPublisher")) continue;
                    value = value.split("network_table:///")[1];
                    String tableName = extractTableName(value);
                    String entryName = extractEntryName(value);

                    if(!loadableTables.containsKey(tableName)) {
                        System.out.println("New table: " + "[" + tableName + "]");
                    }

                    FileLogTableData tableData = loadableTables.containsKey(tableName) ? loadableTables.get(tableName) : new FileLogTableData(tableName);
                    if(!tableData.hasEntry(entryName)) {
                        System.out.println("New entry: " +  "[" + entryName + "]");
                    }
                    tableData.addEntry(new FileLogEntryData(entryName,cell.getColumnIndex()));
                    loadableTables.put(tableName,tableData);
                }
            }
        }
        return loadableTables;
    }

    private HashMap<String,FileLogTableData> readWPICSVLog(File file) throws Exception {
        CSVConverter.convertTOXLSX(file);

        File xlsxFile = new File("data/" + file.getName().substring(0, file.getName().length()-4) + ".xlsx");
        System.out.println(xlsxFile.getPath());
        if(!xlsxFile.exists()) return null;

        FileInputStream stream = new FileInputStream(xlsxFile);
        Workbook workbook = new XSSFWorkbook(stream);
        System.out.println(workbook.getNumberOfSheets());
        recordingSheet = workbook.getSheetAt(0);

        for(Cell cell : recordingSheet.getRow(0)) {
            if(cell.getCellType() == CellType.STRING)
            {
                String value = cell.getStringCellValue();


                if(value.contains("Shuffleboard") || value.contains("limelight"))
                {
                    if(!value.contains("NT:/") || value.contains("CameraPublisher")) continue;
                    value = value.split("NT:/")[1];
                    String tableName = extractTableName(value);
                    String entryName = extractEntryName(value);

                    if(!loadableTables.containsKey(tableName)) {
                        System.out.println("New table: " + "[" + tableName + "]");
                    }

                    FileLogTableData tableData = loadableTables.containsKey(tableName) ? loadableTables.get(tableName) : new FileLogTableData(tableName);
                    if(!tableData.hasEntry(entryName)) {
                        System.out.println("New entry: " +  "[" + entryName + "]");
                    }
                    tableData.addEntry(new FileLogEntryData(entryName,cell.getColumnIndex()));
                    loadableTables.put(tableName,tableData);
                }
            }
        }

        return loadableTables;
    }

    /**
     * Gets the data loaded in the map (The map basically holds all the data from the sheet file)
     * @param tableName Value's category/Shuffleboard tab name (e.g. Driver, Shooter, Limelight)
     * @param entryName Value's name/Shuffleboard entry name (e.g. X position, setpoint, tx,ty)
     * @return Data represented in a hashmap: (Timestamp, Value)
     */
    public HashMap<Double,Number> getDataFromSheet(String tableName, String entryName) {
        FileLogTableData category = getLoadableTables().get(tableName);
        if(category == null) return null;
        System.out.println("Valid category");
        FileLogEntryData column = category.getEntry(entryName);
        if(column == null) return null;
        System.out.println("Valid column");


        System.out.println("Rows: " + recordingSheet.getLastRowNum());

        HashMap<Double,Number> data = new HashMap<>();

        // TODO: Optimize point reading from recordingSheet for long files so JavaFx doesn't shit itself apparently
        for(Row row : recordingSheet) {
            if(row == null) {
                System.out.println("Row is null.");
                continue;
            }
            if(row.getCell(0) == null || row.getCell(0).getRowIndex() == 0)
            {
                continue;
            }

            double timeStamp = type == FileRecordingType.SHUFFLEBOARD_CSV ? row.getCell(0).getNumericCellValue() / 1000 : row.getCell(0).getNumericCellValue();

            Cell cell = row.getCell(column.getColumnIndex());


            if(cell != null)
                if(cell.getCellType() == CellType.NUMERIC)
                    data.put(timeStamp,cell.getNumericCellValue());
                else if(cell.getCellType() == CellType.BOOLEAN) {

                    // i hate this so much
                    double val = cell.getBooleanCellValue() ? GUIController.getInstance().getBooleanTrueNumeric() :  GUIController.getInstance().getBooleanFalseNumeric();
                    data.put(timeStamp, val);
                }
            else
                data.put(timeStamp,0);
        }

        System.out.println("Final Point count: " + data.size());

        return data;
    }

    /**
     * Extracts category/Shuffleboard tab name from the sheet cell value
     * @param sheetValue Value from the sheet cell
     * @return The name of the table it's part of (Driver, Shooter, Limelight)
     */
    public String extractTableName(String sheetValue) {
        if(sheetValue.contains("Shuffleboard"))
            return sheetValue.split("Shuffleboard")[1].split("/")[1];
        else
            return sheetValue.split("/")[0];
    }

    /**
     * Extracts out the Column/Value's name from the sheet cell value
     * @param tableValue Value from the sheet cell
     * @return The name of the value it represents (X position,setpoint,tx,ty)
     */
    public String extractEntryName(String tableValue) {
        if(tableValue.contains("Shuffleboard"))
            return tableValue.split("/")[2];
        else
            return tableValue.split("/")[1];
    }

    public HashMap<String, FileLogTableData> getLoadableTables() {
        return loadableTables;
    }
}
