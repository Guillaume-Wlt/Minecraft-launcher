package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.ui.MainWindow;
import fr.guillaumewlt.ui.panels.BottomPanel;
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
            if (context.getMainWindow() == null) {
                context.setLatch(latch);
                SwingUtilities.invokeLater(() -> {
                    JFrame frame = new MainWindow(context).show();
                    context.setMainWindow(frame);
                });
                latch.await(); // workflow stuck here waiting for countDown()
            } else {
                context.setLatch(latch);
                SwingUtilities.invokeLater(() -> {
                    BottomPanel bottomPanel = context.getBottomPanel();
                    bottomPanel.reset();
                });
                latch.await();
            }
        } catch (LauncherException | InterruptedException e) {
            stop(e.getMessage(), 1);
        }
    }
}
