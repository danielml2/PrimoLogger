package me.danielml.logger.sheets;

import com.aspose.cells.FileFormatType;
import com.aspose.cells.LoadOptions;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;

import java.io.File;

public class CSVConverter {


    public static void convertTOXLSX(File file) throws Exception {
        LoadOptions options = new LoadOptions(FileFormatType.CSV);
        Workbook workbook = new Workbook(file.getPath(),options);

        String fileName = file.getName();
        workbook.save("data/" + fileName.substring(0,fileName.length()-4) + ".xlsx", SaveFormat.XLSX);
    }
}
