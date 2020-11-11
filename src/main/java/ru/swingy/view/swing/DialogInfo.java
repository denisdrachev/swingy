package ru.swingy.view.swing;

import javax.swing.*;

public class DialogInfo {

    public DialogInfo(String text) {
        JLabel label = new JLabel(text);
//        label.setIcon(new ImageIcon("logo.png"));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.BOTTOM);
        label.setHorizontalTextPosition(JLabel.CENTER);
        JOptionPane.showMessageDialog(null, label, "About", JOptionPane.PLAIN_MESSAGE);
    }
}
