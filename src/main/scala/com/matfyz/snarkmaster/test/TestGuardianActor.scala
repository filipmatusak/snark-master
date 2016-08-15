package com.matfyz.snarkmaster.test

import akka.actor.{Actor, ActorRef}
import akka.event.LoggingReceive
import com.matfyz.snarkmaster.model.TestGraphs

class TestGuardianActor(listener: ActorRef) extends Actor{
  override def receive: Receive = LoggingReceive{
    case TestGraphs(graphs, configuration) => listener ! SATColoringTest.test(graphs, configuration)
  }
}

object TestGuardianActor{
  val name = "test-guardian"
}
