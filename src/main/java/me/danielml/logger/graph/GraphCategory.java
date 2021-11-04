package me.danielml.logger.graph;

import java.util.ArrayList;
import java.util.List;
public class GraphCategory {

    private String name;
    private final List<GraphableColumn> subColumns;

    /**
     * Represents a category/shuffleboard tab from the CSV file.
     * Contains a list of the value that can be logged from it (tx,ty, X position, Y position)
     * @param name - Category's name (Driver, Limelight, SHooter)
     */
    public GraphCategory(String name) {
        this.name = name;
        this.subColumns = new ArrayList<>();
    }

    public void addSubColumn(GraphableColumn column) {
        this.subColumns.add(column);
    }

    public GraphableColumn getColumnByName(String name) {
        return subColumns.stream().filter(column -> column.getColumnName().equals(name)).findFirst().orElse(null);
    }

    public List<GraphableColumn> getSubColumns() {
        return subColumns;
    }
}
