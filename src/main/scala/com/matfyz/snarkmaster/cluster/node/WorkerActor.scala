package com.matfyz.snarkmaster.cluster.node

import java.util.UUID

import akka.actor.{ActorContext, ActorSelection, Address, Kill, RootActorPath}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.{MemberUp, UnreachableMember}
import akka.event.LoggingReceive
import com.matfyz.snarkmaster.BaseActor
import com.matfyz.snarkmaster.cluster._
import com.matfyz.snarkmaster.cluster.leader.ClusterGuardian

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.Success

class WorkerActor(parallelism: Int, leaderAddress: Address) extends BaseActor {

  val cluster = Cluster(context.system)

  val activeJobs = scala.collection.mutable.Set[(Int, Future[Any])]()

  val id = UUID.randomUUID()

  var clusterGuardian: ActorSelection = null

  override def preStart(): Unit = cluster.subscribe(self, classOf[MemberUp], classOf[UnreachableMember])
  override def postStop(): Unit = {
    clusterGuardian ! WorkerUnregister(id)
    activeJobs.foreach{case (id,_) => clusterGuardian ! JobFailed(id, "Worker " + self.path + " finished")}
    cluster.unsubscribe(self)
    println("unregister me")
  }

  override def receive: Receive = LoggingReceive {
    case MemberUp(m) if m.hasRole(Roles.leader) =>
      clusterGuardian = context.actorSelection(RootActorPath(m.address) / "user" / ClusterGuardian.name)
      register(clusterGuardian, parallelism)
    case ComputeJob(job) =>
      val originSender = sender()
      val resFuture = Future {
        log.info("Start executing job " + job.id)
        val res = job.task.apply()
        res
      }
      activeJobs += Tuple2(job.id, resFuture)
      resFuture.onComplete(_ => activeJobs.remove(Tuple2(job.id, resFuture)))

      resFuture.onComplete{
        case res: Success[Any] => originSender ! FinishedJob(job.id, res)
        case f => originSender ! JobFailed(job.id, f.toString)
      }
    case KillJobs => self ! Kill
    case LogStats => logStats()
    case x => println(x)
  }

  cluster.joinSeedNodes(Vector(leaderAddress))

  def logStats() = {
    log.info("Stats:\n" +
      "actice jobs: " + activeJobs.map(_._1).toString)
  }

  def register(clusterGuardian: ActorSelection, parallelism: Int)(implicit context: ActorContext) = {
    clusterGuardian ! "Hello, at your service"

    (1 to parallelism).foreach(_ => clusterGuardian.!(WaitingForJob(id)))
  }

  context.system.scheduler.schedule(0.second, 10.seconds, self, LogStats)
}

object WorkerActor{
  val name = "worker-actor"
}