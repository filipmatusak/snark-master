package com.matfyz.snarkmaster.cluster.leader

import akka.actor.Props
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.MemberUp
import akka.event.LoggingReceive
import com.matfyz.snarkmaster.BaseActor

class ClusterGuardian extends BaseActor {

  val jobSchedulerActor = context.actorOf(Props(new JobSchedulerActor), JobSchedulerActor.name)

  override def receive: Receive = LoggingReceive {
    case x => println(x)
    case _ =>
  }

  log.info("Cluster guardian actor started")
}

object ClusterGuardian{
  val name = "cluster-guardian"
}