package me.danielml.logger.recordings;

import java.util.List;
import java.util.Map;

/**
 * Represents a Generic Type of a NetworkTable/Shuffleboard recording.
 * @see NetworkRecording
 * @see FileRecording
 */
public interface Recording {


    /**
     * Generic Method to get data that's loaded
     * @param table the name of the networktable/shuffleboard tab it is related to, (e.g. Driver, Shooter, limelight)
     * @param entry the name of the entry to load (e.g. X, Y, Velocity, tx, ty)
     * @return A hashmap containing the data in the format of (Timestamp, Value)
     */
    Map<Double, Number> getData(String table, String entry);

    /**
     * Get a list of which entries are loaded in the recording for a specific table.
     * @param table the name of the networktable/shuffleboard tab it is related to, (e.g. Driver, Shooter, limelight)
     * @return A list of the entries that are loaded/logged
     */
    List<String> getLoadedEntriesNames(String table);

    /**
     * Gets a list of the loaded/logged tables in the recording.
     * @return List of the networktables/shuffleboard tab's names (e.g. Limelight, Driver, Shooter)
     */
    List<String> getLoadedTableNames();

    String TABLE_ENTRY_SEPARATOR = ";";

}
