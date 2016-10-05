package com.matfyz.snarkmaster.graph.parser

import akka.actor.{Actor, ActorRef}
import akka.event.LoggingReceive
import com.matfyz.snarkmaster.graph.parser.format.SimpleTripleFormat
import com.matfyz.snarkmaster.model.{LogException, LogText, ParseGraph, ParsedGraphs}

class GraphParser(listener: ActorRef) extends Actor{
  override def receive: Receive = LoggingReceive {
    case ParseGraph(file) =>
      listener ! LogText("Start parsing file: " + file.getName)
      try {
        sender ! ParsedGraphs(SimpleTripleFormat.parse(file), file)
      } catch {
        case ex: Exception =>
          listener ! LogException("Parsing error!", ex)
      }
  }
}

object GraphParser{
  val name = "graph-parser"
}