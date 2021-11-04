package me.danielml.logger.graph;

public class GraphableColumn {

    private String columnName;
    private int columnIndex;

    /**
     * Represents a value from the CSV Shuffleboard sheet file (e.g. X position, Y position)
     * Marks which column it's related to in the file. and what's its name
     * @param columnName - Column/Category's name (e.g. X position, setpoint, tx, ty)
     * @param columnIndex - Columm Index
     */
    public GraphableColumn(String columnName, int columnIndex) {
        this.columnName = columnName;
        this.columnIndex = columnIndex;
    }

    public String getColumnName() {
        return columnName;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

}
