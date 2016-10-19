package com.matfyz.snarkmaster.ui.controlPanel

import java.awt.GridLayout
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing._
import javax.swing.border.EmptyBorder

import akka.actor.ActorRef
import com.matfyz.snarkmaster.model.{GraphFileSelected, StartTestGraphs}
import com.matfyz.snarkmaster.test.{StartColoringTest, StartTestMessage}

object GraphTestPanel{
  val panel = new JPanel()
  val layout = new GridLayout(4, 3)
  panel.setBorder(new EmptyBorder(20,20,20,20))
  panel.setLayout(layout)

  // Row 1
  val graphInputName = new JLabel()
  val graphInputButton = new JButton("Open")
  panel.add(new JLabel("Graph: "))
  panel.add(graphInputName)
  panel.add(graphInputButton)

  // Row 2
  val coloringTestCB = new JCheckBox()
  val coloringTestStatus = new JLabel()
  panel.add(new JLabel("Coloring test: "))
  panel.add(coloringTestCB)
  panel.add(coloringTestStatus)

  // Row 3
  val removabilityTestCB = new JCheckBox()
  removabilityTestCB.setEnabled(false)
  val removabilityTestStatus = new JLabel()
  panel.add(new JLabel("Removability test: "))
  panel.add(removabilityTestCB)
  panel.add(removabilityTestStatus)

  // Row 4
  val testGraphStartButton = new JButton("Start Graph Test")
  panel.add(testGraphStartButton)
  panel.add(new JLabel())
  panel.add(new JLabel())

  def init(uiActor: ActorRef) = {
    graphInputButton.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        val fileChooser = new JFileChooser()
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY)
        fileChooser.showOpenDialog(panel)
        if(fileChooser.getSelectedFile != null)
          uiActor ! GraphFileSelected(fileChooser.getSelectedFile)
      }
    })

    testGraphStartButton.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        var test = Seq[StartTestMessage]()
        if(coloringTestCB.isSelected) test :+= StartColoringTest
        uiActor ! StartTestGraphs(test)
      }
    })

    panel
  }
}