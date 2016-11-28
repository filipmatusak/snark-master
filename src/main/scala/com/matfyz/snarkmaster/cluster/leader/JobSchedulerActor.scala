package com.matfyz.snarkmaster.cluster.leader

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

  val workers = mutable.Queue[ActorRef]()
  val jobs = mutable.Queue[(ActorRef, Job)]()

  override def receive: Receive = LoggingReceive {
    case WaitingForJob =>
      log.info("Worker " + sender().path + " wait for task")
      workers.enqueue(sender())
      if(jobs.nonEmpty) runJob()
    case ComputeJob(j) =>
      log.info("Job " + j.id + " scheduled")
      jobs.enqueue((sender(), j))
      if(workers.nonEmpty) runJob()
    case CleanScheduler => context.actorSelection("/user/*") ! KillJobs
    case LogStats => logStats
    case x => println(x)
  }

  def runJob() = {
    val worker = workers.dequeue()
    val job = jobs.dequeue()
    (worker ? ComputeJob(job._2)).onComplete{
      case res: Success[FinishedJob] =>
        if(res.value.jobId != job._2.id)
          throw new SnarkMasterException("Worker " + worker + " responded wrong job result " +
          "expected " + job._2.id + " and get " + res.value.jobId )
        job._1 ! res.value
      case f =>
        log.error("Task " + jobs + " has failed\n" + f.toString)
        log.warning("Task " + jobs + " rescheduled")
        jobs.enqueue(job)
    }
  }

  def logStats() = {
    log.info("Stats:\n" +
      "waiting workers: " + workers.size + "\n" + workers.toString() + "\n" +
      "waiting jobs: " + jobs.map(_._2.id).toString())
  }

  context.system.scheduler.schedule(0.second, 10.seconds, self, LogStats)
}

object JobSchedulerActor{
  val name = "job-scheduler"
}