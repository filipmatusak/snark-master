package com.matfyz.snarkmaster.cluster.leader

import akka.actor.{Address, Props}
import akka.cluster.{Cluster, Member}
import akka.cluster.ClusterEvent.{MemberUp, UnreachableMember}
import akka.event.LoggingReceive
import com.matfyz.snarkmaster.BaseActor
import com.matfyz.snarkmaster.cluster.{TaskMessage, WaitingForTask}

class ClusterGuardian extends BaseActor {
  import ClusterGuardian._

  val jobSchedulerActor = context.actorOf(Props(new JobSchedulerActor), JobSchedulerActor.name)

  val cluster = Cluster(context.system)

  override def preStart(): Unit = cluster.subscribe(self, classOf[MemberUp], classOf[UnreachableMember])
  override def postStop(): Unit = cluster.unsubscribe(self)

  override def receive: Receive = LoggingReceive {
    case m: TaskMessage => jobSchedulerActor forward m
    case UnreachableMember(m) => leaveMember(m.address, cluster)
    case x => println(x)
  }

  log.info("Cluster guardian actor started")
}

object ClusterGuardian{
  val name = "cluster-guardian"

  def leaveMember(addr: Address, cluster: Cluster) = {
    cluster.down(addr)
  }
}