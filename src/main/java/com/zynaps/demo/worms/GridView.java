package com.zynaps.demo.worms;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

class GridView extends JPanel {

    private final BufferedImage image;
    private int generation;
    private double fitness;

    public GridView() {
        setBackground(Color.BLACK);
        setForeground(Color.WHITE);
        setPreferredSize(new Dimension(800, 600));
        setFont(new Font("Space Mono", Font.PLAIN, 15));
        var localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        var defaultScreenDevice = localGraphicsEnvironment.getDefaultScreenDevice();
        image = defaultScreenDevice.getDefaultConfiguration().createCompatibleImage(256, 256);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D gx = (Graphics2D)g;
        gx.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        gx.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        gx.drawImage(image, getWidth() - 512 >> 1, getHeight() - 512 >> 1, 512, 512, this);
        gx.drawString(String.format("Generation: %d, Fitness: %f", generation, fitness), 10, 20);
    }

    public void update(int generation, double fitness, Consumer<BufferedImage> op) {
        this.generation = generation;
        this.fitness = fitness;
        op.accept(image);
        repaint();
    }
}
