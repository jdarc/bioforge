package com.zynaps.demo.circles;

import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;

class MainFrame extends JFrame implements KeyListener, ComponentListener {

    private Controller controller;

    public MainFrame() {
        setTitle("Genetic Algorithm - Circles");
        setBackground(new Color(250, 250, 235));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        setFont(new Font("Space Mono", Font.PLAIN, 15));
        pack();
        setLocationRelativeTo(null);
        setIgnoreRepaint(true);
        addKeyListener(this);
        addComponentListener(this);
    }

    private Controller getController() {
        if (controller == null) {
            controller = new Controller();
        }
        return controller;
    }

    private void reset() {
        getController().reset(getContentPane().getWidth(), getContentPane().getHeight());
    }

    private void update() {
        getController().evolve();
    }

    private void render() {
        var strategy = getBufferStrategy();
        if (strategy == null) {
            createBufferStrategy(2);
            return;
        }

        var g = (Graphics2D) strategy.getDrawGraphics();

        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        var insets = getInsets();
        g.translate(insets.left, insets.top);
        g.clearRect(0, 0, getContentPane().getWidth(), getContentPane().getHeight());

        var controller = getController();

        controller.getStaticCircles().forEach(circle -> render(g, Color.ORANGE, circle));
        render(g, Color.RED, controller.getChampionCircle());

        g.setFont(getFont());
        g.setColor(Color.BLACK);
        g.drawString("Generation: " + controller.getGeneration() + ", Fitness: " + controller.getFitness(), 10, 20);

        g.dispose();

        strategy.show();
    }

    private void render(Graphics2D g, Color pen, Circle circle) {
        var x = circle.x - circle.radius;
        var y = circle.y - circle.radius;
        var r = circle.radius * 2.0;
        Shape oval = new Ellipse2D.Double(x, y, r, r);
        g.setColor(pen);
        g.fill(oval);
        g.setColor(Color.BLACK);
        g.draw(oval);
    }

    @Override
    public void componentResized(ComponentEvent e) {
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
        reset();
        new Timer(8, ignore -> update()).start();
        new Timer(25, ignore -> render()).start();
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_R) {
            reset();
        }
    }
}
