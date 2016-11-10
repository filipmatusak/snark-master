package com.matfyz.snarkmaster.cluster.node

import akka.actor.Actor.Receive
import akka.actor.{ActorContext, RootActorPath}
import akka.cluster.{Cluster, Member}
import akka.cluster.ClusterEvent.MemberUp
import akka.event.LoggingReceive
import com.matfyz.snarkmaster.BaseActor
import com.matfyz.snarkmaster.cluster.Roles
import com.matfyz.snarkmaster.cluster.leader.ClusterGuardian

class WorkerActor(parallelism: Int) extends BaseActor{
  import WorkerActor._

  val cluster = Cluster(context.system)

  override def preStart(): Unit = cluster.subscribe(self, classOf[MemberUp])
  override def postStop(): Unit = cluster.unsubscribe(self)

  override def receive: Receive = LoggingReceive {
    case MemberUp(m) => register(m, parallelism)
    case _ =>
  }
}

object WorkerActor{
  val name = "worker-actor"

  def register(m: Member, parallelism: Int)(implicit context: ActorContext) = {
    if(m.hasRole(Roles.leader)){
      val clusterGuardian = context.actorSelection(RootActorPath(m.address) / "user" / ClusterGuardian.name)
      clusterGuardian ! "Hello, at your service"

      (1 to parallelism).foreach(_ => clusterGuardian ! "tu Som")
    }
  }
}