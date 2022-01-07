package me.danielml.logger.recordings.filerecording;

public class FileLogEntryData {

    private String entryName;
    private int columnIndex;

    /**
     * Represents a value from the CSV Shuffleboard sheet file (e.g. X position, Y position)
     * Marks which column it's related to in the file. and what's its name
     * @param columnName -  Entry's name (e.g. X position, setpoint, tx, ty)
     * @param columnIndex - The column index of the entry's data in the CSV file.
     */
    public FileLogEntryData(String columnName, int columnIndex) {
        this.entryName = columnName;
        this.columnIndex = columnIndex;
    }

    public String getEntryName() {
        return entryName;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

}
