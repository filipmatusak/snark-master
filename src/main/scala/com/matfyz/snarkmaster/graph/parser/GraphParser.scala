package com.matfyz.snarkmaster.graph.parser

import akka.actor.{Actor, ActorRef}
import akka.event.LoggingReceive
import com.matfyz.snarkmaster.graph.parser.format.SimpleTripleFormat
import com.matfyz.snarkmaster.model.{ParseGraph, ParsedGraphs}

class GraphParser(listener: ActorRef) extends Actor{
  override def receive: Receive = LoggingReceive {
    case ParseGraph(file) => sender ! ParsedGraphs(SimpleTripleFormat.parse(file), file)
  }
}

object GraphParser{
  val name = "graph-parser"
}