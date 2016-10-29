package com.matfyz.snarkmaster.test

import akka.actor.{Actor, ActorRef}
import akka.event.LoggingReceive
import com.matfyz.snarkmaster.model.{LogResult, LogText, TestGraphs}

class TestGuardianActor(listener: ActorRef) extends Actor{
  override def receive: Receive = LoggingReceive{
    case TestGraphs(graphs, configuration, tests) =>
      tests.foreach{ test =>
        listener ! LogText("Start test graphs: " + graphs.map(_.name).mkString(", "))
        val testResult = test.start(graphs, configuration)
        sender ! testResult
      }
  }
}

object TestGuardianActor{
  val actorName = "test-guardian"
}
