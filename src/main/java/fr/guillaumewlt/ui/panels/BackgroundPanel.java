package fr.guillaumewlt.ui.panels;

import fr.guillaumewlt.ui.eventhandler.ButtonHandler;
import fr.guillaumewlt.workflow.LauncherContext;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ShortBuffer;
import java.util.Objects;


public class BackgroundPanel extends JPanel {

    private final LauncherContext context;
    private BufferedImage currentFrame;

    private File bgVideo;
    private SourceDataLine line;
    private boolean paused;

    private JButton playPauseBtn;
    private JButton volumeBtn;
    private JPopupMenu volumePopup;
    private JSlider volumeSlider;

    public BackgroundPanel(LauncherContext context) {
        this.context = context;
        setLayout(null);

        try {
            bgVideo = new File(getClass().getResource("/background/background.mp4").toURI());
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }

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
        volumeBtn.setIcon(loadIcon("/icons/speaker.png", 24));
        volumeBtn.setText("");

        volumeSlider = new JSlider(JSlider.VERTICAL, 0, 100, 30);
        volumeSlider.setPreferredSize(new Dimension(24, 80));

        volumePopup = new JPopupMenu();
        volumePopup.add(volumeSlider);

        ButtonHandler buttonHandler = new ButtonHandler();
        buttonHandler.setBackgroundPanel(this);
        playPauseBtn.addActionListener(buttonHandler);
        volumeBtn.addActionListener(buttonHandler);

        add(playPauseBtn);
        add(volumeBtn);

        startVideo();
    }

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

    private void startVideo() {
        avutil.av_log_set_level(avutil.AV_LOG_ERROR);
        new Thread(() -> {
            try {
                FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(bgVideo);
                Java2DFrameConverter converter = new Java2DFrameConverter();
                grabber.start();
                long[] startTime = { System.nanoTime() };
                while (true) {
                    if (paused) {
                        Thread.sleep(100);
                        continue;
                    }
                    Frame frame = grabber.grabImage();
                    if (frame == null) {
                        grabber.restart();
                        startTime[0] = System.nanoTime();
                    }
                    else {
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
                grabber.setSampleFormat(avutil.AV_SAMPLE_FMT_S16);
                grabber.start();
                int sampleRate = grabber.getSampleRate();
                int audioChannels = grabber.getAudioChannels();
                AudioFormat audioFormat = new AudioFormat(sampleRate, 16, audioChannels, true, false);
                line = AudioSystem.getSourceDataLine(audioFormat);
                line.open(audioFormat);
                line.start();
                while (true) {
                    if (paused) {
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

    private ImageIcon loadIcon(String path, int size) {
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(path)));
        Image scaled = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
}