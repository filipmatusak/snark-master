package com.matfyz.snarkmaster.cluster

class Task(f: => Any) extends Serializable {
  def apply(): Any = f
}

case class Job(id: Int, task: Task) extends Serializable
