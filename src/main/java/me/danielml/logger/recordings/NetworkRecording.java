package me.danielml.logger.recordings;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import javafx.application.Platform;
import me.danielml.logger.javafx.GUIController;
import me.danielml.logger.networktables.NetworkLogTable;
import me.danielml.logger.networktables.UpdateLog;

import java.util.*;

/**
 * Represents a recording that's taken from network tables that are being run on the DS Computer, similar to logging entries with Glass.
 * Differently from {@link FileRecording}, these values are NOT static, as they are being updated according to the values it gets updated to,
 * this gets logged in REAL TIME.
 */
public class NetworkRecording implements Recording {

    private HashMap<String, NetworkLogTable> loggedTables;

    /** Static instance of the Network Table instance, since we always only refer to the default instance between the robot & the DS computer */
    public static final NetworkTableInstance NT_INSTANCE = NetworkTableInstance.getDefault();
    // Using system time to determine the time that has passed since it connected to it.
    private final long startTime;
    private final Timer timer;

    private GUIController controller;
    private int tableCount = 0;


    public NetworkRecording(int teamNumber, GUIController controller) {
        this.loggedTables = new HashMap<>();
        this.timer = new Timer();
        this.startTime = System.currentTimeMillis();
        this.init(teamNumber);
        this.controller = controller;
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
                if(!controller.isNetworkLogging())
                {
                    cancel();
                    return;
                }
                update();
            }
        }),0,20);
        tableCount = getLoadedTableNames().size();
    }

    /**
     * Updates the log values
     */
    public void update() {
        double time = (System.currentTimeMillis() - startTime) / 1000; // Time in seconds

        List<UpdateLog> updates = new ArrayList<>();
        for(NetworkLogTable table : loggedTables.values()) {
            // See #NetworkLogTable.update()
            List<UpdateLog> logs = table.update(time);
            updates.addAll(logs);
        }
        Platform.runLater(() -> {
            int newTableCount = getLoadedTableNames().size();
            if(tableCount != newTableCount) {
                tableCount = newTableCount;
                controller.updateGUIViews();
            }
            controller.updateChartData(updates);
        });
    }

    @Override
    public Map<Double, Number> getData(String table, String entry) {
        if(!loggedTables.containsKey(table))
        {
            NetworkTable networkTable = table.contains("Shuffleboard/") ? getShuffleboardTable(table.split("Shuffleboard/")[1]) : NT_INSTANCE.getTable(table);

            NetworkLogTable networkLogTable = new NetworkLogTable(table, networkTable);
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
        if(table.contains("Shuffleboard/"))
            return new ArrayList<>(getShuffleboardTable(table.split("Shuffleboard/")[1]).getKeys());
        return new ArrayList<>(NT_INSTANCE.getTable(table).getKeys());
    }

    @Override
    public List<String> getLoadedTableNames() {
        List<String> tableNames = new ArrayList<>();
        NetworkTable shuffleboard = NT_INSTANCE.getTable("Shuffleboard");
        for(String s : shuffleboard.getSubTables()) {
            tableNames.add("Shuffleboard/" + s);
        }
        if(NT_INSTANCE.getTable("limelight").getKeys().size() > 0)
            tableNames.add("limelight");
        return tableNames;
    }

    /**
     * Gets the network table for a specific shuffleboard tab, since it's a sub-table of Shuffleboard
     * @param tabName Shuffleboard's tab name
     * @return the network table for that specific tab
     */
    private NetworkTable getShuffleboardTable(String tabName) {
        return NT_INSTANCE.getTable("Shuffleboard").getSubTable(tabName);
    }
}
