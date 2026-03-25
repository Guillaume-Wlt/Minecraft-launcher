package fr.guillaumewlt.workflow;

import lombok.Getter;

public enum ProgramStep {
    INIT("=== Starting..."),
    DOWNLOAD_MANIFEST("=== Downloading manifest..."),
    INTERPRET_MANIFEST("=== Interpreting manifest..."),
    DOWNLOAD_VERSION_JSON("=== Downloading version JSON file..."),
    INTERPRET_VERSION_JSON("=== Interpreting version JSON file..."),
    DOWNLOAD_CLIENT_JAR("=== Downloading client jar..."),
    CHECK_CLIENT_FILE("=== Checking client file..."),
    DOWNLOAD_VERSION_LIBRARIES("=== Downloading version libraries..."),
    STARTING_CLIENT("=== Starting client..."),
    END("");
    // TODO Add more states for the app.

    @Getter
    private final String mainTask;

    ProgramStep(String mainTask) {
        this.mainTask = mainTask;
    }
}
