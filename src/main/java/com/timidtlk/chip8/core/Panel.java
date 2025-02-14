package com.timidtlk.chip8.core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Panel extends JPanel {
    private Display display;
    private final int SCALE = 10;

    public Panel(Display display) {
        this.display = display;
        setPreferredSize(new Dimension(display.WIDTH * SCALE, display.HEIGHT * SCALE));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < display.HEIGHT; i++) {
            for (int j = 0; j < display.WIDTH; j++) {
                g.setColor(display.getPixel(j, i) ? Color.WHITE : Color.BLACK);

                g.fillRect(j * SCALE, i * SCALE, SCALE, SCALE);
            }
        }
    }

}
