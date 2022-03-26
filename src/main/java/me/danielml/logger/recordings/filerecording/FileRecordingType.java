package me.danielml.logger.recordings.filerecording;

public enum FileRecordingType {
    SHUFFLEBOARD_CSV, WPI_CSV, UNSUPPORTED;


    public static FileRecordingType fromFile(String fileName) {
        if(fileName.contains(".csv") || fileName.contains(".xlsx"))
            return SHUFFLEBOARD_CSV;
        return UNSUPPORTED;
    }
}
