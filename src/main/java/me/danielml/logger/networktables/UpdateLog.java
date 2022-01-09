package me.danielml.logger.networktables;

/**
 * Represents a single change of an entry's value
 * @see me.danielml.logger.recordings.NetworkRecording
 */
public class UpdateLog {

    private final double timestamp;
    private final Number value;
    private final String tableName;
    private final String entryName;

    /**
     * Represents a change of a single entry's data
     * @param timestamp Timestamp of a change
     * @param value the value it changed to
     * @param tableName The NetworkTable it's apart of
     * @param entryName The entry it's related to
     */
    public UpdateLog(double timestamp, Number value, String tableName, String entryName) {
        this.timestamp = timestamp;
        this.value = value;
        this.tableName = tableName;
        this.entryName = entryName;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public Number getValue() {
        return value;
    }

    public String getMapName() {
        return tableName + "_" + entryName;
    }
}
