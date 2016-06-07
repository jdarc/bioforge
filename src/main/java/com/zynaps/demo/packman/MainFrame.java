package com.zynaps.demo.packman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

class MainFrame extends JFrame {

    private final Controller controller;
    private final Visualizer surface;

    public MainFrame() {
        setTitle("Genetic Algorithm - Packman");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_R:
                        controller.reset();
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
                new Timer(0, ignore -> controller.evolve()).start();
                new Timer(30, ignore -> render()).start();
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }
        });
        surface = new Visualizer();
        getContentPane().add(surface, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        controller = new Controller();
    }

    private void render() {
        surface.clear();

        Shape shape = controller.getShape();
        if (shape != null) {
            for (Edge edge : shape.edges) {
                surface.setColor(Color.RED);
                surface.drawLine(edge.p1.x, edge.p1.y, edge.p2.x, edge.p2.y);
                surface.setColor(Color.BLUE);
                surface.drawLine(edge.center.x, edge.center.y, edge.center.x + edge.normal.x, edge.center.y + edge.normal.y);
            }
            surface.setColor(Color.GREEN);
            List<Circle> circles = controller.getChampion();
            for (Circle circle : circles) {
                surface.drawCircle(circle.x, circle.y, circle.radius);
            }
        }

        surface.setColor(Color.BLACK);
        surface.drawString("Generation: " + controller.getGeneration() + ", Fitness: " + controller.getFitness(), 10, 20);
        surface.repaint();
    }
}
