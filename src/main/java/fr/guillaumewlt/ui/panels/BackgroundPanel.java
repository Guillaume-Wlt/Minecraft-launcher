package fr.guillaumewlt.ui.panels;

import fr.guillaumewlt.parser.SettingsJSONParser;
import fr.guillaumewlt.ui.eventhandler.ButtonsListener;
import fr.guillaumewlt.ui.eventhandler.VolumeSliderListener;
import fr.guillaumewlt.workflow.LauncherContext;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.json.JSONObject;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ShortBuffer;
import java.nio.file.Files;
import java.util.Objects;


public class BackgroundPanel extends JPanel {

    // -------------------------------------------------------Fields

    private final LauncherContext context; // context of the launcher
    private BufferedImage currentFrame; // Current frame playing > BufferedImage

    private File bgVideo; // Contain the video for the bg
    private SourceDataLine line; // Contain AudioLine
    private double volume;
    private boolean paused; // Manage if video is paused or not

    private JButton playPauseBtn; // Play/Pause Btn
    private JButton volumeBtn; // Volume Btn
    private JPopupMenu volumePopup; // Volume PopUp
    private long lastPopupClose = 0;
    private JSlider volumeSlider; // Volume Slide

    // -------------------------------------------------------Panel

    public BackgroundPanel(LauncherContext context) {
        this.context = context; // assign context
        setLayout(null); // layer null > Frame take all content

        try {
            bgVideo = new File(getClass().getResource("/background/background.mp4").toURI()); // assign Video to Field
        } catch (URISyntaxException ex) {
            ex.printStackTrace(); // catch Exception
        }

        SettingsJSONParser parser = new SettingsJSONParser(context);
        parser.readSettings();

        playPauseBtn = new JButton();
        volumeBtn = new JButton();

        playPauseBtn.setActionCommand("playPause");
        playPauseBtn.setFont(playPauseBtn.getFont().deriveFont(Font.BOLD, 13f));
        playPauseBtn.setPreferredSize(new Dimension(36, 36));
        playPauseBtn.setIcon(loadIcon("/icons/pause-button.png", 24));
        playPauseBtn.setText("");

        volumeBtn.setActionCommand("volume");
        volumeBtn.setFont(volumeBtn.getFont().deriveFont(Font.BOLD, 13f));
        volumeBtn.setPreferredSize(new Dimension(36, 36));
        if (parser.getVolume() == 0) {
            volumeBtn.setIcon(loadIcon("/icons/speaker-off.png", 24));
        } else {
            volumeBtn.setIcon(loadIcon("/icons/speaker.png", 24));
        }
        volumeBtn.setText("");

        volumeSlider = new JSlider(JSlider.VERTICAL, 0, 100, 30);
        volumeSlider.setPreferredSize(new Dimension(36, 80));
        volumeSlider.setValue((int) parser.getVolume());

        volumePopup = new JPopupMenu();
        volumePopup.add(volumeSlider);
        volumePopup.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                lastPopupClose = System.currentTimeMillis();
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {

            }
        });

        VolumeSliderListener volumeSliderListener = new VolumeSliderListener();
        volumeSliderListener.setBackgroundPanel(this);
        volumeSlider.addChangeListener(volumeSliderListener);

        ButtonsListener buttonsListener = new ButtonsListener();
        buttonsListener.setBackgroundPanel(this);
        playPauseBtn.addActionListener(buttonsListener);
        volumeBtn.addActionListener(buttonsListener);

        add(playPauseBtn);
        add(volumeBtn);

        if (parser.isVideoPaused()) {
            paused = true;
            playPauseBtn.setIcon(loadIcon("/icons/play-button.png", 24));
        }

        volume = parser.getVolume();

        startVideo();
    }

    // -------------------------------------------------------Frame Drawer

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (currentFrame != null) {
            int imgW = currentFrame.getWidth();
            int imgH = currentFrame.getHeight();
            float scale = Math.max((float) getWidth() / imgW, (float) getHeight() / imgH);
            int drawW = (int) (imgW * scale);
            int drawH = (int) (imgH * scale);
            int x = (getWidth() - drawW) / 2;
            int y = (getHeight() - drawH) / 2;
            g.drawImage(currentFrame, x, y, drawW, drawH, this);
        }
        int btnW = 40, btnH = 40, margin = 10;
        playPauseBtn.setBounds(getWidth() - (btnW * 2 + margin * 2), getHeight() - btnH - margin, btnW, btnH);
        volumeBtn.setBounds(getWidth() - (btnW + margin), getHeight() - btnH - margin, btnW, btnH);
    }

    // -------------------------------------------------------Video Player

    private void startVideo() {
        avutil.av_log_set_level(avutil.AV_LOG_ERROR);
        new Thread(() -> {
            try {
                FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(bgVideo);
                Java2DFrameConverter converter = new Java2DFrameConverter();
                grabber.start();
                long pauseStart = 0;
                long[] startTime = { System.nanoTime() };

                Frame frame;
                frame = grabber.grabImage();
                if (frame == null) {
                    grabber.restart();
                    startTime[0] = System.nanoTime();
                } else {
                    currentFrame = converter.convert(frame);
                    repaint();
                }
                while (true) {
                    if (paused) { // If paused >> Paused the Thread
                        if (pauseStart == 0) pauseStart = System.nanoTime();
                        Thread.sleep(100);
                        continue;
                    }
                    if (pauseStart != 0) {
                        startTime[0] += System.nanoTime() - pauseStart;
                        pauseStart = 0;
                    }
                    frame = grabber.grabImage();
                    if (frame == null) {
                        grabber.restart();
                        startTime[0] = System.nanoTime();
                    } else {
                        currentFrame = converter.convert(frame);
                        repaint();
                        long delay = frame.timestamp / 1000 - (System.nanoTime() - startTime[0]) / 1_000_000;
                        if (delay > 0) Thread.sleep(delay);
                    }
                }
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(bgVideo);
                grabber.setSampleFormat(avutil.AV_SAMPLE_FMT_S16); // user S16 sample Format
                grabber.start();
                int sampleRate = grabber.getSampleRate();
                int audioChannels = grabber.getAudioChannels();
                AudioFormat audioFormat = new AudioFormat(sampleRate, 16, audioChannels, true, false);
                line = AudioSystem.getSourceDataLine(audioFormat);
                line.open(audioFormat, (int) (audioFormat.getSampleRate() / 10 * audioFormat.getFrameSize())); // open Audio Line
                line.start();

                FloatControl control = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = volume == 0 ? -80f : (float)(20 * Math.log10(volume / 100.0));
                control.setValue(dB);

                while (true) {
                    if (paused) { // If paused >> Paused the Thread
                        Thread.sleep(100);
                        continue;
                    }
                    Frame frame = grabber.grabSamples();
                    if (frame == null) grabber.restart();
                    else {
                        ShortBuffer shortBuffer = (ShortBuffer) frame.samples[0];
                        byte[] bytes = new byte[shortBuffer.limit() * 2];
                        for (int i = 0; i < shortBuffer.limit(); i++) {
                            short s = shortBuffer.get(i);
                            bytes[i * 2] = (byte) (s & 0xFF);
                            bytes[i * 2 + 1] = (byte) ((s >> 8) & 0xFF);
                        }
                        line.write(bytes, 0, bytes.length);
                    }
                }
            } catch (IOException | InterruptedException | LineUnavailableException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    public void togglePlayPause() {
        paused = !paused;
        if (paused) {
            playPauseBtn.setIcon(loadIcon("/icons/play-button.png", 24));
            line.stop();
        } else {
            playPauseBtn.setIcon(loadIcon("/icons/pause-button.png", 24));
            line.start();
        }
        saveSettings();
    }

    public void setVolume(double value) {
        if (line == null) return;
        FloatControl control = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
        float dB = (float) (20 * Math.log10(value / 100));
        volume = value;
        if (value == 0 || value < 0) {
            control.setValue(-80f);
            volumeBtn.setIcon(loadIcon("/icons/speaker-off.png", 24));
        } else {
            control.setValue(dB);
            volumeBtn.setIcon(loadIcon("/icons/speaker.png", 24));
        }
        saveSettings();
    }

    public void toggleVolumeMenu() {
        if (System.currentTimeMillis() - lastPopupClose < 300) return;
        volumePopup.show(volumeBtn, 1, -volumePopup.getPreferredSize().height - 5);
    }

    public void saveSettings() {
        File settingsFile = new File(context.getLauncherDirs().configDir().path() + "settings.json");
        try {
            JSONObject settings;
            if (settingsFile.exists()) {
                String content = Files.readString(settingsFile.toPath());
                settings = new JSONObject(content);
            } else {
                settings = new JSONObject();
            }

            try (FileWriter fw = new FileWriter(settingsFile)) {
                settings.put("volume", volume);
                settings.put("videoPaused", paused);
                fw.write(settings.toString());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // -------------------------------------------------------Utils for Icons

    private ImageIcon loadIcon(String path, int size) {
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(path)));
        Image scaled = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
}