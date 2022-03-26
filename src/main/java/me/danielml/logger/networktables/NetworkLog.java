package me.danielml.logger.networktables;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a concurring log of values for a specific NetworkTable entry.
 * @see me.danielml.logger.recordings.NetworkRecording
 */
public class NetworkLog {

    private final Map<Double,Number> values; // Values of that specific entry, in the format of (Timestamp, Value)

    private Number lastValue;
    private double lastUpdateTime;


    public NetworkLog() {
        this.values = new HashMap<>();
        this.lastValue = 0;
        this.lastUpdateTime = 0;
    }

    /**
     * Updates the log according to the most recent value change/update of the entry.
     * @param time - Timestamp of the update
     * @param newValue - The new value it changed to
     * @param tableName - The NetworkTable its apart of
     * @param entryName - The entry's name.
     * @return A list of all the changes for the entry's value
     */
    public List<UpdateLog> update(Double time, Number newValue, String tableName, String entryName) {
        List<UpdateLog> updates = new ArrayList<>();
//        if(!newValue.equals(lastValue)) {
//            // We do this to reduce unnecessary points being added to the graph, essentially optimizing it.
//            values.put(lastUpdateTime, lastValue);
//            values.put(time,newValue);
//            updates.add(new UpdateLog(lastUpdateTime,lastValue,tableName,entryName));
//            updates.add(new UpdateLog(time,newValue,tableName,entryName));
//        } else {
//            lastUpdateTime = time;
//            lastValue = newValue;
//            updates.add(new UpdateLog(lastUpdateTime,lastValue,tableName,entryName));
//        }
        updates.add(new UpdateLog(time,newValue,tableName,entryName));
        return updates;
    }

    public Map<Double, Number> getValues() {
        return values;
    }
}
