package me.danielml.logger.sheets;

import com.aspose.cells.FileFormatType;
import com.aspose.cells.LoadOptions;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;

public class CSVConverter {

    LoadOptions options = new LoadOptions(FileFormatType.CSV);
    public void convertTOXLSX(String fileName, String newFileName) throws Exception {
        Workbook workbook = new Workbook("data/" + fileName + ".csv",options);

        workbook.save("data/" + newFileName + ".xlsx", SaveFormat.XLSX);
    }
}
