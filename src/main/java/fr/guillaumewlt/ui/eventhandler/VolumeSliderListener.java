package fr.guillaumewlt.ui.eventhandler;

import org.lwjgl.openal.AL10;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.function.IntSupplier;

public class VolumeSliderListener implements ChangeListener {

    private final JSlider volumeSlider;
    private final JButton volumeBtn;
    private final ImageIcon speakerIcon;
    private final ImageIcon speakerOffIcon;
    private final IntSupplier alSourceSupplier;
    private int previousVolume;

    public VolumeSliderListener(JSlider volumeSlider, JButton volumeBtn,
                                ImageIcon speakerIcon, ImageIcon speakerOffIcon,
                                IntSupplier alSourceSupplier, int initialVolume) {
        this.volumeSlider = volumeSlider;
        this.volumeBtn = volumeBtn;
        this.speakerIcon = speakerIcon;
        this.speakerOffIcon = speakerOffIcon;
        this.alSourceSupplier = alSourceSupplier;
        this.previousVolume = initialVolume;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        int alSource = alSourceSupplier.getAsInt();
        if (alSource == 0) return;
        int value = volumeSlider.getValue();
        AL10.alSourcef(alSource, AL10.AL_GAIN, value / 100.0f);
        if (value == 0 && previousVolume > 0) volumeBtn.setIcon(speakerOffIcon);
        if (value > 0 && previousVolume == 0) volumeBtn.setIcon(speakerIcon);
        previousVolume = value;
    }
}