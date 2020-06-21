package com.zynaps.demo.worms;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;
import javax.swing.JPanel;

class GridView extends JPanel {

    private final BufferedImage image;
    private int generation;
    private double fitness;

    public GridView() {
        setBackground(Color.BLACK);
        setForeground(Color.WHITE);
        setPreferredSize(new Dimension(800, 600));
        setFont(new Font("Helvetica Neue", Font.PLAIN, 15));
        var localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        var defaultScreenDevice = localGraphicsEnvironment.getDefaultScreenDevice();
        image = defaultScreenDevice.getDefaultConfiguration().createCompatibleImage(256, 256);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, getWidth() - 512 >> 1, getHeight() - 512 >> 1, 512, 512, this);
        g.drawString(String.format("Generation: %d, Fitness: %f", generation, fitness), 10, 20);
    }

    public void update(int generation, double fitness, Consumer<BufferedImage> op) {
        this.generation = generation;
        this.fitness = fitness;
        op.accept(image);
        repaint();
    }
}
