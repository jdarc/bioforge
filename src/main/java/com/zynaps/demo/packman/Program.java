package com.zynaps.demo.packman;

import javax.swing.*;

class Program {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
