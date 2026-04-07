package fr.guillaumewlt.workflow;

import lombok.Getter;

/**
 * Represents each step in the launcher workflow, in execution order.
 * Each constant holds a display message shown to the user when the step begins.
 * The workflow is driven by {@link WorkflowRunner}.
 */
public enum ProgramStep {
    /** Launcher initialisation. */
    INIT("Starting"),

    /** Download the Mojang version manifest listing all available versions. */
    DOWNLOAD_MANIFEST("Downloading manifest"),

    /** Process to start the UI of the application. */
    SHOW_UI("Showing UI"),

    /** Parse the manifest and prompt the user to select a version. */
    INTERPRET_MANIFEST("Interpreting manifest"),

    /** Download the version JSON file for the selected version. */
    DOWNLOAD_VERSION_JSON("Downloading version JSON file"),

    /** Parse the version JSON to extract client, libraries, assets and runtime metadata. */
    INTERPRET_VERSION_JSON("Interpreting version JSON file"),

    /** Extract client JAR download URL and SHA-1 from the version JSON. */
    INTERPRET_CLIENT_JAR_INFOS(" Interpreting client jar infos"),

    /** Download the client JAR, verified against its SHA-1 checksum. */
    DOWNLOAD_CLIENT_JAR("Downloading client jar"),

    /** Parse the libraries array to extract download URLs, SHA-1 checksums and native classifiers. */
    INTERPRET_VERSION_LIBRARIES_INFOS("Interpreting client libraries"),

    /** Download all required libraries, verified against their SHA-1 checksums. */
    DOWNLOAD_VERSION_LIBRARIES("Downloading version libraries"),

    /** Extract native library files (.dll / .so / .dylib) into {@code bin/<version>/}. */
    EXTRACT_NATIVES_LIBRARIES("Extracting natives libraries"),

    /** Extract asset index URL and SHA-1 from the version JSON. */
    INTERPRET_CLIENT_ASSETS_INDEX("Interpreting client assets index"),

    /** Download the asset index JSON file listing all game assets. */
    DOWNLOAD_CLIENT_ASSETS_INDEX("Downloading assets index"),

    /** Parse the asset index to build the list of assets to download. */
    INTERPRET_CLIENT_ASSETS_INFOS("Interpreting client assets"),

    /** Download all game assets (textures, sounds, etc.), verified against their SHA-1 checksums. */
    DOWNLOAD_CLIENT_ASSETS("Downloading client assets"),

    /** Download the Mojang runtime manifest for the required JRE component. */
    DOWNLOAD_RUNTIME_JSON("Downloading runtime JSON file"),

    /** Parse the runtime manifest to build the list of JRE files to download. */
    INTERPRET_RUNTIME_JSON("Interpreting runtime JSON file"),

    /** Download the detailed JRE manifest listing all files to download for the required component. */
    DOWNLOAD_JRE_MANIFEST("Downloading JRE_MANIFEST"),

    /** Parse the detailed JRE manifest to build the list of files to download. */
    INTERPRET_JRE_MANIFEST("Interpreting JRE_MANIFEST"),

    /** Download the JRE files into {@code runtime/<component>/}. */
    DOWNLOAD_JRE_FILES("Downloading runtime JRE file"),

    /** Build the classpath string from all downloaded libraries and the client JAR. */
    CLASSPATH_BUILDING("Classpath building"),

    /** Prompt the user for username and RAM settings. */
    REQUEST_INFOS("Request infos"),

    /** Launch the Minecraft client via ProcessBuilder. */
    STARTING_CLIENT("Starting client"),

    /** Terminal step — workflow is complete. */
    END("");
    // TODO Add more states for the app.

    @Getter
    private final String mainTask;

    ProgramStep(String mainTask) {
        this.mainTask = mainTask;
    }
}
