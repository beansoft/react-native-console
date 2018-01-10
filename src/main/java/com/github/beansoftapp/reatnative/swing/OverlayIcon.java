package com.github.beansoftapp.reatnative.swing;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Overlay one icon with another one to produce a final one.
 * Modified from https://www.experts-exchange.com/questions/26892583/What-is-the-best-way-to-do-icon-overlay-in-Java.html.
 * @date 2018-01-10
 */
public class OverlayIcon implements Icon {
    private Icon base;
    private java.util.List<Icon> overlays;

    public OverlayIcon(Icon base) {
        this.base = base;
        this.overlays = new ArrayList<>();
    }

    public OverlayIcon add(Icon overlay) {
        overlays.add(overlay);
        return this;
    }

    @Override
    public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
        base.paintIcon(c, g, x, y);
        for (Icon icon : overlays) {
            icon.paintIcon(c, g, x, y);
        }
    }

    @Override
    public int getIconWidth() {
        return base.getIconWidth();
    }

    @Override
    public int getIconHeight() {
        return base.getIconHeight();
    }
}