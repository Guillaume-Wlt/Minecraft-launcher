package fr.guillaumewlt.workflow;

import fr.guillaumewlt.processing.steps.*;
import fr.guillaumewlt.ui.windows.ConsoleWindow;
import fr.guillaumewlt.utils.ProgressBarUtils;
import fr.guillaumewlt.utils.console.ConsoleMessage;

public class WorkflowRunner {

    private final LauncherContext context =  new LauncherContext();
    private ProgramStep currentStep;

    public WorkflowRunner() {
        ConsoleWindow.getInstance(null); // Instanciate the console before workflow start
        currentStep = ProgramStep.INIT;
    }

    public void run() {
        while (currentStep != ProgramStep.END) {
            switch (currentStep) {
                case INIT:
                    changeStepUpdate(currentStep);
                    new InitProcess(context).process(); // init the Program
                    currentStep = ProgramStep.DOWNLOAD_MANIFEST;
                    break;
                case DOWNLOAD_MANIFEST:
                    changeStepUpdate(currentStep);
                    new DownloadManifestProcess(context).process(); // Download Manifest (version_manifest.jar)
                    currentStep = ProgramStep.INTERPRET_MANIFEST;
                    break;
                case INTERPRET_MANIFEST:
                    changeStepUpdate(currentStep);
                    new InterpretManifestProcess(context).process(); // Interpret Manifest & Selecting version here
                    currentStep = ProgramStep.SHOW_UI;
                    break;
                case SHOW_UI:
                    changeStepUpdate(currentStep);
                    new ShowUIProcess(context).process(); // Start the UI process // Wait for informations to be fill to continue
                    currentStep = ProgramStep.DOWNLOAD_VERSION_JSON;
                    break;
                case DOWNLOAD_VERSION_JSON:
                    changeStepUpdate(currentStep);
                    new DownloadVersionJSONProcess(context).process(); // Download version JSON file
                    currentStep = ProgramStep.INTERPRET_VERSION_JSON;
                    break;
                case INTERPRET_VERSION_JSON:
                    changeStepUpdate(currentStep);
                    new InterpretVersionJSONProcess(context).process(); // Interpret version JSON file
                    currentStep = ProgramStep.INTERPRET_CLIENT_JAR_INFOS;
                    break;
                case INTERPRET_CLIENT_JAR_INFOS:
                    changeStepUpdate(currentStep);
                    new InterpretClientJarInfos(context).process(); // Interpret Client Jar Infos collected in INTERPRET_VERSION_JSON
                    currentStep = ProgramStep.DOWNLOAD_CLIENT_JAR;
                    break;
                case DOWNLOAD_CLIENT_JAR:  // Download the client JAR (ex: 1.21.11.jar, ...)
                    changeStepUpdate(currentStep);
                    new DownloadClientJarProcess(context).process();
                    currentStep = ProgramStep.INTERPRET_VERSION_LIBRARIES_INFOS;
                    break;
                case INTERPRET_VERSION_LIBRARIES_INFOS: // Interpret the libraries infos collected in INTERPRET_VERSION_JSON
                    changeStepUpdate(currentStep);
                    new InterpretLibrariesInfos(context).process();
                    currentStep = ProgramStep.DOWNLOAD_VERSION_LIBRARIES;
                    break;
                case DOWNLOAD_VERSION_LIBRARIES: // Download each library from List<LibraryInfos>
                    changeStepUpdate(currentStep);
                    new DownloadLibrariesProcess(context).process();
                    currentStep = ProgramStep.EXTRACT_NATIVES_LIBRARIES;
                    break;
                case EXTRACT_NATIVES_LIBRARIES: // Extract each Natives from the Jar file downloaded in the previous step
                    changeStepUpdate(currentStep);
                    new ExtractNativesLibrariesProcess(context).process();
                    currentStep = ProgramStep.INTERPRET_CLIENT_ASSETS_INDEX;
                    break;
                case INTERPRET_CLIENT_ASSETS_INDEX: // Interpret the client assets index infos
                    changeStepUpdate(currentStep);
                    new InterpretClientAssetsIndex(context).process();
                    currentStep = ProgramStep.DOWNLOAD_CLIENT_ASSETS_INDEX;
                    break;
                case DOWNLOAD_CLIENT_ASSETS_INDEX: // Download the client assets index
                    changeStepUpdate(currentStep);
                    new DownloadAssetsIndexProcess(context).process();
                    currentStep = ProgramStep.INTERPRET_CLIENT_ASSETS_INFOS;
                    break;
                case INTERPRET_CLIENT_ASSETS_INFOS: // Interpret the client assets infos
                    changeStepUpdate(currentStep);
                    new InterpretClientAssetsInfos(context).process();
                    currentStep = ProgramStep.DOWNLOAD_CLIENT_ASSETS;
                    break;
                case DOWNLOAD_CLIENT_ASSETS: // Download the client assets
                    changeStepUpdate(currentStep);
                    new DownloadClientAssetsProcess(context).process();
                    currentStep = ProgramStep.DOWNLOAD_RUNTIME_JSON;
                    break;
                case DOWNLOAD_RUNTIME_JSON: // Download the runtime JSON manifest (all.json)
                    changeStepUpdate(currentStep);
                    new DownloadRuntimeJSONProcess(context).process();
                    currentStep = ProgramStep.INTERPRET_RUNTIME_JSON;
                    break;
                case INTERPRET_RUNTIME_JSON: // Interpret the manifest to get the right JRE version
                    changeStepUpdate(currentStep);
                    new InterpretRuntimeProcess(context).process();
                    currentStep = ProgramStep.DOWNLOAD_JRE_MANIFEST;
                    break;
                case DOWNLOAD_JRE_MANIFEST: // Download the detailed JRE manifest (Allow to get all the files to reconstruct the JRE)
                    changeStepUpdate(currentStep);
                    new DownloadJREManifestProcess(context).process();
                    currentStep = ProgramStep.INTERPRET_JRE_MANIFEST;
                    break;
                case INTERPRET_JRE_MANIFEST: // Parse the JRE manifest to get all the files to download
                    changeStepUpdate(currentStep);
                    new InterpretJREManifestProcess(context).process();
                    currentStep = ProgramStep.DOWNLOAD_JRE_FILES;
                    break;
                case DOWNLOAD_JRE_FILES: // Download each JRE file into runtime/<component>/
                    changeStepUpdate(currentStep);
                    new DownloadJREFilesProcess(context).process();
                    currentStep = ProgramStep.CLASSPATH_BUILDING;
                    break;
                case CLASSPATH_BUILDING: // Build the classPath to start the game
                    changeStepUpdate(currentStep);
                    new ClassPathBuildingProcess(context).process();
                    currentStep = ProgramStep.REQUEST_INFOS;
                    break;
                case REQUEST_INFOS: // Request infos to the user : username, min ram, max ram, ...
                    changeStepUpdate(currentStep);
                    new RequestInfosProcess(context).process();
                    currentStep = ProgramStep.STARTING_CLIENT;
                    break;
                case STARTING_CLIENT: // Start the client with a processBuilder
                    changeStepUpdate(currentStep);
                    new StartingClientProcess(context).process();
                    end();
                    break;
                default: // Activate when a ProgramStep isn't recognize (fallback option)
                    ConsoleMessage.WORKFLOW_RUNNER_UNKNOWN_STEP_ERR.errPrintln(currentStep);
                    System.exit(1);
            }
        }
    }

    private void changeStepUpdate(ProgramStep currentStep) {
        ConsoleMessage.WORKFLOW_RUNNER_CHANGE_STEP_MESSAGE.outPrintln(currentStep.getMainTask());

        // Position de SHOW_UI dans l'enum — les étapes avant (INIT, DOWNLOAD_MANIFEST,
        // INTERPRET_MANIFEST, SHOW_UI) ne font pas partie du workflow utilisateur visible
        int showUIIndex = ProgramStep.SHOW_UI.ordinal();

        // On ne met à jour la barre que pour les étapes après SHOW_UI
        if (currentStep.ordinal() > showUIIndex) {
            // Nombre d'étapes à afficher : total minus les étapes ignorées (avant et incluant SHOW_UI)
            int totalSteps = ProgramStep.values().length - showUIIndex - 1;
            // Position relative de l'étape courante en partant de 1 après SHOW_UI
            int currentIndex = currentStep.ordinal() - showUIIndex;
            // Conversion en pourcentage (0-100) pour la JProgressBar
            int value = (currentIndex * 100) / totalSteps;
            ProgressBarUtils.update(value, currentStep.getMainTask());
        }
    }

    private void end() {
        ConsoleMessage.WORKFLOW_RUNNER_ENDING_MESSAGE.outPrintln();
        currentStep = ProgramStep.END;
    }
}
