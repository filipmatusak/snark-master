package com.matfyz.snarkmaster.test

import akka.actor.{Actor, ActorRef}
import akka.event.LoggingReceive
import com.matfyz.snarkmaster.model.{LogResult, LogText, TestGraphs}

import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global

class TestGuardianActor(listener: ActorRef) extends Actor{
  override def receive: Receive = LoggingReceive{
    case TestGraphs(graphs, configuration, tests) =>
      val originSender = sender
      Future{
        tests.foreach{ test =>
          listener ! LogText("Start test graphs: " + graphs.map(_.name).mkString(", "))
          val testResult = test.start(graphs, configuration)
          originSender ! testResult
        }
      }
  }
}

object TestGuardianActor{
  val actorName = "test-guardian"
}
