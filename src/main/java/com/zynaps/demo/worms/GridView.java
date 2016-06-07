package com.zynaps.demo.worms;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

class GridView extends JPanel {

    private int generation;
    private double fitness;
    private BufferedImage image;

    public GridView() {
        setBackground(Color.BLACK);
        setForeground(Color.WHITE);
        setPreferredSize(new Dimension(800, 600));
        setFont(new Font("Helvetica Neue", Font.PLAIN, 15));
        image = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, getWidth() - 512 >> 1, getHeight() - 512 >> 1, 512, 512, this);
        g.drawString(String.format("Generation: %d, Fitness: %f", generation, fitness), 10, 20);
    }

    public void update(int generation, double fitness, Consumer<BufferedImage> op) {
        if (!SwingUtilities.isEventDispatchThread()) {
            try {
                SwingUtilities.invokeAndWait(() -> update(generation, fitness, op));
            } catch (InterruptedException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return;
        }
        this.generation = generation;
        this.fitness = fitness;
        op.accept(image);
        repaint();
    }
}
