package com.matfyz.snarkmaster.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;

public class MainForm {
    public JPanel panel1;
    public JTabbedPane tabbedPane1;
    public JTextArea logTextArea;
    public JScrollPane logPane;
    public JButton selectGraphButton;
    public JLabel graphInputName;
    public JButton startColoringTestButton;
    public JLabel coloringTestStatus;
    public JLabel inputComponentName;
    public JButton selectComponentButton;
    public JButton startTransitionTestButton;
    public JLabel transitionTestStatus;

    public void run() {
        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
