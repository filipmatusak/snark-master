package com.matfyz.snarkmaster.ui.controlPanel

import java.awt.event.{ActionEvent, ActionListener}
import java.awt.{BorderLayout, GridLayout}
import javax.swing._
import javax.swing.border.EmptyBorder

import akka.actor.{Actor, ActorRef}
import akka.event.LoggingReceive
import com.matfyz.snarkmaster.model._
import com.matfyz.snarkmaster.test.{StartColoringTest, StartTestMessage}

class ControlPanelActor(uiActor: ActorRef,
                        frame: JPanel) extends Actor{

/*
// Row 4
  val transitionTestCB = new JCheckBox()
  transitionTestCB.setEnabled(false)
  val transitionTestStatus = new JLabel()
  panel.add(new JLabel("Transition test: "))
  panel.add(transitionTestCB)
  panel.add(transitionTestStatus)

  val testStartButton = new JButton("Start Test")
  panel.add(testStartButton)
  panel.add(new JLabel())
  panel.add(new JLabel())*/

  //panel.getComponents.foreach(_.setPreferredSize(new Dimension(20, 40)))

  val graphTestPanel = GraphTestPanel.init(uiActor)

  frame.add(graphTestPanel, BorderLayout.NORTH)
  uiActor ! ComponentInitialized

  override def receive: Receive = LoggingReceive {
    case ParsedGraphs(_, file) => GraphTestPanel.graphInputName.setText(file.getName)
    case _ =>
  }
}


object ControlPanelActor{
  val name = "control-panel"

}

