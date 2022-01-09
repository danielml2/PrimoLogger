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
    private final String tableName;

    public NetworkLogTable(String tableName, NetworkTable networkTable) {
       this.table = networkTable;
       this.tableName = tableName;
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
     * @return Returns a list of all the changes happened to the entries that are being logged
     */
    public List<UpdateLog> update(double time) {
        List<UpdateLog> updates = new ArrayList<>();
        for(Map.Entry<String,NetworkLog> entry : entries.entrySet()) {
            NetworkTableEntry networkEntry = table.getEntry(entry.getKey());
            List<UpdateLog> entryChanges = entry.getValue().update(time,networkEntry.getNumber(0),tableName,entry.getKey());
            updates.addAll(entryChanges);
        }
        return updates;
    }

}
