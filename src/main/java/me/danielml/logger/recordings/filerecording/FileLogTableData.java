package me.danielml.logger.recordings.filerecording;

import java.util.ArrayList;
import java.util.List;
public class FileLogTableData {

    private String name;
    private final List<FileLogEntryData> entries;

    /**
     * Represents a category/shuffleboard tab from the CSV file.
     * Contains a list of the value that can be logged from it (tx,ty, X position, Y position)
     * @param name - Table's name (Driver, Limelight, SHooter)
     */
    public FileLogTableData(String name) {
        this.name = name;
        this.entries = new ArrayList<>();
    }

    public void addEntry(FileLogEntryData column) {
        this.entries.add(column);
    }

    public FileLogEntryData getEntry(String name) {
        return entries.stream().filter(column -> column.getEntryName().equals(name)).findFirst().orElse(null);
    }

    public List<FileLogEntryData> getEntries() {
        return entries;
    }
}
