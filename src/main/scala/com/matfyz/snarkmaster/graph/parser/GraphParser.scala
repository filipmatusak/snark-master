package com.matfyz.snarkmaster.graph.parser

import akka.actor.{Actor, ActorRef}
import com.matfyz.snarkmaster.graph.parser.format.SimpleTripleFormat
import com.matfyz.snarkmaster.model.ParseGraph

class GraphParser(listener: ActorRef) extends Actor{
  override def receive: Receive = {
    case ParseGraph(file) => listener ! SimpleTripleFormat.parse(file)
  }
}

object GraphParser{
  val name = "graph-parser"
}