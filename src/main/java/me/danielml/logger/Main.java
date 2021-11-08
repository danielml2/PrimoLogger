package me.danielml.logger;

public class Main {
    /**
     * Reason for a different run class is because of an issue with JavaFX library packaging & maven.
     * @see {https://www.reddit.com/r/javahelp/comments/j6iucg/comment/g826uxj/?utm_source=share&utm_medium=web2x&context=3}
     * @see {https://stackoverflow.com/questions/52653836/maven-shade-javafx-runtime-components-are-missing}
     */
    public static void main(String[] args) {
        FXMainApp.main(args);
    }
}
