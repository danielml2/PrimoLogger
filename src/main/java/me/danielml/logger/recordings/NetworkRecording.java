package me.danielml.logger.recordings;

import edu.wpi.first.networktables.NetworkTableInstance;
import me.danielml.logger.networktables.NetworkLogTable;

import java.util.*;

/**
 * Represents a recording that's taken from network tables that are being run on the DS Computer, similar to logging entries with Glass.
 * Differently from {@link FileRecording}, these values are NOT static, as they are being updated according to the values it gets updated to,
 * this gets logged in REAL TIME.
 */
public class NetworkRecording implements Recording {

    private HashMap<String, NetworkLogTable> loggedTables;

    // Static instance of the networktableinstance, since we always only refer to the default instance between the robot & the DS computer
    public static final NetworkTableInstance NT_INSTANCE = NetworkTableInstance.getDefault();
    // Using system time to determine the time that has passed since it connected to it.
    private final long startTime;
    private final Timer timer;

    public NetworkRecording(int teamNumber) {
        this.loggedTables = new HashMap<>();
        this.timer = new Timer();
        this.startTime = System.currentTimeMillis();
        this.init(teamNumber);
    }

    /**
     * Initializes the connection to the NetworkTables
     * @param teamNumber - Robot's team number to connect to.
     */
    private void init(int teamNumber) {
        NT_INSTANCE.startClientTeam(teamNumber);
        NT_INSTANCE.startDSClient();
        timer.scheduleAtFixedRate((new TimerTask() {
            @Override
            public void run() {
                update();
            }
        }),0,100);
    }

    /**
     * Updates the log values
     */
    public void update() {
        double time = (System.currentTimeMillis() - startTime) / 1000;
        for(NetworkLogTable table : loggedTables.values()) {
            table.update(time);
        }
    }

    @Override
    public Map<Double, Number> getData(String table, String entry) {
        if(!loggedTables.containsKey(table))
        {
            NetworkLogTable networkLogTable = new NetworkLogTable(table);
            networkLogTable.addLoggedForValue(entry);
            loggedTables.put(table, networkLogTable);
            return new HashMap<>();
        } else
        {
            NetworkLogTable networkLogTable = loggedTables.get(table);
            if(networkLogTable.isLogged(entry))
                return networkLogTable.getLog(entry).getValues();
            else
                networkLogTable.addLoggedForValue(entry);
        }
        return new HashMap<>();
    }

    @Override
    public List<String> getLoadedEntriesNames(String table) {
        return loggedTables.get(table).getLoggedEntryNames();
    }

    @Override
    public List<String> getLoadedTableNames() {
        return new ArrayList<>(loggedTables.keySet());
    }
}
