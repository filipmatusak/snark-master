package com.matfyz.snarkmaster

import akka.actor.{Actor, Props}
import akka.event.LoggingReceive
import com.matfyz.snarkmaster.graph.parser.GraphParser
import com.matfyz.snarkmaster.model.{GraphFileSelected, ParseGraph, TestGraphs}
import com.matfyz.snarkmaster.test.{SnarkTestResult, TestGuardianActor}
import com.matfyz.snarkmaster.ui.UIActor

class NodeGuardian extends Actor{
  val uiActor = context.actorOf(Props(new UIActor(self)), UIActor.name)
  val testGuardianActor =  context.actorOf(Props(new TestGuardianActor(self)), TestGuardianActor.name)
  val graphParserActor = context.actorOf(Props(new GraphParser(self)), GraphParser.name)

  override def receive: Receive = LoggingReceive {
    case GraphFileSelected(file) => graphParserActor forward ParseGraph(file)
    case m: TestGraphs => testGuardianActor forward m
    case testResult: Seq[SnarkTestResult] =>

    case _ =>
  }

}

object NodeGuardian {

  val name = "node-guardian"

}
