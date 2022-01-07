package me.danielml.logger.networktables;


import java.util.HashMap;
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

    // TODO: Add an object for update contents, and have it be passed back until @NetworkRecording, and update the GUI from there accordingly
    public void update(Double time, Number newValue) {
        if(!newValue.equals(lastValue)) {
            // We do this to reduce unnecessary points being added to the graph, essentially optimizing it.
            values.put(lastUpdateTime, lastValue);
            values.put(time,newValue);
        }
        lastUpdateTime = time;
        lastValue = newValue;
    }

    public Map<Double, Number> getValues() {
        return values;
    }
}
