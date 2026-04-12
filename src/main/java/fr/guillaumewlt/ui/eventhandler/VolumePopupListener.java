package fr.guillaumewlt.ui.eventhandler;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;

public class VolumePopupListener implements PopupMenuListener {

    private final JButton volumeBtn;
    private final Runnable onDismissedByButton;

    public VolumePopupListener(JButton volumeBtn, Runnable onDismissedByButton) {
        this.volumeBtn = volumeBtn;
        this.onDismissedByButton = onDismissedByButton;
    }

    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        Point mouse = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(mouse, volumeBtn);
        if (volumeBtn.contains(mouse)) onDismissedByButton.run();
    }

    @Override
    public void popupMenuCanceled(PopupMenuEvent e) {}

    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}
}