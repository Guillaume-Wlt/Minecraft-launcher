package fr.guillaumewlt.workflow;

import fr.guillaumewlt.processing.steps.*;

public class WorkflowRunner {

    private ProgramStep currentStep;

    public WorkflowRunner() {
        currentStep = ProgramStep.INIT;
    }


    // TODO Remplacer l'instantiation de chaque méthode pour POO (new ...)
    public void run() {
        while (currentStep != ProgramStep.END) {
            switch (currentStep) {
                case INIT:
                    System.out.println(currentStep.getMainTask());
                    new InitProcess().process(); // init the app
                    currentStep = ProgramStep.DOWNLOAD_MANIFEST;
                    break;
                case DOWNLOAD_MANIFEST:
                    System.out.println(currentStep.getMainTask());
                    new DownloadManifestProcess().process(); // Download Manifest
                    currentStep = ProgramStep.INTERPRET_MANIFEST;
                    break;
                case INTERPRET_MANIFEST:
                    System.out.println(currentStep.getMainTask());
                    new InterpretManifestProcess().process(); // Interpret Manifest
                    currentStep = ProgramStep.DOWNLOAD_VERSION_JSON;
                    break;
                case DOWNLOAD_VERSION_JSON:
                    System.out.println(currentStep.getMainTask());
                    new DownloadVersionJSONProcess().process(); // Download version JSON file
                    currentStep = ProgramStep.INTERPRET_VERSION_JSON;
                    break;
                case INTERPRET_VERSION_JSON:
                    System.out.println(currentStep.getMainTask());
                    new InterpretVersionJSONProcess().process(); // Interpret version JSON file
                    currentStep = ProgramStep.DOWNLOAD_CLIENT_JAR;
                    break;
                case DOWNLOAD_CLIENT_JAR:
                    System.out.println(currentStep.getMainTask());
                    new DownloadClientJarProcess().process(); // Download Client .jar
                    currentStep = ProgramStep.DOWNLOAD_VERSION_LIBRARIES;
                    break;
                case DOWNLOAD_VERSION_LIBRARIES:
                    System.out.println(currentStep.getMainTask());
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
