package me.danielml.logger.sheets;

import com.aspose.cells.FileFormatType;
import com.aspose.cells.LoadOptions;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;

import java.io.File;

public class CSVConverter {

    /**
     * Converts a shuffleboard CSV file to a XLSX file form, makes it easier to read (also i can't be asked to deal with CSV files)
     * @param file Shuffleboard CSV export file
     * @throws Exception - If the file is not avaliable or if it fails to save it as  XLSX file
     * Saves the XLSX file to the data directory
     */
    public static void convertTOXLSX(File file) throws Exception {
        LoadOptions options = new LoadOptions(FileFormatType.CSV);
        Workbook workbook = new Workbook(file.getPath(),options);

        String fileName = file.getName();
        File dataDirectory = new File("data");
        if(!dataDirectory.exists()) dataDirectory.mkdir();
        workbook.save("data/" + fileName.substring(0,fileName.length()-4) + ".xlsx", SaveFormat.XLSX);
    }
}
