package fr.guillaumewlt.workflow;

import fr.guillaumewlt.processing.steps.*;
import fr.guillaumewlt.utils.console.ConsoleMessage;

import java.util.Scanner;

public class WorkflowRunner {

    private final LauncherContext context =  new LauncherContext();
    private ProgramStep currentStep;

    public WorkflowRunner() {
        currentStep = ProgramStep.INIT;
        context.setScanner(new Scanner(System.in));
    }

    public void run() {
        while (currentStep != ProgramStep.END) {
            switch (currentStep) {
                case INIT:
                    changeStepMessage(currentStep);
                    new InitProcess(context).process(); // init the Program
                    currentStep = ProgramStep.DOWNLOAD_MANIFEST;
                    break;
                case DOWNLOAD_MANIFEST:
                    changeStepMessage(currentStep);
                    new DownloadManifestProcess(context).process(); // Download Manifest (version_manifest.jar)
                    currentStep = ProgramStep.INTERPRET_MANIFEST;
                    break;
                case INTERPRET_MANIFEST:
                    changeStepMessage(currentStep);
                    new InterpretManifestProcess(context).process(); // Interpret Manifest & Selecting version here
                    currentStep = ProgramStep.DOWNLOAD_VERSION_JSON;
                    break;
                case DOWNLOAD_VERSION_JSON:
                    changeStepMessage(currentStep);
                    new DownloadVersionJSONProcess(context).process(); // Download version JSON file
                    currentStep = ProgramStep.INTERPRET_VERSION_JSON;
                    break;
                case INTERPRET_VERSION_JSON:
                    changeStepMessage(currentStep);
                    new InterpretVersionJSONProcess(context).process(); // Interpret version JSON file // CUTTING HERE FOR TEST PURPOSE ONLY ---
                    currentStep = ProgramStep.DOWNLOAD_RUNTIME_JSON;
                    break;
                case INTERPRET_CLIENT_JAR_INFOS:
                    changeStepMessage(currentStep);
                    new InterpretClientJarInfos(context).process(); // Interpret Client Jar Infos collected in INTERPRET_VERSION_JSON
                    currentStep = ProgramStep.DOWNLOAD_CLIENT_JAR;
                    break;
                case DOWNLOAD_CLIENT_JAR:  // Download the client JAR (ex: 1.21.11.jar, ...)
                    changeStepMessage(currentStep);
                    new DownloadClientJarProcess(context).process();
                    currentStep = ProgramStep.INTERPRET_VERSION_LIBRARIES_INFOS;
                    break;
                case INTERPRET_VERSION_LIBRARIES_INFOS: // Interpret the libraries infos collected in INTERPRET_VERSION_JSON
                    changeStepMessage(currentStep);
                    new InterpretLibrariesInfos(context).process();
                    currentStep = ProgramStep.DOWNLOAD_VERSION_LIBRARIES;
                    break;
                case DOWNLOAD_VERSION_LIBRARIES: // Download each library from List<LibraryInfos>
                    changeStepMessage(currentStep);
                    new DownloadLibrariesProcess(context).process();
                    currentStep = ProgramStep.EXTRACT_NATIVES_LIBRARIES;
                    break;
                case EXTRACT_NATIVES_LIBRARIES: // Extract each Natives from the Jar file downloaded in the previous step
                    changeStepMessage(currentStep);
                    new ExtractNativesLibrariesProcess(context).process();
                    currentStep = ProgramStep.INTERPRET_CLIENT_ASSETS_INDEX;
                    break;
                case INTERPRET_CLIENT_ASSETS_INDEX: // Interpret the client assets index infos
                    changeStepMessage(currentStep);
                    new InterpretClientAssetsIndex(context).process();
                    currentStep = ProgramStep.DOWNLOAD_CLIENT_ASSETS_INDEX;
                    break;
                case DOWNLOAD_CLIENT_ASSETS_INDEX: // Download the client assets index
                    changeStepMessage(currentStep);
                    new DownloadAssetsIndexProcess(context).process();
                    currentStep = ProgramStep.INTERPRET_CLIENT_ASSETS_INFOS;
                    break;
                case INTERPRET_CLIENT_ASSETS_INFOS: // Interpret the client assets infos
                    changeStepMessage(currentStep);
                    new InterpretClientAssetsInfos(context).process();
                    currentStep = ProgramStep.DOWNLOAD_CLIENT_ASSETS;
                    break;
                case DOWNLOAD_CLIENT_ASSETS: // Download the client assets
                    changeStepMessage(currentStep);
                    new DownloadClientAssetsProcess(context).process();
                    currentStep = ProgramStep.DOWNLOAD_RUNTIME_JSON;
                    break;
                case DOWNLOAD_RUNTIME_JSON: // Download the runtime JSON manifest (all.json) //GOING HERE ---
                    changeStepMessage(currentStep);
                    new DownloadRuntimeJSONProcess(context).process();
                    currentStep = ProgramStep.INTERPRET_RUNTIME_JSON;
                    break;
                case INTERPRET_RUNTIME_JSON: // Interpret the manifest to get the right JRE version
                    changeStepMessage(currentStep);
                    new InterpretRuntimeProcess(context).process();
                    currentStep = ProgramStep.DOWNLOAD_JRE_MANIFEST;
                    break;
                case DOWNLOAD_JRE_MANIFEST: // Download the detailed JRE manifest (Allow to get all the files to reconstruct the JRE)
                    changeStepMessage(currentStep);
                    new DownloadJREManifestProcess(context).process();
                    currentStep = ProgramStep.INTERPRET_JRE_MANIFEST;
                    break;
                case INTERPRET_JRE_MANIFEST: // Parse the JRE manifest to get all the files to download
                    changeStepMessage(currentStep);
                    // ---
                    end();
//                    currentStep = ProgramStep.DOWNLOAD_JRE_FILES;
                    break;
                case DOWNLOAD_JRE_FILES: // Download each JRE file into runtime/<component>/
                    changeStepMessage(currentStep);
                    // ---
                    currentStep = ProgramStep.CLASSPATH_BUILDING;
                    break;
                case CLASSPATH_BUILDING: // Build the classPath to start the game
                    changeStepMessage(currentStep);
                    new ClassPathBuildingProcess(context).process();
                    currentStep = ProgramStep.REQUEST_INFOS;
                    break;
                case REQUEST_INFOS: // Request infos to the user : username, min ram, max ram, ...
                    changeStepMessage(currentStep);
                    new RequestInfosProcess(context).process();
                    currentStep = ProgramStep.STARTING_CLIENT;
                    break;
                case STARTING_CLIENT: // Start the client with a processBuilder
                    changeStepMessage(currentStep);
                    new StartingClientProcess(context).process();
                    end();
                    break;
                default: // Activate when a ProgramStep isn't recognize (fallback option)
                    System.err.println(ConsoleMessage.WORKFLOW_RUNNER_UNKNOWN_STEP_ERR.format(currentStep));
                    System.exit(1);
            }
        }
    }

    private void changeStepMessage(ProgramStep currentStep) {
        System.out.println(ConsoleMessage.WORKFLOW_RUNNER_CHANGE_STEP_MESSAGE.format(currentStep.getMainTask()));
    }

    private void end() {
        System.out.println(ConsoleMessage.WORKFLOW_RUNNER_ENDING_MESSAGE.getMessage());
        currentStep = ProgramStep.END;
    }
}
