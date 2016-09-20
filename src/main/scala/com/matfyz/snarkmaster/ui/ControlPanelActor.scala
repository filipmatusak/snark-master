package com.matfyz.snarkmaster.ui

import java.awt.{BorderLayout, Color}
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing._
import java.io.File

import akka.actor.{Actor, ActorRef}
import akka.event.LoggingReceive
import com.matfyz.snarkmaster.model._

class ControlPanelActor(uiActor: ActorRef,
                        frame: JPanel) extends Actor{

  val graphInputPanel = new JPanel()
  val graphInputName = new JLabel()
  val graphInputButton = new JButton("Open")

  val fileChooser = new JFileChooser()
  val testStartButton = new JButton("Start Test")

  fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY)

  graphInputPanel.add(new JLabel("Graph: "))
  graphInputPanel.add(graphInputName)
  graphInputPanel.add(graphInputButton)
  graphInputPanel.add(testStartButton)

  graphInputButton.addActionListener(new ActionListener {
    override def actionPerformed(e: ActionEvent): Unit = {
      fileChooser.showOpenDialog(frame)
      graphInputName.setText("Loading")

      if(fileChooser.getSelectedFile != null)
        uiActor ! GraphFileSelected(fileChooser.getSelectedFile)
    }
  })

  testStartButton.addActionListener(new ActionListener {
    override def actionPerformed(e: ActionEvent): Unit = uiActor ! StartTestGraphs
  })

//  graphInputPanel.setBackground(Color.ORANGE)

  frame.add(graphInputPanel, BorderLayout.NORTH)
  uiActor ! ComponentInitialized
//  frame.setVisible(true)

  override def receive: Receive = LoggingReceive {
    case ParsedGraphs(_, file) => graphInputName.setText(file.getName)
    case _ =>
  }
}


object ControlPanelActor{
  val name = "control-panel"
}
