package fr.guillaumewlt.workflow;

import fr.guillaumewlt.processing.steps.*;
import fr.guillaumewlt.utils.console.ConsoleMessage;

public class WorkflowRunner {

    private final LauncherContext context =  new LauncherContext();
    private ProgramStep currentStep;

    public WorkflowRunner() {
        currentStep = ProgramStep.INIT;
    }

    public void run() {
        while (currentStep != ProgramStep.END) {
            switch (currentStep) {
                case INIT:
                    changeStepMessage(currentStep);
                    new InitProcess(context).process(); // init the app
                    currentStep = ProgramStep.DOWNLOAD_MANIFEST;
                    break;
                case DOWNLOAD_MANIFEST:
                    changeStepMessage(currentStep);
                    new DownloadManifestProcess(context).process(); // Download Manifest
                    currentStep = ProgramStep.INTERPRET_MANIFEST;
                    break;
                case INTERPRET_MANIFEST:
                    changeStepMessage(currentStep);
                    new InterpretManifestProcess(context).process(); // Interpret Manifest
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
                    new InterpretClientJarInfos(context).process(); // Interpret Client Jar Infos
                    currentStep = ProgramStep.DOWNLOAD_CLIENT_JAR;
                    break;
                case DOWNLOAD_CLIENT_JAR:
                    changeStepMessage(currentStep);
                    new DownloadClientJarProcess(context).process(); // Download Client .jar*
                    currentStep = ProgramStep.INTERPRET_VERSION_LIBRARIES_INFOS;
                    break;
                case INTERPRET_VERSION_LIBRARIES_INFOS:
                    changeStepMessage(currentStep);
                    new InterpretLibrariesInfos(context).process(); //*
                    currentStep = ProgramStep.DOWNLOAD_VERSION_LIBRARIES;
                    break;
                case DOWNLOAD_VERSION_LIBRARIES:
                    changeStepMessage(currentStep);
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
