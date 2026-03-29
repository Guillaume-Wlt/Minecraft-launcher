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
                    new DownloadManifestProcess(context).process(); // Download Manifest
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
                    new InterpretVersionJSONProcess(context).process(); // Interpret version JSON file
                    currentStep = ProgramStep.INTERPRET_CLIENT_JAR_INFOS;
                    break;
                case INTERPRET_CLIENT_JAR_INFOS:
                    changeStepMessage(currentStep);
                    new InterpretClientJarInfos(context).process(); // Interpret Client Jar Infos collected in INTERPRET_VERSION_JSON
                    currentStep = ProgramStep.DOWNLOAD_CLIENT_JAR;
                    break;
                case DOWNLOAD_CLIENT_JAR:
                    changeStepMessage(currentStep);
                    new DownloadClientJarProcess(context).process(); // Download Client .jar*
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
                case DOWNLOAD_CLIENT_ASSETS:
                    changeStepMessage(currentStep);
                    new DownloadClientAssetsProcess(context).process();
                    currentStep = ProgramStep.CLASSPATH_BUILDING;
                    break;
                case CLASSPATH_BUILDING:
                    changeStepMessage(currentStep);
                    new ClassPathBuildingProcess(context).process();
                    currentStep = ProgramStep.REQUEST_INFOS;
                    break;
                case REQUEST_INFOS:
                    changeStepMessage(currentStep);
                    new RequestInfosProcess(context).process();
                    currentStep = ProgramStep.STARTING_CLIENT;
                    break;
                case STARTING_CLIENT:
                    changeStepMessage(currentStep);
                    new StartingClientProcess(context).process();
                    end();
                    break;
                default:
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
