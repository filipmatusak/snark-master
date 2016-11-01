package com.matfyz.snarkmaster.parser

import java.io.File

import akka.actor.{Actor, ActorRef}
import akka.event.LoggingReceive
import com.matfyz.snarkmaster.parser.format.{GraphFileFormat, SimpleGraphFromat, TripleGraphFormat}
import com.matfyz.snarkmaster.model._

class GraphParser(listener: ActorRef) extends Actor{
  override def receive: Receive = LoggingReceive {
    case ParseGraph(file, format) =>
      listener ! LogText("Start parsing file: " + file.getName)
      try {
        sender ! ParsedGraphs(format.parse(file), file)
      } catch {
        case ex: Exception =>
          listener ! LogException("Parsing error!", Some(ex))
      }
  }
}

object GraphParser{
  val actorName = "graph-parser"
}