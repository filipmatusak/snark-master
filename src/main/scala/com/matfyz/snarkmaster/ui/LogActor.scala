package com.matfyz.snarkmaster.ui

import java.awt.event.{InputMethodEvent, InputMethodListener}
import java.text.SimpleDateFormat
import java.util.Date

import akka.actor.{Actor, ActorRef}
import com.matfyz.snarkmaster.model.{LogException, LogResult, LogText}
import com.matfyz.snarkmaster.test._

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

}

object LogActor{
  val actorName = "log-panel"

  private def logResult(result: SnarkTestResult): String = {
    result match {
      case WithoutColoring(graph) => "Graph " + graph.name + " hasn't coloring!"

      case ColoringExists(coloring, graph) =>
        "Graph " + graph.name + " has coloring \n" +
          coloring.sorted.map{ x => f"(${x._1}%3d, ${x._2}%3d) -> ${x._3}%3d"}.mkString("\t","\n\t","")

      case r: TransitionResult => "Graph " + r.graph.name + " has " + r.transitions.size + " transitions\n" +
        "edge vertices are " + r.edgeVertices.mkString("(", ", ", ")") + "\n" +
        r.rawTransitions.map(x=> x.mkString(", ")).sorted.mkString("\t","\n\t","\n") +
        "edge vertices are " + r.edgeVertices.mkString("(", ", ", ")") + "\n" +
        r.transitions.map(x=> x.mkString(", ")).toSeq.sorted.mkString("\t","\n\t","\n")

      case TransitionExists(graph, _) =>
        "Graph " + graph.name + " has some transition"

      case TransitionDoesntExists(graph, _) =>
        "Graph " + graph.name + " has no transition"
    }
  }

  val dateFormater = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss,SSS")

  private def logFormat(msg: String) = {
    dateFormater.format(new Date()) + " :  " + msg + "\n"
  }
}