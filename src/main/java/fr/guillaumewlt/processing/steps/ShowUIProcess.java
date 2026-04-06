package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.ui.MainWindow;
import fr.guillaumewlt.workflow.LauncherContext;

import javax.swing.*;
import java.util.concurrent.CountDownLatch;

public class ShowUIProcess extends Processes{
    public ShowUIProcess(LauncherContext context) {
        super(context);
    }

    @Override
    public void process() {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            SwingUtilities.invokeLater(() -> new MainWindow(context, latch).show());
            latch.await(); // workflow stuck here waiting for countDown()
        } catch (LauncherException | InterruptedException e) {
            stop(e.getMessage(), 1);
        }
    }
}
