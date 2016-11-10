package com.matfyz.snarkmaster.cluster.leader

import akka.actor.Actor.Receive
import akka.actor.ActorRef
import akka.event.LoggingReceive
import com.matfyz.snarkmaster.BaseActor
import com.matfyz.snarkmaster.cluster.{NewTask, Task, WaitingForTask}

import scala.collection.mutable
import scala.concurrent.Future
import akka.pattern.{ask, pipe}

import scala.util.{Failure, Success}

class JobSchedulerActor extends BaseActor {

  val workers = mutable.Queue[ActorRef]()
  val tasks = mutable.Queue[(ActorRef, Task)]()

  override def receive: Receive = LoggingReceive {
    case WaitingForTask =>
      if(tasks.isEmpty) workers.enqueue(sender())
      else {
        val oldTask = tasks.dequeue()
        (workers.dequeue() ? oldTask._2).onComplete{
          case Success(res) => oldTask._1 ! res
          case Failure(f) =>
            log.error("Task " + tasks + " has failed\n" + f.toString)
            log.warning("Task " + tasks + " rescheduled")
            tasks.enqueue(oldTask)
        }
      }
    case NewTask(t) =>
    case x => println(x)
  }

  val ff: Future[Future[Int]] = ???
}

object JobSchedulerActor{
  val name = "job-scheduler"
}