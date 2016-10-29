package com.matfyz.snarkmaster

import akka.actor.{Actor, Props}
import akka.event.LoggingReceive
import com.matfyz.snarkmaster.parser.GraphParser
import com.matfyz.snarkmaster.model._
import com.matfyz.snarkmaster.parser.format.{SimpleGraphFromat, TripleGraphFormat}
import com.matfyz.snarkmaster.test.{SnarkTestResult, TestGuardianActor}
import com.matfyz.snarkmaster.ui.{MainForm, UIActor}

import scala.swing.MainFrame

class NodeGuardian(mainForm: MainForm) extends Actor{
  val uiActor = context.actorOf(Props(new UIActor(self, mainForm)), UIActor.actorName)
  val testGuardianActor =  context.actorOf(Props(new TestGuardianActor(self)), TestGuardianActor.actorName)
  val graphParserActor = context.actorOf(Props(new GraphParser(self)), GraphParser.actorName)

  override def receive: Receive = LoggingReceive {
    case m: LogMessage => uiActor forward m
      //todo to file
    case m: ParseGraph => graphParserActor forward m
    case m: TestGraphs => testGuardianActor forward m
    case _ =>
  }

  uiActor ! LogText("SnarkMaster started!")

}

object NodeGuardian {

  val name = "node-guardian"

}
