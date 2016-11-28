package com.matfyz.snarkmaster.cluster

trait ClusterMessage extends Serializable
trait TaskMessage extends ClusterMessage

case object WaitingForJob extends TaskMessage
case class ComputeJob(job: Job) extends TaskMessage
case class FinishedJob(jobId: Int, result: Any) extends TaskMessage
case class JobFailed(jobId: Int, msg: String) extends TaskMessage
case class NewTask(task: Task) extends TaskMessage
case object CleanScheduler extends TaskMessage
case object KillJobs extends TaskMessage

case object LogStats