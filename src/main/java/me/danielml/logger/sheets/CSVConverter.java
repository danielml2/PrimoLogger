package me.danielml.logger.sheets;

import com.aspose.cells.FileFormatType;
import com.aspose.cells.LoadOptions;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;

public class CSVConverter {


    public static void convertTOXLSX(String fileName, String newFileName) throws Exception {
        LoadOptions options = new LoadOptions(FileFormatType.CSV);
        Workbook workbook = new Workbook("data/" + fileName + ".csv",options);

        workbook.save("data/" + newFileName + ".xlsx", SaveFormat.XLSX);
    }
}
