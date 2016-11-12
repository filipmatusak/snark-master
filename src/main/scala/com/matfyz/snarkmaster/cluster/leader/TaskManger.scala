package com.matfyz.snarkmaster.cluster.leader

import akka.actor.ActorRef
import akka.event.LoggingReceive
import akka.pattern.ask
import com.matfyz.snarkmaster.cluster.{ComputeJob, FinishedJob, Job, NewTask}
import com.matfyz.snarkmaster.{BaseActor, SnarkMasterException}

import scala.util.Success

class TaskManger(jobSchedulerActor: ActorRef) extends BaseActor {
  var jobId = 0

  override def receive = LoggingReceive {
    case NewTask(t) =>
      val oldSender = sender()
      log.info("New task " + jobId)
      val job = Job(jobId, t)
      jobId = jobId + 1
      scheduleJob(job, oldSender)
  }

  def scheduleJob(job: Job, s: ActorRef) = {
    (jobSchedulerActor ? ComputeJob(job)).onComplete{
      case res: Success[FinishedJob] =>
        if(res.value.jobId != job.id) throw new SnarkMasterException("JobScheduler responded wrong job result " +
          "expected " + job.id + " and get " + res.value.jobId )
        s ! res.value.result
      case f =>
        log.error("Job " + job + " has failed\n" + f.toString)
    }
  }
}
