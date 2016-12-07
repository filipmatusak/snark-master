package com.matfyz.snarkmaster.cluster.leader

import java.util.UUID

import akka.actor.ActorRef
import akka.event.LoggingReceive
import akka.pattern.ask
import akka.util.Timeout
import com.matfyz.snarkmaster.cluster._
import com.matfyz.snarkmaster.{BaseActor, SnarkMasterException}

import scala.collection.mutable
import scala.concurrent.duration._
import scala.util.Success

class JobSchedulerActor extends BaseActor {
  override implicit val timeout = Timeout(5.minutes)

  var executors = mutable.Queue[(ActorRef, UUID)]()
  val jobs = mutable.Queue[(ActorRef, Job)]()

  override def receive: Receive = LoggingReceive {
    case WaitingForJob(id) =>
      log.info(s"Worker ${sender().path} id = $id wait for task")
      executors.enqueue((sender(), id))
      if(jobs.nonEmpty) runJob()
    case ComputeJob(j) =>
      log.info("Job " + j.id + " scheduled")
      jobs.enqueue((sender(), j))
      if(executors.nonEmpty) runJob()
    case CleanScheduler => context.actorSelection("/user/*") ! KillJobs
    case LogStats => logStats()
    case WorkerUnregister(id) =>
      println("unregister " + id)
      executors = executors.filter(_._2 != id)
    case x => println(x)
  }

  def runJob() = {
    val worker = executors.dequeue()
    val job = jobs.dequeue()
    (worker._1 ? ComputeJob(job._2)).onComplete{
      case res: Success[FinishedJob] =>
        if(res.value.jobId != job._2.id)
          throw new SnarkMasterException(s"Worker $worker responded wrong job result " +
          s"expected ${job._2.id} and get ${res.value.jobId}" )
        job._1 ! res.value
      case f =>
        log.error("Task " + job._2.id + " has failed\n" + f.toString)
        log.warning("Task " + job._2.id + " rescheduled")
        jobs.enqueue(job)
    }
  }

  def logStats() = {
    log.info("Stats:\n" +
      "waiting workers: " + executors.size + "\n" + executors.toString() + "\n" +
      "waiting jobs: " + jobs.map(_._2.id).toString())
  }

  context.system.scheduler.schedule(0.second, 10.seconds, self, LogStats)
}

object JobSchedulerActor{
  val name = "job-scheduler"
}