package fr.guillaumewlt.ui.panels;

import fr.guillaumewlt.parser.SettingsJSONParser;
import fr.guillaumewlt.ui.eventhandler.ButtonHandler;
import fr.guillaumewlt.ui.eventhandler.VolumePopupListener;
import fr.guillaumewlt.ui.eventhandler.VolumeSliderListener;
import fr.guillaumewlt.workflow.LauncherContext;
import org.json.JSONObject;
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
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Animated background panel that plays a video (as pre-extracted frames) with synchronized audio.
 *
 * <p>Architecture — two threads communicate via a bounded {@link ArrayBlockingQueue}:</p>
 * <ul>
 *   <li><b>Producer</b> — reads frame files from {@code /background/background-frames/},
 *       decodes them with {@link javax.imageio.ImageIO} and pushes them into the buffer.
 *       Blocks automatically when the queue is full (backpressure).</li>
 *   <li><b>Consumer</b> — pops frames at 24 FPS, stores them in {@code currentFrame}
 *       and calls {@link #repaint()}. Sleeps 100 ms when paused instead of consuming frames.</li>
 * </ul>
 *
 * <p>Audio is handled by <b>OpenAL via LWJGL</b>, which applies volume changes
 * ({@code AL_GAIN}) with zero latency — unlike {@code javax.sound.sampled.Clip}
 * which goes through a software buffer.</p>
 *
 * <p>Rendering uses a <b>cover/crop</b> strategy: the frame is scaled to fill the
 * panel while preserving its aspect ratio, cropping the overflow.</p>
 */
public class BackgroundPanel extends JPanel {

    private final LauncherContext context;

    // Queue between producer (file reader) and consumer (renderer) — capacity = 30 frames ahead
    private final ArrayBlockingQueue<BufferedImage> buffer;
    // Last frame received from the consumer thread, drawn in paintComponent
    private BufferedImage currentFrame;

    // OpenAL handles — initialized in startAudio()
    private long alDevice;   // physical audio device
    private long alContext;  // OpenAL context bound to the device
    private int alBuffer;    // PCM audio data loaded into OpenAL memory
    private int alSource;    // emitter: controls play/pause/gain

    // True when the video and audio are paused
    private boolean paused;

    private JButton playPauseBtn;
    private JButton volumeBtn;
    private JPopupMenu volumePopup;
    private JSlider volumeSlider;

    // Prevents the volume popup from reopening immediately after being dismissed by a click on volumeBtn
    private boolean skipNextVolumeShow = false;

    // Preloaded to avoid reloading on every slider change event
    private ImageIcon speakerIcon;
    private ImageIcon speakerOffIcon;

    public BackgroundPanel(LauncherContext context) {
        this.context = context;
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

        // Load saved volume and paused state from settings.json
        SettingsJSONParser parser = new SettingsJSONParser(context);
        parser.readSettings();
        int savedVolume = parser.getVolume();
        boolean savedPaused = parser.isVideoPaused();

        startVideo();

        // Apply saved settings after OpenAL is initialized
        SwingUtilities.invokeLater(() -> {
            volumeSlider.setValue(savedVolume);
            if (savedPaused) togglePlayPause();
        });

        // Save volume to settings.json on slider release
        volumeSlider.addChangeListener(e -> {
            if (!volumeSlider.getValueIsAdjusting()) saveSettings();
        });
    }

    /**
     * Draws the current frame using a cover/crop strategy:
     * the image is scaled so that both dimensions fill the panel,
     * then centered (overflow is clipped by Swing automatically).
     * Button positions are also updated here since the panel size
     * is only reliable after the component is shown.
     */
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

    /**
     * Toggles play/pause state for both video and audio.
     * Called by {@link fr.guillaumewlt.ui.eventhandler.ButtonHandler} on playPauseBtn click,
     * and automatically by {@link fr.guillaumewlt.workflow.WorkflowRunner} at STARTING_CLIENT.
     */
    public void togglePlayPause() {
        paused = !paused;
        if (paused) {
            AL10.alSourcePause(alSource);
            playPauseBtn.setIcon(loadIcon("/icons/play-button.png", 24));
        } else {
            AL10.alSourcePlay(alSource);
            playPauseBtn.setIcon(loadIcon("/icons/pause-button.png", 24));
        }
        saveSettings();
    }

    /** Merges volume and videoPaused into settings.json, following the existing pattern. */
    private void saveSettings() {
        if (context == null || context.getLauncherDirs() == null) return;
        File settingsFile = new File(context.getLauncherDirs().configDir().path() + "settings.json");
        try {
            JSONObject settings;
            if (settingsFile.exists()) {
                settings = new JSONObject(Files.readString(settingsFile.toPath()));
            } else {
                settings = new JSONObject();
            }
            settings.put("volume", volumeSlider.getValue());
            settings.put("videoPaused", paused);
            try (FileWriter fw = new FileWriter(settingsFile)) {
                fw.write(settings.toString());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
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

    /**
     * Starts the producer and consumer threads, then initializes audio.
     * Must be called after all UI components are initialized so that
     * the consumer can safely call repaint() and update button icons.
     */
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

    /**
     * Initializes OpenAL and starts looping audio playback.
     *
     * <p>Flow:</p>
     * <ol>
     *   <li>Open the default audio device and create an OpenAL context.</li>
     *   <li>Decode the WAV file to raw PCM bytes using {@link AudioSystem}
     *       (javax.sound.sampled is used only for decoding, not playback).</li>
     *   <li>Upload PCM data to an OpenAL buffer.</li>
     *   <li>Create a source, attach the buffer, set initial gain to 0.3 and loop.</li>
     * </ol>
     */
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