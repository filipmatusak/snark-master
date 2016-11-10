package com.matfyz.snarkmaster.cluster.leader

import akka.actor.Actor.Receive
import akka.event.LoggingReceive
import com.matfyz.snarkmaster.BaseActor

class JobSchedulerActor extends BaseActor{
  override def receive: Receive = LoggingReceive {
    case _ =>
  }
}

object JobSchedulerActor{
  val name = "job-scheduler"
}