package com.matfyz.snarkmaster.ui

import java.awt.{BorderLayout, Color, Dimension}
import java.util.Date
import javax.swing.plaf.TextUI
import javax.swing._
import javax.swing.border.{EtchedBorder, TitledBorder}

import akka.actor.{Actor, ActorRef}
import com.matfyz.snarkmaster.model.{ComponentInitialized, LogMessage}

class LogPanelActor(uiActor: ActorRef,
                    frame: JPanel) extends Actor {
  val panel = new JPanel()
  panel.setBorder ( new TitledBorder ( new EtchedBorder () ) )

  val textArea = new JTextArea()
  textArea.setEditable ( false )
  textArea.setSize(new Dimension(760,200))
  textArea.setRows(20)

  textArea.setLineWrap(true)
  textArea.setWrapStyleWord(true)

  val scroll = new JScrollPane(textArea)
  scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS)

  panel.add(scroll)
  frame.add(panel, BorderLayout.SOUTH)
  //panel.setBackground(Color.green)

  override def receive: Receive = {
    case LogMessage(msg) =>
      textArea.append(logFormat(msg))
  }

  private def logFormat(msg: String) = {
    new Date().toString + ": " + msg
  }

  uiActor ! ComponentInitialized
}

object LogPanelActor{
  val name = "log-panel"
}


