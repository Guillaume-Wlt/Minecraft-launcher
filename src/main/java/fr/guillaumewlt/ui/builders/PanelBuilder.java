package fr.guillaumewlt.ui.builders;

import javax.swing.*;
import java.awt.*;

public class PanelBuilder {

    private PanelBuilder() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Dimension size = new Dimension(800, 600);
        private LayoutManager layoutManager = new FlowLayout();

        public Builder layout(LayoutManager layout) {
            this.layoutManager = layout;
            return this;
        }

        public Builder size(Dimension size) {
            this.size = size;
            return this;
        }

        public JPanel build() {
            JPanel panel = new JPanel();
            panel.setLayout(layoutManager);
            panel.setPreferredSize(size);
            return panel;
        }
    }
}
