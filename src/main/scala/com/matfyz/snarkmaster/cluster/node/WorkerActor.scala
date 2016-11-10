package com.matfyz.snarkmaster.cluster.node

import akka.actor.Actor.Receive
import akka.actor.{ActorContext, ActorSystem, Address, RootActorPath}
import akka.cluster.{Cluster, Member}
import akka.cluster.ClusterEvent.{MemberJoined, MemberUp, ReachableMember, UnreachableMember}
import akka.event.LoggingReceive
import com.matfyz.snarkmaster.BaseActor
import com.matfyz.snarkmaster.cluster.{Roles, WaitingForTask}
import com.matfyz.snarkmaster.cluster.leader.{ClusterGuardian, Leader}

import scala.concurrent.duration._

class WorkerActor(parallelism: Int, leaderAddress: Address) extends BaseActor{
  import WorkerActor._

  val cluster = Cluster(context.system)

  override def preStart(): Unit = cluster.subscribe(self, classOf[MemberUp], classOf[UnreachableMember])
  override def postStop(): Unit = cluster.unsubscribe(self)

  override def receive: Receive = LoggingReceive {
    case MemberUp(m) if m.hasRole(Roles.leader) => register(m, parallelism)
    case UnreachableMember(m) if m.hasRole(Roles.leader) => cluster.system.terminate()
    case x => println(x)
  }

  cluster.joinSeedNodes(Vector(leaderAddress))
}

object WorkerActor{
  val name = "worker-actor"

  def register(m: Member, parallelism: Int)(implicit context: ActorContext) = {
    if(m.hasRole(Roles.leader)){
      val clusterGuardian = context.actorSelection(RootActorPath(m.address) / "user" / ClusterGuardian.name)
      clusterGuardian ! "Hello, at your service"

      (1 to parallelism).foreach(_ => clusterGuardian ! WaitingForTask)
    }
  }
}