package com.matfyz.snarkmaster.ui

import java.awt.BorderLayout
import javax.swing.{JFrame, JPanel}

import akka.actor.{Actor, ActorRef, Props}
import akka.event.LoggingReceive
import com.matfyz.snarkmaster.configuration.Configuration
import com.matfyz.snarkmaster.graph.Graph
import com.matfyz.snarkmaster.model._

class UIActor(listener: ActorRef) extends Actor{

  var componentsCount = 0
  var componentInitialized = 0

  val frame = new JFrame("SnarkMaster\u2122")
  val mainPanel = new JPanel()

  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  frame.setSize(800, 400)
  mainPanel.setLayout(new BorderLayout())
  frame.add(mainPanel)

  val controlPanel = createComponent(context.actorOf(Props(new ControlPanelActor(self, mainPanel)), ControlPanelActor.name))
  val logPanel = createComponent(context.actorOf(Props(new LogPanelActor(self, mainPanel)), LogPanelActor.name))

  var graphs: Seq[Graph] = Nil


  override def receive: Receive = LoggingReceive {
    case ComponentInitialized =>
      componentInitialized += 1
      if(componentInitialized == componentsCount){
        frame.setVisible(true)
        (0 until 30).foreach(i => logPanel ! LogText(i.toString))
      }
    case m: GraphFileSelected => listener ! m
    case m: ParsedGraphs =>
      graphs = m.graphs
      controlPanel ! m
    case StartTestGraphs =>
      if(graphs.isEmpty) logPanel ! LogText("Select graph before test!")
      else listener ! TestGraphs(graphs, Configuration.THConfiguration)
    case m: LogMessage => logPanel forward m
  }

  private def createComponent[U](f: => U) = {
    componentsCount += 1
    f
  }
}

object UIActor{
  val name = "ui"

}