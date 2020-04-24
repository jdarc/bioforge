package com.zynaps.demo.packman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

class MainFrame extends JFrame {

    private final Controller controller;
    private final Visualizer surface;

    public MainFrame() {
        setTitle("Genetic Algorithm - Packman");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    controller.reset();
                }
            }
        });
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                new Timer(0, ignore -> controller.evolve()).start();
                new Timer(33, ignore -> render()).start();
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
        surface.setFont(this.getFont());
        surface.setColor(Color.BLACK);
        surface.drawString("Generation: " + controller.getGeneration() + ", Fitness: " + controller.getFitness(), 10, 20);
        surface.repaint();
    }
}
