package fr.guillaumewlt.utils;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConsoleUtils {

    private static JTextPane consolePane;

    public static void register(JTextPane pane) {
        consolePane = pane;
    }

    private static final BlockingQueue<String> queue = new LinkedBlockingQueue<>();

    public static final ConsoleOut out = new ConsoleOut();

    public static final ConsoleErr err = new ConsoleErr();

    static {
        // --- Styles de texte ---
        // Timestamp [HH:MM:SS] : vert gras — identifie visuellement le début de chaque entrée de log
        SimpleAttributeSet timestampAttrs = new SimpleAttributeSet();
        StyleConstants.setForeground(timestampAttrs, new Color(85, 255, 85));
        StyleConstants.setBold(timestampAttrs, true);

        // Erreurs [ERROR] / [FATAL_ERROR] : rouge gras — signale les erreurs et leur stack trace complète
        SimpleAttributeSet errorAttrs = new SimpleAttributeSet();
        StyleConstants.setForeground(errorAttrs, new Color(255, 85, 85));
        StyleConstants.setBold(errorAttrs, true);

        // Étapes du workflow [STEP_STATUS] : bleu gras — marque les changements d'étape du launcher
        SimpleAttributeSet stepStatusAttrs = new SimpleAttributeSet();
        StyleConstants.setForeground(stepStatusAttrs, new Color(85, 85, 255));
        StyleConstants.setBold(stepStatusAttrs, true);

        // Persiste entre les batchs pour colorer toute la stack trace d'une erreur
        // (les lignes "at ..." ne contiennent pas [ERROR] mais font partie du même bloc)
        AtomicBoolean inErrorBlock = new AtomicBoolean(false);

        Thread flusher = new Thread(() -> {
            while (true) {
                try {
                    // Attend 100ms avant chaque flush pour accumuler les messages
                    // et éviter de flooder l'EDT avec un invokeLater par message
                    Thread.sleep(100);

                    // Transfère atomiquement tous les messages de la queue dans un batch local
                    // drainTo() est thread-safe et vide la queue en une seule opération
                    List<String> batch = new ArrayList<>();
                    queue.drainTo(batch);

                    if (!batch.isEmpty() && consolePane != null) {
                        // Envoie le batch à l'EDT (seul thread autorisé à modifier les composants Swing)
                        SwingUtilities.invokeLater(() -> {
                            try {
                                // Récupère le Document du JTextPane — plus efficace que setText()
                                // car insertString() ajoute sans relire tout le contenu existant
                                javax.swing.text.Document doc = consolePane.getDocument();
                                for (String msg : batch) {
                                    // Détecte une nouvelle entrée de log via son timestamp [HH:MM:SS]
                                    // Les lignes de stack trace ("at ...", "fnm: ...") ne matchent pas
                                    boolean isNewLogLine = msg.matches("(?s)^\\[\\d{2}:\\d{2}:\\d{2}\\].*");
                                    if (isNewLogLine) {
                                        // Met à jour le flag d'erreur pour colorer la stack trace qui suit
                                        inErrorBlock.set(msg.contains("ERROR"));
                                    }

                                    // Sélectionne le style selon le type de message :
                                    // erreur (et sa stack trace) > step status > défaut
                                    AttributeSet attrs;
                                    if (inErrorBlock.get()) {
                                        attrs = errorAttrs;
                                    } else if (msg.contains("STEP_STATUS")) {
                                        attrs = stepStatusAttrs;
                                    } else {
                                        attrs = null;
                                    }

                                    if (msg.startsWith("\r")) {
                                        // Comportement "terminal" : \r revient au début de la ligne courante
                                        // On supprime la dernière ligne du document et on la remplace
                                        // Utilisé par DownloadProgress pour mettre à jour la barre en place
                                        String clean = msg.substring(1); // supprime le \r
                                        int end = doc.getLength();
                                        String docText = doc.getText(0, end);
                                        int start = docText.lastIndexOf('\n') + 1; // début de la dernière ligne
                                        doc.remove(start, end - start); // efface la dernière ligne
                                        doc.insertString(start, clean, attrs); // écrit la nouvelle
                                    } else {
                                        if (isNewLogLine) {
                                            // Sépare le timestamp du reste pour lui appliquer sa propre couleur
                                            doc.insertString(doc.getLength(), msg.substring(0, 10), timestampAttrs);
                                            doc.insertString(doc.getLength(), msg.substring(10), attrs);
                                        } else {
                                            // Ligne de stack trace ou message sans timestamp : style uniforme
                                            doc.insertString(doc.getLength(), msg, attrs);
                                        }
                                    }
                                }
                            } catch (javax.swing.text.BadLocationException ignored) {}
                        });
                    }
                } catch (InterruptedException ignored) {}
            }
        });
        // Daemon : le thread s'arrête automatiquement quand le programme se ferme
        flusher.setDaemon(true);
        flusher.start();
    }

    public static class ConsoleOut {

        public void print(String message) {
            if (consolePane != null) {
                queue.add(message);
            } else {
                System.out.println(message); // Fallback
            }
        }

        public void println(String message) {
            print(message + "\n");
        }
    }

    public static class ConsoleErr {
        public void print(String message) {
            if (consolePane != null) {
                queue.add(message);
            } else {
                System.err.println(message); // Fallback
            }
        }

        public void println(String message) {
            print(message + "\n");
        }
    }
}
