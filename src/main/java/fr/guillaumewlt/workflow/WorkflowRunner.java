package fr.guillaumewlt.workflow;

import fr.guillaumewlt.processing.steps.DownloadManifestProcess;
import fr.guillaumewlt.processing.steps.DownloadVersionJSONProcess;
import fr.guillaumewlt.processing.steps.InterpretManifestProcess;
import fr.guillaumewlt.processing.steps.InterpretVersionJSONProcess;

public class WorkflowRunner {

    private ProgramStep currentStep;

    public WorkflowRunner() {
        currentStep = ProgramStep.INIT;
    }

    public void run() {
        while (currentStep != ProgramStep.END) {
            switch (currentStep) {
                case INIT:
                    System.out.println(currentStep.getMainTask());
                    currentStep = ProgramStep.DOWNLOAD_MANIFEST;
                    break;
                case DOWNLOAD_MANIFEST:
                    System.out.println(currentStep.getMainTask());
                    DownloadManifestProcess.downloadManifest(); // Download Manifest
                    currentStep = ProgramStep.INTERPRET_MANIFEST;
                    break;
                case INTERPRET_MANIFEST:
                    System.out.println(currentStep.getMainTask());
                    InterpretManifestProcess.interpManifest(); // Interpret Manifest
                    currentStep = ProgramStep.DOWNLOAD_VERSION_JSON;
                    break;
                case DOWNLOAD_VERSION_JSON:
                    System.out.println(currentStep.getMainTask());
                    DownloadVersionJSONProcess.downloadVersionJSON();
                    currentStep = ProgramStep.INTERPRET_VERSION_JSON;
                    break;
                case INTERPRET_VERSION_JSON:
                    System.out.println(currentStep.getMainTask());
                    InterpretVersionJSONProcess.interpVersionJSON();
                    end();
                    break;
            }
        }
    }

    private void end() {
        System.out.println("Ending...");
        currentStep = ProgramStep.END;
    }
}
