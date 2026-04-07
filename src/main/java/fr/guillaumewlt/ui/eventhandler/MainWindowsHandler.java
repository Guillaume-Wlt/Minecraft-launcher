package fr.guillaumewlt.ui.eventhandler;

import fr.guillaumewlt.ui.windows.ConsoleWindow;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MainWindowsHandler implements WindowListener {

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {
        if (e.getOppositeWindow() != null) return; // internal focus, ignore...
        ConsoleWindow console = ConsoleWindow.getInstance(null);
        if (console.isVisible()) {
            console.toFront();
        }
    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
