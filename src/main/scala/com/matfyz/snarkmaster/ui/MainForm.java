package com.matfyz.snarkmaster.ui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;

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
    public JRadioButton configurationSelection1;
    public JRadioButton configurationSelection2;
    public JButton startTransitionExistTestButton;
    public JLabel transitionExistTestStatus;
    public JRadioButton configurationSelection3;

    public void run() {
        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        ButtonGroup configurationSelectionGroup1 = new ButtonGroup();
        configurationSelectionGroup1.add(configurationSelection2);
        configurationSelectionGroup1.add(configurationSelection1);
        configurationSelectionGroup1.add(configurationSelection3);
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        panel1.setPreferredSize(new Dimension(600, 600));
        tabbedPane1 = new JTabbedPane();
        panel1.add(tabbedPane1, BorderLayout.NORTH);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(20, 20, 20, 20), 20, 20));
        tabbedPane1.addTab("Coloring", panel2);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(5, 3, new Insets(0, 0, 0, 0), 10, 10));
        panel2.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, true));
        final JLabel label1 = new JLabel();
        label1.setText("Graph:");
        panel3.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        graphInputName = new JLabel();
        graphInputName.setRequestFocusEnabled(true);
        graphInputName.setText("");
        panel3.add(graphInputName, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        selectGraphButton = new JButton();
        selectGraphButton.setText("Select graph");
        panel3.add(selectGraphButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        startColoringTestButton = new JButton();
        startColoringTestButton.setText("Start test");
        panel3.add(startColoringTestButton, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Configuration:");
        panel3.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        coloringTestStatus = new JLabel();
        coloringTestStatus.setText("");
        panel3.add(coloringTestStatus, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        configurationSelection1 = new JRadioButton();
        configurationSelection1.setSelected(true);
        configurationSelection1.setText("Tetrahedral");
        panel3.add(configurationSelection1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        configurationSelection2 = new JRadioButton();
        configurationSelection2.setText("Extended tetrahedral");
        panel3.add(configurationSelection2, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        configurationSelection3 = new JRadioButton();
        configurationSelection3.setText("Three Coloring");
        panel3.add(configurationSelection3, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 1, new Insets(20, 20, 20, 20), 20, 20));
        tabbedPane1.addTab("Transition", panel4);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), 10, 10));
        panel4.add(panel5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, true));
        final JLabel label3 = new JLabel();
        label3.setText("Component:");
        panel5.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        inputComponentName = new JLabel();
        inputComponentName.setRequestFocusEnabled(true);
        inputComponentName.setText("");
        panel5.add(inputComponentName, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        selectComponentButton = new JButton();
        selectComponentButton.setText("Select component");
        panel5.add(selectComponentButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        startTransitionTestButton = new JButton();
        startTransitionTestButton.setText("Start test");
        panel5.add(startTransitionTestButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        transitionTestStatus = new JLabel();
        transitionTestStatus.setText("");
        panel5.add(transitionTestStatus, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        startTransitionExistTestButton = new JButton();
        startTransitionExistTestButton.setText("Start existence test");
        panel5.add(startTransitionExistTestButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        transitionExistTestStatus = new JLabel();
        transitionExistTestStatus.setText("");
        panel5.add(transitionExistTestStatus, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        logPane = new JScrollPane();
        logPane.setAutoscrolls(true);
        logPane.setVerticalScrollBarPolicy(22);
        panel1.add(logPane, BorderLayout.SOUTH);
        logTextArea = new JTextArea();
        logTextArea.setAutoscrolls(true);
        logTextArea.setEditable(false);
        logTextArea.setEnabled(true);
        logTextArea.setFocusTraversalPolicyProvider(false);
        logTextArea.setLineWrap(true);
        logTextArea.setMargin(new Insets(1, 10, 1, 10));
        logTextArea.setOpaque(true);
        logTextArea.setRequestFocusEnabled(true);
        logTextArea.setRows(20);
        logTextArea.setSelectedTextColor(new Color(-16777216));
        logTextArea.setText("");
        logTextArea.setVerifyInputWhenFocusTarget(false);
        logTextArea.setWrapStyleWord(true);
        logPane.setViewportView(logTextArea);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
