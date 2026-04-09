package fr.guillaumewlt.utils;

import lombok.Getter;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConsoleUtils {

    @Getter
    private static JTextPane consolePane;

    public static void register(JTextPane pane) {
        consolePane = pane;
    }

    private static final BlockingQueue<String> queue = new LinkedBlockingQueue<>();

    public static final ConsoleOut out = new ConsoleOut();

    public static final ConsoleErr err = new ConsoleErr();

    static {
        // --- Styles de texte ---
        // Erreurs [ERROR] / [FATAL_ERROR] : rouge gras — signale les erreurs et leur stack trace complète
        SimpleAttributeSet errorAttrs = new SimpleAttributeSet();
        StyleConstants.setForeground(errorAttrs, new Color(255, 85, 85));
        StyleConstants.setBold(errorAttrs, true);

        // Étapes du workflow [STEP_STATUS] : bleu gras — marque les changements d'étape du launcher
        SimpleAttributeSet stepStatusAttrs = new SimpleAttributeSet();
        StyleConstants.setForeground(stepStatusAttrs, new Color(85, 85, 255));
        StyleConstants.setBold(stepStatusAttrs, true);

        // Persiste entre les appels à process() pour colorer toute la stack trace d'une erreur
        // (les lignes "at ..." ne contiennent pas [ERROR] mais font partie du même bloc)
        // boolean[] plutôt que boolean : permet la capture et la modification depuis la classe anonyme
        boolean[] inErrorBlock = new boolean[]{false};

        // SwingWorker<Void, String> :
        //   - doInBackground() tourne sur un thread du pool — lit la queue et publie les messages
        //   - process() s'exécute automatiquement sur l'EDT — applique les styles et met à jour le Document
        SwingWorker<Void, String> flusher = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                // queue.take() est bloquant : le thread dort jusqu'au prochain message
                // isCancelled() permet d'arrêter proprement le worker si nécessaire
                while (!isCancelled()) {
                    String msg = queue.take();
                    publish(msg); // SwingWorker accumule les messages et appelle process() par batch
                }
                return null;
            }

            @Override
            protected void process(List<String> chunks) {
                // Appelé sur l'EDT avec les messages accumulés depuis le dernier appel
                // chunks peut contenir plusieurs messages publiés entre deux passages sur l'EDT
                if (consolePane == null) return;
                try {
                    JScrollPane scrollPane = (JScrollPane) consolePane.getParent().getParent();
                    JScrollBar bar = scrollPane.getVerticalScrollBar();
                    boolean isAtBottom = bar.getValue() + bar.getVisibleAmount() >= bar.getMaximum() - 10;

                    Document doc = consolePane.getDocument();

                    for (String msg : chunks) {
                        boolean isNewLogLine = msg.matches("(?s)^\\[\\d{2}:\\d{2}:\\d{2}].*");
                        if (isNewLogLine) {
                            // Met à jour le flag d'erreur pour colorer la stack trace qui suit
                            inErrorBlock[0] = msg.contains("ERROR");
                        }

                        // Sélectionne le style selon le type de message :
                        // erreur (et sa stack trace) > step status > défaut
                        AttributeSet attrs;
                        if (inErrorBlock[0]) {
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
                            doc.insertString(doc.getLength(), msg, attrs);
                        }
                    }

                    if (isAtBottom) {
                        consolePane.setCaretPosition(doc.getLength());
                    }
                } catch (BadLocationException ignored) {}
            }
        };

        flusher.execute();
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
