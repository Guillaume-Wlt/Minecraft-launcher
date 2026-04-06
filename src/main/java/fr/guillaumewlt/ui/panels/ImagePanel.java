package fr.guillaumewlt.ui.panels;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {

    private JLabel imageLabel;

    public ImagePanel() {
        imageLabel = new JLabel("IMAGE PLACEHOLDER");
        imageLabel.setForeground(Color.GRAY);
        imageLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        setLayout(new GridBagLayout());
        setBackground(new Color(180, 180, 180));
        add(imageLabel);
    }
}
