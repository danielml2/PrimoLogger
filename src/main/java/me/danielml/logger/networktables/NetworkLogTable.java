package me.danielml.logger.networktables;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import me.danielml.logger.recordings.NetworkRecording;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  Represents a log of the values of a specific table in a NetworkTable while in a {@link NetworkRecording}
 *  @see NetworkRecording
 */
public class NetworkLogTable {

    private final HashMap<String, NetworkLog> entries;
    private final NetworkTable table;

    public NetworkLogTable(String tableName) {
       this.table = NetworkRecording.NT_INSTANCE.getTable(tableName);
       this.entries = new HashMap<>();
    }

    public NetworkLog getLog(String entry) {
        return entries.get(entry);
    }

    public boolean isLogged(String entry) {
        return entries.containsKey(entry);
    }

    public void addLoggedForValue(String entry) {
        if(isLogged(entry)) return;
        entries.put(entry, new NetworkLog());
    }

    /**
     * Updates all the entries that are being logged
     * @param time - Time of the update
     */
    public void update(double time) {
        for(Map.Entry<String,NetworkLog> entry : entries.entrySet()) {
            NetworkTableEntry networkEntry = table.getEntry(entry.getKey());
            entry.getValue().update(time,networkEntry.getNumber(0));
        }
    }

    public List<String> getLoggedEntryNames() {
        return new ArrayList<>(entries.keySet());
    }
}
