package com.zynaps.demo.worms;

import javax.swing.*;
import java.awt.*;

class Program {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GridView view = new GridView();
            Controller controller = new Controller(view);
            JFrame frame = new JFrame("Genetic Programming - Worms");
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
