package fr.guillaumewlt.workflow;

import lombok.Getter;

public enum ProgramStep {
    INIT("=== Starting..."),
    DOWNLOAD_MANIFEST("=== Downloading manifest..."),
    INTERPRET_MANIFEST("=== Interpreting manifest..."),
    DOWNLOAD_VERSION_JSON("=== Downloading version JSON file..."),
    INTERPRET_VERSION_JSON("=== Interpreting version JSON file..."),
    INTERPRET_CLIENT_JAR_INFOS("=== Interpreting client jar infos..."),
    DOWNLOAD_CLIENT_JAR("=== Downloading client jar..."),
    INTERPRET_VERSION_LIBRARIES_INFOS("=== Interpreting client libraries..."),
    DOWNLOAD_VERSION_LIBRARIES("=== Downloading version libraries..."),
    EXTRACT_NATIVES_LIBRARIES("=== Extracting natives libraries..."),
    INTERPRET_CLIENT_ASSETS_INDEX("=== Interpreting client assets index..."),
    DOWNLOAD_CLIENT_ASSETS_INDEX("=== Downloading assets index..."),
    INTERPRET_CLIENT_ASSETS_INFOS("=== Interpreting client assets..."),
    DOWNLOAD_CLIENT_ASSETS("=== Downloading client assets..."),
    CLASSPATH_BUILDING("=== Classpath building..."),
    REQUEST_INFOS("=== Request infos..."),
    STARTING_CLIENT("=== Starting client..."),
    END("");
    // TODO Add more states for the app.

    @Getter
    private final String mainTask;

    ProgramStep(String mainTask) {
        this.mainTask = mainTask;
    }
}
