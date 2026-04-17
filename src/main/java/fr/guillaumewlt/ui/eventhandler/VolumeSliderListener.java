package fr.guillaumewlt.ui.eventhandler;

import fr.guillaumewlt.ui.panels.BackgroundPanel;
import lombok.Setter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class VolumeSliderListener implements ChangeListener {

    @Setter
    private BackgroundPanel backgroundPanel;

    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider slider = (JSlider) e.getSource();
        backgroundPanel.setVolume(slider.getValue());
    }
}
