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
    private JLabel inputComponentName;
    private JButton selectComponentButton;
    private JButton startTransitionTestButton;

    public void run() {
        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
