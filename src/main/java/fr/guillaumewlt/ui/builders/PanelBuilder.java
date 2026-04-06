package fr.guillaumewlt.ui.builders;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PanelBuilder {

    private PanelBuilder() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Dimension size = null;
        private LayoutManager layoutManager = new FlowLayout();
        private List<JComponent> components = new ArrayList<>();
        private boolean visible = false;

        public Builder layout(LayoutManager layout) {
            this.layoutManager = layout;
            return this;
        }

        public Builder size(Dimension size) {
            this.size = size;
            return this;
        }

        public Builder component(JComponent component) {
            this.components.add(component);
            return this;
        }

        public Builder visible(boolean visible) {
            this.visible = visible;
            return this;
        }

        public JPanel build() {
            JPanel panel = new JPanel();
            panel.setLayout(layoutManager);
            if (size != null) {
                panel.setPreferredSize(size);
            }
            if (!components.isEmpty()) {
                for (JComponent component : components) {
                    panel.add(component);
                }
            }
            panel.setVisible(visible);
            return panel;
        }
    }
}
