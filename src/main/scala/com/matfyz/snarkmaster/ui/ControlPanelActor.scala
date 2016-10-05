package com.matfyz.snarkmaster.ui

import java.awt.{BorderLayout, GridBagLayout, GridLayout}
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing._

import scala.swing._
import akka.actor.{Actor, ActorRef}
import akka.event.LoggingReceive
import com.matfyz.snarkmaster.model._
import com.matfyz.snarkmaster.test.{StartColoringTest, StartTestMessage}

class ControlPanelActor(uiActor: ActorRef,
                        frame: JPanel) extends Actor{
  val panel = new JPanel()
  val layout = new GridLayout(1, 0)
  panel.setLayout(layout)

  // Row 1
  val graphInputPanel = new JPanel()
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


  val testStartButton = new JButton("Start Test")
  panel.add(testStartButton)

  //panel.getComponents.foreach(_.setPreferredSize(new Dimension(20, 40)))

  graphInputButton.addActionListener(new ActionListener {
    override def actionPerformed(e: ActionEvent): Unit = {
      val fileChooser = new JFileChooser()
      fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY)
      fileChooser.showOpenDialog(frame)
      if(fileChooser.getSelectedFile != null)
        uiActor ! GraphFileSelected(fileChooser.getSelectedFile)
    }
  })

  testStartButton.addActionListener(new ActionListener {
    override def actionPerformed(e: ActionEvent): Unit = {
      var test = Seq[StartTestMessage]()
      if(coloringTestCB.isSelected) test :+= StartColoringTest
      uiActor ! StartTestGraphs(test)
    }
  })

  frame.add(panel, BorderLayout.NORTH)
  uiActor ! ComponentInitialized

  override def receive: Receive = LoggingReceive {
    case ParsedGraphs(_, file) => graphInputName.setText(file.getName)
    case _ =>
  }
}


object ControlPanelActor{
  val name = "control-panel"
}
