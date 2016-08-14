package com.matfyz.snarkmaster.ui

import java.awt.BorderLayout
import javax.swing.JFrame

import akka.actor.{Actor, ActorRef, Props}
import akka.event.LoggingReceive
import com.matfyz.snarkmaster.configuration.Configuration
import com.matfyz.snarkmaster.graph.Graph
import com.matfyz.snarkmaster.model._

class UIActor(listener: ActorRef)extends JFrame("SnarkMaster\u2122") with Actor{

  val frame = context.actorOf(Props(new ControlPanelActor(self, this.asInstanceOf[JFrame])), ControlPanelActor.name)


  var graphs: Seq[Graph] = Nil

  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  setSize(1024, 600)
  setLayout(new BorderLayout)

  override def receive: Receive = LoggingReceive {
    case FrameInitialized => setVisible(true)
    case m: GraphFileSelected => listener ! m
    case m: ParsedGraphs =>
      graphs = m.graphs
      frame ! m
    case StartTestGraphs => listener ! TestGraphs(graphs, Configuration.THConfiguration)
  }
}

object UIActor{
  val name = "ui"
}