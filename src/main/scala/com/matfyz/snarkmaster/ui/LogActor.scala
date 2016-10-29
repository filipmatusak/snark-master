package com.matfyz.snarkmaster.ui

import java.awt.event.{InputMethodEvent, InputMethodListener}
import java.text.SimpleDateFormat
import java.util.Date

import akka.actor.{Actor, ActorRef}
import com.matfyz.snarkmaster.model.{LogException, LogResult, LogText}
import com.matfyz.snarkmaster.test.{ColoringExists, SnarkTestResult, WithoutColoring}

class LogActor(listener: ActorRef, mainFrame: MainForm) extends Actor {
  import LogActor._

  val textArea = mainFrame.logTextArea

  override def receive: Receive = {
    case LogText(msg) =>
      textArea.append(logFormat(msg))
    case LogResult(results) =>
      results.foreach(r => textArea.append(logFormat(logResult(r))))
    case LogException(msg, ex) =>
      textArea.append(logFormat(msg))
      ex.foreach(t => textArea.append(logFormat(t.toString)))
  }

  init(mainFrame)
}

object LogActor{
  val actorName = "log-panel"

  private def logResult(result: SnarkTestResult): String = {
    result match {
      case WithoutColoring(graph) => "Graph " + graph.name + " hasn't coloring!"
      case ColoringExists(coloring, graph) =>
        "Graph " + graph.name + " has coloring \n" +
          coloring.map{ x => (x._1, x._2) + " -> " + x._3}.mkString("\t","\n\t","")
    }
  }

  val dateFormater = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss,SSS")

  private def logFormat(msg: String) = {
    dateFormater.format(new Date()) + " :  " + msg + "\n"
  }

  def init(mainForm: MainForm) = {
    mainForm.logTextArea.addInputMethodListener(new InputMethodListener() {
      def inputMethodTextChanged(event: InputMethodEvent): Unit = {
        println("AAAA")
        mainForm.logTextArea.setCaretPosition(mainForm.logTextArea.getDocument.getLength)
      }
      def caretPositionChanged(event: InputMethodEvent): Unit = {
        println("AAAA")

      }
    })
  }
}