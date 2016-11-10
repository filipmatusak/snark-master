package com.matfyz.snarkmaster.cluster

trait ClusterMessage extends Serializable
trait TaskMessage extends ClusterMessage

case object WaitingForTask extends TaskMessage
case class ComputeTask(task: Task) extends TaskMessage
case class NewTask(task: Task) extends TaskMessage


