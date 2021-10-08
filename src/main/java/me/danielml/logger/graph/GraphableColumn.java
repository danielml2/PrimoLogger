package me.danielml.logger.graph;

public class GraphableColumn {

    private String columnName;
    private int columnIndex;

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
