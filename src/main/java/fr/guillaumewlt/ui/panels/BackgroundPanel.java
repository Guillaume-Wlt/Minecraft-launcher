package fr.guillaumewlt.ui.panels;

import fr.guillaumewlt.ui.eventhandler.ButtonHandler;
import fr.guillaumewlt.ui.eventhandler.VolumePopupListener;
import fr.guillaumewlt.ui.eventhandler.VolumeSliderListener;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.*;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;

public class BackgroundPanel extends JPanel {

    private final ArrayBlockingQueue<BufferedImage> buffer;
    private BufferedImage currentFrame;

    private long alDevice;
    private long alContext;
    private int alBuffer;
    private int alSource;

    private boolean paused;

    private JButton playPauseBtn;
    private JButton volumeBtn;
    private JPopupMenu volumePopup;
    private JSlider volumeSlider;

    private boolean skipNextVolumeShow = false;

    private ImageIcon speakerIcon;
    private ImageIcon speakerOffIcon;

    public BackgroundPanel() {

        buffer = new ArrayBlockingQueue<>(30);
        setLayout(null);

        speakerIcon = loadIcon("/icons/speaker.png", 24);
        speakerOffIcon = loadIcon("/icons/speaker-off.png", 24);

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
        volumeBtn.setIcon(speakerIcon);
        volumeBtn.setText("");

        volumeSlider = new JSlider(JSlider.VERTICAL, 0, 100, 30);
        volumeSlider.setPreferredSize(new Dimension(24, 80));
        volumeSlider.addChangeListener(new VolumeSliderListener(
                volumeSlider, volumeBtn, speakerIcon, speakerOffIcon, () -> alSource, 30
        ));

        volumePopup = new JPopupMenu();
        volumePopup.add(volumeSlider);
        volumePopup.addPopupMenuListener(new VolumePopupListener(volumeBtn, () -> skipNextVolumeShow = true));

        ButtonHandler buttonHandler = new ButtonHandler();
        buttonHandler.setBackgroundPanel(this);
        playPauseBtn.addActionListener(buttonHandler);
        volumeBtn.addActionListener(buttonHandler);

        add(playPauseBtn);
        add(volumeBtn);

        startVideo();
        SwingUtilities.invokeLater(() -> volumeSlider.setValue(30));
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

    public void togglePlayPause() {
        paused = !paused;
        if (paused) {
            AL10.alSourcePause(alSource);
            playPauseBtn.setIcon(loadIcon("/icons/play-button.png", 24));
        } else {
            AL10.alSourcePlay(alSource);
            playPauseBtn.setIcon(loadIcon("/icons/pause-button.png", 24));
        }
    }

    public void showVolumePopup() {
        if (skipNextVolumeShow) {
            skipNextVolumeShow = false;
            return;
        }
        volumePopup.show(
                volumeBtn,
                (volumeBtn.getWidth() - volumeSlider.getPreferredSize().width) / 2,
                -volumeSlider.getPreferredSize().height - 10
        );
    }

    private void startVideo() {
        startAudio();
        new Thread(() -> {
            try {
                File[] files = new File(getClass().getResource("/background/background-frames/").toURI()).listFiles();
                Arrays.sort(files);
                while (true) {
                    for (File file : files) {
                        BufferedImage image = ImageIO.read(file);
                        buffer.put(image);
                    }
                }
            } catch (IOException | InterruptedException | URISyntaxException ex) {
                ex.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                while (true) {
                    if (!paused) {
                        currentFrame = buffer.take();
                        repaint();
                        Thread.sleep(1000 / 24);
                    } else {
                        Thread.sleep(100);
                    }
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    private void startAudio() {
        try {
            // Init OpenAL device et context
            alDevice = ALC10.alcOpenDevice((ByteBuffer) null);
            ALCCapabilities alcCaps = ALC.createCapabilities(alDevice);
            alContext = ALC10.alcCreateContext(alDevice, (IntBuffer) null);
            ALC10.alcMakeContextCurrent(alContext);
            AL.createCapabilities(alcCaps);

            // Lecture du WAV via AudioSystem (décodage uniquement)
            File bgAudio = new File(getClass().getResource("/background/background.wav").toURI());
            AudioInputStream ais = AudioSystem.getAudioInputStream(bgAudio);
            AudioFormat fmt = ais.getFormat();
            byte[] bytes = ais.readAllBytes();
            ByteBuffer audioData = BufferUtils.createByteBuffer(bytes.length).put(bytes).flip();

            // Format OpenAL selon canaux et profondeur
            int alFormat;
            if (fmt.getChannels() == 1) {
                alFormat = fmt.getSampleSizeInBits() == 8 ? AL10.AL_FORMAT_MONO8 : AL10.AL_FORMAT_MONO16;
            } else {
                alFormat = fmt.getSampleSizeInBits() == 8 ? AL10.AL_FORMAT_STEREO8 : AL10.AL_FORMAT_STEREO16;
            }

            // Création buffer + source OpenAL
            alBuffer = AL10.alGenBuffers();
            AL10.alBufferData(alBuffer, alFormat, audioData, (int) fmt.getSampleRate());

            alSource = AL10.alGenSources();
            AL10.alSourcei(alSource, AL10.AL_BUFFER, alBuffer);
            AL10.alSourcei(alSource, AL10.AL_LOOPING, AL10.AL_TRUE);
            AL10.alSourcef(alSource, AL10.AL_GAIN, 0.3f);
            AL10.alSourcePlay(alSource);

        } catch (IOException | URISyntaxException | UnsupportedAudioFileException ex) {
            ex.printStackTrace();
        }
    }

    private ImageIcon loadIcon(String path, int size) {
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(path)));
        Image scaled = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
}