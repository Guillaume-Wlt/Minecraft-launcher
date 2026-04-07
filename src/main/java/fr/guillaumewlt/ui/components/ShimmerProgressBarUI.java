package fr.guillaumewlt.ui.components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;

/**
 * UI personnalisée pour JProgressBar avec un effet de surbrillance animé (shimmer).
 * <p>
 * Remplace le rendu par défaut de {@link BasicProgressBarUI} par :
 * - Un fond arrondi
 * - Une barre de progression verte arrondie (couleur lue via {@code getForeground()})
 * - Un dégradé blanc semi-transparent qui se déplace de gauche à droite en boucle
 * - Le texte centré en blanc gras par-dessus
 */
public class ShimmerProgressBarUI extends BasicProgressBarUI {

    // Position horizontale actuelle du centre du dégradé shimmer
    private int shimmerOffset = 0;

    // Timer Swing qui cadence l'animation (~50fps)
    private Timer timer;

    @Override
    protected void installDefaults() {
        super.installDefaults();
        // Toutes les 20ms : déplace le shimmer de 3px vers la droite et redessine la barre
        // Le modulo (width + 100) fait revenir le shimmer au début une fois sorti de la barre
        timer = new Timer(20, e -> {
            shimmerOffset = (shimmerOffset + 3) % (progressBar.getWidth() + 100);
            progressBar.repaint();
        });
        timer.start();
    }

    @Override
    protected void uninstallDefaults() {
        super.uninstallDefaults();
        // Arrête le timer quand le UI est détaché du composant pour éviter les fuites mémoire
        timer.stop();
    }

    @Override
    public void paintDeterminate(Graphics g, JComponent c) {
        Graphics2D g2 = (Graphics2D) g.create();
        // Active l'antialiasing pour des bords arrondis lisses
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width  = progressBar.getWidth();
        int height = progressBar.getHeight();
        // Largeur en pixels de la partie remplie selon le pourcentage courant (0.0 - 1.0)
        int filled = (int) (width * progressBar.getPercentComplete());

        // Fond : rectangle arrondi de la couleur de fond du composant (gris FlatLaf par défaut)
        g2.setColor(progressBar.getBackground());
        g2.fillRoundRect(0, 0, width, height, 6, 6);

        // Barre de progression : rectangle arrondi de la couleur définie via setForeground()
        g2.setColor(progressBar.getForeground());
        g2.fillRoundRect(0, 0, filled, height, 6, 6);

        // Effet shimmer : dégradé blanc semi-transparent animé qui se déplace sur la barre
        if (filled > 0) {
            // Clip arrondi pour que le shimmer ne dépasse pas les bords arrondis de la barre
            Shape roundClip = new java.awt.geom.RoundRectangle2D.Float(0, 0, filled, height, 6, 6);
            g2.clip(roundClip);

            // Dégradé à 3 points : transparent → blanc semi-transparent → transparent
            // Les fractions {0, 0.5, 1} répartissent les couleurs sur la largeur du dégradé
            LinearGradientPaint shimmer = new LinearGradientPaint(
                    shimmerOffset - 60, 0, shimmerOffset + 60, 0,
                    new float[]{0f, 0.5f, 1f},
                    new Color[]{
                        new Color(255, 255, 255, 0),   // transparent
                        new Color(255, 255, 255, 60),  // blanc 24% opaque (pic du shimmer)
                        new Color(255, 255, 255, 0)    // transparent
                    }
            );
            g2.setPaint(shimmer);
            g2.fillRect(0, 0, filled, height);
        }

        // Texte centré en blanc gras par-dessus la barre
        if (progressBar.isStringPainted()) {
            g2.setClip(null); // réinitialise le clip pour ne pas couper le texte
            g2.setColor(Color.WHITE);
            g2.setFont(progressBar.getFont().deriveFont(12f));
            FontMetrics fm = g2.getFontMetrics();
            String text = progressBar.getString();
            int x = (width  - fm.stringWidth(text)) / 2;
            int y = (height + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(text, x, y);
        }

        g2.dispose();
    }
}
