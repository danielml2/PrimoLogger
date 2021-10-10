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

    public SheetsReader(String fileName) {
        graphCategories = new HashMap<>();
       try {
           readFile(fileName + ".xlsx");
       } catch (IOException exception) {
           exception.printStackTrace();
       }
    }

    public void readFile(String fileName) throws IOException {
        File file = new File("data/" + fileName);
        if(!file.exists()) return;

        FileInputStream stream = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(stream);
        recordingSheet = workbook.getSheet("recording");

        for(Cell cell : recordingSheet.getRow(0)) {
            if(cell.getCellType() == CellType.STRING)
            {
                String value = cell.getStringCellValue();


                if(value.contains("Shuffleboard") || value.contains("limelight"))
                {
                    if(!value.contains("network_table") || value.contains("CameraPublisher")) continue;
                    value = value.split("network_table:///")[1];
                    String categoryName = filterCategory(value);
                    String columnName = filterColumn(value);

                    GraphCategory category = graphCategories.containsKey(categoryName) ? graphCategories.get(categoryName) : new GraphCategory(categoryName);
                    category.addSubColumn(new GraphableColumn(columnName,cell.getColumnIndex()));
                    graphCategories.put(categoryName,category);
                }
            }
        }

    }

    public HashMap<Double,Double> getDataFromSheet(String categoryName, String columnName) {
        GraphCategory category = graphCategories.get(categoryName);
        if(category == null) return null;
        GraphableColumn column = category.getColumnByName(columnName);
        if(column == null) return null;

        HashMap<Double,Double> data = new HashMap<>();

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

    public String filterCategory(String tableValue) {
        if(tableValue.contains("Shuffleboard"))
            return tableValue.split("Shuffleboard")[1].split("/")[1];
        else
            return tableValue.split("/")[0];
    }

    public String filterColumn(String tableValue) {
        if(tableValue.contains("Shuffleboard"))
            return tableValue.split("/")[2];
        else
            return tableValue.split("/")[1];
    }


}
