package com.zynaps.demo.worms;

import java.awt.Frame;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

class Program {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            var view = new GridView();
            var controller = new Controller(view);
            var frame = new JFrame("Genetic Programming - Worms");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.addWindowStateListener(e -> controller.replayChampion = (e.getNewState() & Frame.ICONIFIED) != Frame.ICONIFIED);
            frame.add(view);
            frame.pack();
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            controller.start();
        });
    }
}
