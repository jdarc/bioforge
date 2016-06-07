package com.zynaps.demo.circles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferStrategy;
import java.util.Collection;

class MainFrame extends JFrame {

    private Controller controller;

    public MainFrame() {
        controller = new Controller();
        setTitle("Genetic Algorithm - Circles");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1440, 900));
        setFont(new Font("Helvetica Neue", Font.PLAIN, 15));
        pack();
        setLocationRelativeTo(null);
        setIgnoreRepaint(true);
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_R:
                        reset();
                        break;
                }
            }
        });
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                reset();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
                new Timer(0, ignore -> update()).start();
                new Timer(25, ignore -> render()).start();
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }
        });
    }

    private void reset() {
        controller.reset(getContentPane().getWidth(), getContentPane().getHeight());
    }

    private void update() {
        controller.evolve();
    }

    private void render() {
        BufferStrategy strategy = getBufferStrategy();
        if (strategy == null) {
            createBufferStrategy(2);
            return;
        }

        Graphics2D g = (Graphics2D)strategy.getDrawGraphics();
        AffineTransform tx = g.getTransform();

        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Insets insets = getInsets();
        g.translate(insets.left, insets.top);
        g.clearRect(0, 0, getContentPane().getWidth(), getContentPane().getHeight());

        Collection<Circle> circles = controller.getStaticCircles();
        for (Circle circle : circles) {
            render(g, Color.ORANGE, circle);
        }
        render(g, Color.RED, controller.getChampionCircle());

        g.setFont(getFont());
        g.setColor(Color.BLACK);
        g.drawString("Generation: " + controller.getGeneration() + ", Fitness: " + controller.getFitness(), 10, 20);

        g.setTransform(tx);
        g.dispose();

        strategy.show();
    }

    private void render(Graphics2D g, Color pen, Circle circle) {
        double x = circle.x - circle.radius;
        double y = circle.y - circle.radius;
        double r = circle.radius * 2.0;
        Shape oval = new Ellipse2D.Double(x, y, r, r);
        g.setColor(pen);
        g.fill(oval);
        g.setColor(Color.BLACK);
        g.draw(oval);
    }
}
