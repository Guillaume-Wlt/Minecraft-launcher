package fr.guillaumewlt.workflow;

import fr.guillaumewlt.console.ConsoleMessage;
import fr.guillaumewlt.ui.windows.ConsoleWindow;
import fr.guillaumewlt.utils.ProgressBarUtils;

/**
 * Drives the launcher workflow by iterating through {@link ProgramStep} constants.
 * Each step instantiates its associated {@link fr.guillaumewlt.processing.Processes} and executes it.
 * The loop runs until {@link ProgramStep#END} is reached.
 */
public class WorkflowRunner {

    private final LauncherContext context = new LauncherContext();
    private ProgramStep currentStep;

    public WorkflowRunner() {
        ConsoleWindow.getInstance(null); // Instantiate the console before the workflow starts
        currentStep = ProgramStep.INIT;
    }

    /**
     * Starts the workflow loop.
     * On each iteration: updates the UI progress, executes the current step, then advances to the next one.
     */
    public void run() {
        while (currentStep != ProgramStep.END) {
            changeStepUpdate(currentStep);
            currentStep.createProcess(context).process();
            currentStep = currentStep.next();
        }
    }

    /**
     * Logs the current step and updates the progress bar.
     * Steps before and including {@link ProgramStep#SHOW_UI} are excluded from the progress bar,
     * as they run before the user-visible workflow begins.
     *
     * @param currentStep the step that is about to be executed
     */
    private void changeStepUpdate(ProgramStep currentStep) {
        ConsoleMessage.WORKFLOW_RUNNER_CHANGE_STEP_MESSAGE.outPrintln(currentStep.getMainTask());

        // Steps before SHOW_UI (INIT, DOWNLOAD_MANIFEST, INTERPRET_MANIFEST, SHOW_UI)
        // are not part of the user-visible workflow and are excluded from the progress bar
        int showUIIndex = ProgramStep.SHOW_UI.ordinal();

        if (currentStep.ordinal() > showUIIndex) {
            // Total number of visible steps (everything after SHOW_UI, excluding END)
            int totalSteps = ProgramStep.values().length - showUIIndex - 1;
            // Relative position of the current step starting from 1 after SHOW_UI
            int currentIndex = currentStep.ordinal() - showUIIndex;
            // Convert to a percentage (0–100) for the JProgressBar
            int value = (currentIndex * 100) / totalSteps;
            ProgressBarUtils.update(value, currentStep.getMainTask());
        }
    }
}
