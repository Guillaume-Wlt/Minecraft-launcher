package fr.guillaumewlt.workflow;

import fr.guillaumewlt.processing.Processes;
import fr.guillaumewlt.processing.download.*;
import fr.guillaumewlt.processing.interpret.*;
import fr.guillaumewlt.processing.steps.*;
import lombok.Getter;

import java.util.function.Function;

/**
 * Represents each step in the launcher workflow, in execution order.
 * Each constant holds a display message shown to the user when the step begins.
 * The workflow is driven by {@link WorkflowRunner}.
 */
public enum ProgramStep {
    /** Launcher initialisation. */
    INIT("Starting", InitProcess::new),

    /** Download the Mojang version manifest listing all available versions. */
    DOWNLOAD_MANIFEST("Downloading manifest", DownloadManifestProcess::new),

    /** Parse the manifest and prompt the user to select a version. */
    INTERPRET_MANIFEST("Interpreting manifest", InterpretManifestProcess::new),

    /** Process to start the UI of the application. */
    SHOW_UI("Showing UI", ShowUIProcess::new),

    /** Download the version JSON file for the selected version. */
    DOWNLOAD_VERSION_JSON("Downloading version JSON file", DownloadVersionJSONProcess::new),

    /** Parse the version JSON to extract client, libraries, assets and runtime metadata. */
    INTERPRET_VERSION_JSON("Interpreting version JSON file", InterpretVersionJSONProcess::new),

    /** Extract client JAR download URL and SHA-1 from the version JSON. */
    INTERPRET_CLIENT_JAR_INFOS("Interpreting client jar infos", InterpretClientJarInfos::new),

    /** Download the client JAR, verified against its SHA-1 checksum. */
    DOWNLOAD_CLIENT_JAR("Downloading client jar", DownloadClientJarProcess::new),

    /** Parse the libraries array to extract download URLs, SHA-1 checksums and native classifiers. */
    INTERPRET_VERSION_LIBRARIES_INFOS("Interpreting client libraries", InterpretLibrariesInfos::new),

    /** Download all required libraries, verified against their SHA-1 checksums. */
    DOWNLOAD_VERSION_LIBRARIES("Downloading version libraries", DownloadLibrariesProcess::new),

    /** Extract native library files (.dll / .so / .dylib) into {@code bin/<version>/}. */
    EXTRACT_NATIVES_LIBRARIES("Extracting natives libraries", ExtractNativesLibrariesProcess::new),

    /** Extract asset index URL and SHA-1 from the version JSON. */
    INTERPRET_CLIENT_ASSETS_INDEX("Interpreting client assets index", InterpretClientAssetsIndex::new),

    /** Download the asset index JSON file listing all game assets. */
    DOWNLOAD_CLIENT_ASSETS_INDEX("Downloading assets index", DownloadAssetsIndexProcess::new),

    /** Parse the asset index to build the list of assets to download. */
    INTERPRET_CLIENT_ASSETS_INFOS("Interpreting client assets", InterpretClientAssetsInfos::new),

    /** Download all game assets (textures, sounds, etc.), verified against their SHA-1 checksums. */
    DOWNLOAD_CLIENT_ASSETS("Downloading client assets", DownloadClientAssetsProcess::new),

    /** Download the Mojang runtime manifest for the required JRE component. */
    DOWNLOAD_RUNTIME_JSON("Downloading runtime JSON file", DownloadRuntimeJSONProcess::new),

    /** Parse the runtime manifest to build the list of JRE files to download. */
    INTERPRET_RUNTIME_JSON("Interpreting runtime JSON file", InterpretRuntimeProcess::new),

    /** Download the detailed JRE manifest listing all files to download for the required component. */
    DOWNLOAD_JRE_MANIFEST("Downloading JRE_MANIFEST", DownloadJREManifestProcess::new),

    /** Parse the detailed JRE manifest to build the list of files to download. */
    INTERPRET_JRE_MANIFEST("Interpreting JRE_MANIFEST", InterpretJREManifestProcess::new),

    /** Download the JRE files into {@code runtime/<component>/}. */
    DOWNLOAD_JRE_FILES("Downloading runtime JRE file", DownloadJREFilesProcess::new),

    /** Build the classpath string from all downloaded libraries and the client JAR. */
    CLASSPATH_BUILDING("Classpath building", ClassPathBuildingProcess::new),

    /** Prompt the user for username and RAM settings. */
    REQUEST_INFOS("Request infos", RequestInfosProcess::new),

    /** Launch the Minecraft client via ProcessBuilder. */
    STARTING_CLIENT("Starting client", StartingClientProcess::new),

    /** Terminal step — workflow is complete. */
    END("", null);
    // TODO Add more states for the app.

    /**
     * Label displayed to the user when this step begins.
     */
    @Getter
    private final String mainTask;

    /**
     * The step that follows this one in the workflow.
     * Assigned in the static initializer block to avoid forward-reference issues
     * (an enum constant cannot reference a constant declared after it in the constructor).
     */
    private ProgramStep nextStep;

    /**
     * Factory that instantiates the {@link Processes} associated with this step.
     * Stored as a constructor reference (e.g. {@code InitProcess::new}), which is
     * equivalent to {@code context -> new InitProcess(context)}.
     */
    private final Function<LauncherContext, Processes> processFactory;

    ProgramStep(String mainTask, Function<LauncherContext, Processes> processFactory) {
        this.mainTask = mainTask;
        this.processFactory = processFactory;
    }

    /**
     * Maps each step to its successor.
     * Defined here rather than in the constructor because enum constants
     * cannot forward-reference constants declared later in the same enum.
     * {@code STARTING_CLIENT} loops back to {@code SHOW_UI} to restart the game launch cycle.
     */
    static {
        INIT.nextStep = DOWNLOAD_MANIFEST;
        DOWNLOAD_MANIFEST.nextStep = INTERPRET_MANIFEST;
        INTERPRET_MANIFEST.nextStep = SHOW_UI;
        SHOW_UI.nextStep = DOWNLOAD_VERSION_JSON;
        DOWNLOAD_VERSION_JSON.nextStep = INTERPRET_VERSION_JSON;
        INTERPRET_VERSION_JSON.nextStep = INTERPRET_CLIENT_JAR_INFOS;
        INTERPRET_CLIENT_JAR_INFOS.nextStep = DOWNLOAD_CLIENT_JAR;
        DOWNLOAD_CLIENT_JAR.nextStep = INTERPRET_VERSION_LIBRARIES_INFOS;
        INTERPRET_VERSION_LIBRARIES_INFOS.nextStep = DOWNLOAD_VERSION_LIBRARIES;
        DOWNLOAD_VERSION_LIBRARIES.nextStep = EXTRACT_NATIVES_LIBRARIES;
        EXTRACT_NATIVES_LIBRARIES.nextStep = INTERPRET_CLIENT_ASSETS_INDEX;
        INTERPRET_CLIENT_ASSETS_INDEX.nextStep = DOWNLOAD_CLIENT_ASSETS_INDEX;
        DOWNLOAD_CLIENT_ASSETS_INDEX.nextStep = INTERPRET_CLIENT_ASSETS_INFOS;
        INTERPRET_CLIENT_ASSETS_INFOS.nextStep = DOWNLOAD_CLIENT_ASSETS;
        DOWNLOAD_CLIENT_ASSETS.nextStep = DOWNLOAD_RUNTIME_JSON;
        DOWNLOAD_RUNTIME_JSON.nextStep = INTERPRET_RUNTIME_JSON;
        INTERPRET_RUNTIME_JSON.nextStep = DOWNLOAD_JRE_MANIFEST;
        DOWNLOAD_JRE_MANIFEST.nextStep = INTERPRET_JRE_MANIFEST;
        INTERPRET_JRE_MANIFEST.nextStep = DOWNLOAD_JRE_FILES;
        DOWNLOAD_JRE_FILES.nextStep = CLASSPATH_BUILDING;
        CLASSPATH_BUILDING.nextStep = REQUEST_INFOS;
        REQUEST_INFOS.nextStep = STARTING_CLIENT;
        STARTING_CLIENT.nextStep = SHOW_UI;
    }

    /**
     * Returns the step that follows this one in the workflow.
     *
     * @return the next {@link ProgramStep}
     */
    public ProgramStep next() {
        return nextStep;
    }

    /**
     * Instantiates the {@link Processes} associated with this step and injects the shared context.
     * Calling {@code .process()} on the returned object executes the step.
     *
     * @param context the shared launcher context passed to the process constructor
     * @return a new instance of the process for this step
     */
    public Processes createProcess(LauncherContext context) {
        return processFactory.apply(context);
    }
}
