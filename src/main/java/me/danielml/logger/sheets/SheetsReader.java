package me.danielml.logger.sheets;

import me.danielml.logger.graph.GraphCategory;
import me.danielml.logger.graph.GraphableColumn;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

public class SheetsReader {

    private HashMap<String, GraphCategory> graphCategories;
    private Sheet recordingSheet;

    /**
     * Holds the data from XLSX file generated from the converter {@link CSVConverter}
     * @param fileName XLSX file name (Converted files go in the data directory)
     */
    public SheetsReader(String fileName) {
        graphCategories = new HashMap<>();
       try {
           readFile(fileName + ".xlsx");
       } catch (IOException exception) {
           exception.printStackTrace();
       }
    }

    /**
     * Reads the file and saves it into the hashmaps
     * @param fileName XLSX file name
     * @throws IOException If the file doesn't exist, or other IOExceptions
     */
    public void readFile(String fileName) throws IOException {
        File file = new File("data/" + fileName);
        System.out.println(file.getPath());
        if(!file.exists()) return;

        FileInputStream stream = new FileInputStream(file);
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
                    String categoryName = extractCategoryName(value);
                    String columnName = extractColumnName(value);

                    GraphCategory category = graphCategories.containsKey(categoryName) ? graphCategories.get(categoryName) : new GraphCategory(categoryName);
                    category.addSubColumn(new GraphableColumn(columnName,cell.getColumnIndex()));
                    graphCategories.put(categoryName,category);
                }
            }
        }

    }

    /**
     * Gets the data loaded in the map (The map basically holds all the data from the sheet file)
     * @param categoryName Value's category/Shuffleboard tab name (e.g. Driver, Shooter, Limelight)
     * @param columnName Value's name/Shuffleboard entry name (e.g. X position, setpoint, tx,ty)
     * @return Data represented in a hashmap: (Timestamp, Value)
     */
    public HashMap<Double,Double> getDataFromSheet(String categoryName, String columnName) {
        GraphCategory category = graphCategories.get(categoryName);
        if(category == null) return null;
        GraphableColumn column = category.getColumnByName(columnName);
        if(column == null) return null;

        HashMap<Double,Double> data = new HashMap<>();

        // TODO: Optimize point reading from recordingSheet for long files so JavaFx doesn't shit itself apparently
        for(Row row : recordingSheet) {
            if(row.getCell(0).getRowIndex() == 0)
                continue;

            double timeStamp = row.getCell(0).getNumericCellValue() / 1000;

            Cell cell = row.getCell(column.getColumnIndex());

            if(cell.getCellType() == CellType.NUMERIC)
                data.put(timeStamp,cell.getNumericCellValue());
            else
                return null;
        }

        return data;
    }

    /**
     * Extracts category/Shuffleboard tab name from the sheet cell value
     * @param sheetValue Value from the sheet cell
     * @return The name of category/NetworkTable it's of (Driver, Shooter, Limelight)
     */
    public String extractCategoryName(String sheetValue) {
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
    public String extractColumnName(String tableValue) {
        if(tableValue.contains("Shuffleboard"))
            return tableValue.split("/")[2];
        else
            return tableValue.split("/")[1];
    }

    public HashMap<String, GraphCategory> getGraphCategories() {
        return graphCategories;
    }
}
