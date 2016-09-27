package com.matfyz.snarkmaster.ui

import java.awt.{BorderLayout, Dimension}
import java.util.Date
import javax.swing._
import javax.swing.border.{EtchedBorder, TitledBorder}

import akka.actor.{Actor, ActorRef}
import com.matfyz.snarkmaster.model.{ComponentInitialized, LogResult, LogText}
import com.matfyz.snarkmaster.test.{ColoringExists, SnarkTestResult, WithoutColoring}

class LogPanelActor(uiActor: ActorRef,
                    frame: JPanel) extends Actor {
  import LogPanelActor._

  val panel = new JPanel()
  panel.setBorder ( new TitledBorder ( new EtchedBorder () ) )

  val textArea = new JTextArea()
  textArea.setEditable ( false )
  textArea.setSize(new Dimension(760,200))
  textArea.setRows(15)

  textArea.setLineWrap(true)
  textArea.setWrapStyleWord(true)

  val scroll = new JScrollPane(textArea)
  scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS)

  panel.add(scroll)
  frame.add(panel, BorderLayout.SOUTH)
  //panel.setBackground(Color.green)

  override def receive: Receive = {
    case LogText(msg) => textArea.append(logFormat(msg))
      toBottom()
    case LogResult(results) =>
      results.foreach(r => textArea.append(logFormat(logResult(r))))
      toBottom()
  }

  uiActor ! ComponentInitialized

  private def toBottom() = textArea.setCaretPosition(textArea.getDocument.getLength)
}

object LogPanelActor{
  val name = "log-panel"

  private def logResult(result: SnarkTestResult): String = {
    result match {
      case WithoutColoring(graph) => "Graph " + graph.name + " hasn't coloring!"
      case ColoringExists(coloring, graph) =>
        "Graph " + graph.name + " has coloring \n" +
        coloring.map{ x => (x._1, x._2) + " -> " + x._3}.mkString("\t","\n\t","")
    }
  }

  private def logFormat(msg: String) = {
    new Date().toInstant.toString + ": " + msg + "\n"
  }
}


