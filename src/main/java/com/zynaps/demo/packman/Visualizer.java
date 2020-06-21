package com.zynaps.demo.packman;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import javax.swing.JPanel;

class Visualizer extends JPanel {

    private final ArrayList<Drawable> drawables = new ArrayList<>();
    private Color color = Color.BLACK;
    private final double scale = 50.0;

    Visualizer() {
        setBackground(new Color(250, 250, 250));
        setPreferredSize(new Dimension(1440, 900));
    }

    @Override
    public void paint(Graphics g) {

        var gx = (Graphics2D) g;
        var saved = gx.getTransform();

        gx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gx.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        gx.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        gx.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        gx.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        gx.setFont(getFont());

        var size = getSize();
        var width = size.getWidth();
        var height = size.getHeight();
        var edge = Math.max(width, height);
        gx.clearRect(0, 0, (int) width, (int) height);

        gx.translate(width * 0.5, height * 0.5);
        gx.scale(scale, -scale);
        gx.setStroke(new BasicStroke((float) (1.0 / scale)));

        var e = edge / (2.0 * scale);

        var p = new GeneralPath();
        p.reset();
        for (double l = 1; l < e; l += 1) {
            p.moveTo(-l, e);
            p.lineTo(-l, -e);
            p.moveTo(l, e);
            p.lineTo(l, -e);
            p.moveTo(e, -l);
            p.lineTo(-e, -l);
            p.moveTo(e, l);
            p.lineTo(-e, l);
        }
        gx.setColor(new Color(245, 245, 245));
        gx.draw(p);

        p.reset();
        var notch = 6.0 / scale;
        for (var l = 0.5; l < e; l += 0.5) {
            var notch1 = l != (int) l ? notch * 0.5 : notch;
            p.moveTo(-l, notch1);
            p.lineTo(-l, -notch1);
            p.moveTo(l, notch1);
            p.lineTo(l, -notch1);
            p.moveTo(notch1, -l);
            p.lineTo(-notch1, -l);
            p.moveTo(notch1, l);
            p.lineTo(-notch1, l);
        }
        gx.setColor(new Color(200, 200, 200));
        gx.draw(p);

        p.reset();
        p.moveTo(0, -e);
        p.lineTo(0, e);
        p.moveTo(-e, 0);
        p.lineTo(e, 0);
        gx.setColor(new Color(200, 200, 200));
        gx.draw(p);

        drawables.forEach(thing -> thing.draw(gx));

        gx.setTransform(saved);
    }

    public void clear() {
        drawables.clear();
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void drawLine(double x1, double y1, double x2, double y2) {
        addMarker(x1, y1);
        addMarker(x2, y2);
        addWire(new Line2D.Double(x1, y1, x2, y2));
    }

    public void drawCircle(double x, double y, double radius) {
        addMarker(x, y);
        addWire(new Ellipse2D.Double(x - radius, y - radius, radius * 2, radius * 2));
    }

    public void drawPolyline(double[] x, double[] y) {
        addPolyform(x, y, false);
    }

    public void drawString(String s, int x, int y) {
        drawables.add(new Text(s, x, y));
    }

    private void addPolyform(double[] x, double[] y, boolean closed) {
        if (x == null || y == null || x.length != y.length) {
            return;
        }
        if (x.length < 3) {
            drawLine(x[0], y[0], x[1], y[1]);
            return;
        }
        var path = new GeneralPath();
        path.moveTo(x[0], y[0]);
        addMarker(x[0], y[0]);
        for (var i = 1; i < x.length; i++) {
            path.lineTo(x[i], y[i]);
            addMarker(x[i], y[i]);
        }
        if (closed) {
            path.closePath();
        }
        addWire(path);
    }

    private void addMarker(double x, double y) {
        var offset = 3.0 / scale;
        var size = 6.0 / scale;
        addSolid(new Ellipse2D.Double(x - offset, y - offset, size, size));
    }

    private void addWire(java.awt.Shape shape) {
        drawables.add(new Wire(shape, color));
    }

    private void addSolid(java.awt.Shape shape) {
        drawables.add(new Solid(shape, color));
    }

    private interface Drawable {

        void draw(Graphics2D g);
    }

    private static class Text implements Drawable {

        private final String msg;
        private final int x;
        private final int y;

        private Text(String msg, int x, int y) {
            this.msg = msg;
            this.x = x;
            this.y = y;
        }

        @Override
        public void draw(Graphics2D g) {
            var tx = g.getTransform();
            g.setTransform(new AffineTransform(2, 0, 0, 2, 0, 0));
            g.setColor(Color.black);
            g.drawString(msg, x, y);
            g.setTransform(tx);
        }
    }

    private static class Wire implements Drawable {

        private final java.awt.Shape shape;
        private final Color color;

        public Wire(java.awt.Shape shape, Color color) {
            this.shape = shape;
            this.color = color;
        }

        public void draw(Graphics2D g) {
            g.setColor(color);
            g.draw(shape);
        }
    }

    private static class Solid implements Drawable {

        private final java.awt.Shape shape;
        private final Color color;

        public Solid(java.awt.Shape shape, Color color) {
            this.shape = shape;
            this.color = color;
        }

        public void draw(Graphics2D g) {
            g.setColor(color);
            g.fill(shape);
        }
    }
}
