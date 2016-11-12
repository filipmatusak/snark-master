package com.matfyz.snarkmaster.cluster.leader

import akka.actor.{Actor, Props}
import akka.event.LoggingReceive
import akka.pattern.ask
import akka.util.Timeout
import com.matfyz.snarkmaster.BaseActorISpec
import com.matfyz.snarkmaster.cluster._
import org.scalatest.FlatSpecLike

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.Random

class JobSchedulerISpec extends BaseActorISpec with FlatSpecLike{

  import ExecutionContext.Implicits.global
  implicit val timeout = 10.minutes

  behavior of "job scheduling"

  it should "get job results in right order" in {
    val taskCount = 10
    val result = new Array[Int](taskCount)

    val jobScheduler = system.actorOf(Props(new JobSchedulerActor), JobSchedulerActor.name)

    val testWorkerActor = system.actorOf(Props(new Actor {
      override def receive = LoggingReceive {
        case "init" => (0 until 3).foreach(_ => jobScheduler ! WaitingForJob)
        case ComputeJob(t) =>
          println("received task " + t.id)
          val oldSender = sender()
          Future {
            val taskRes = t.task.apply()
            oldSender ! FinishedJob(t.id, taskRes)
            jobScheduler ! WaitingForJob
            println("computed task " + t.id)
          }
      }
    }), "worker")

    testWorkerActor ! "init"

    (0 until taskCount).foreach{ i =>
      result.update(i, -1)

      val duration = Random.nextInt(10) + 1
      println("creating task, duration " + duration)

      def x() = {
        val fut = Future{
          def sleep(time: Long) { Thread.sleep(time) }
          sleep(duration * 1000)
        }
        Await.result(fut, 5.minutes)
      }

      val job = Job(i, new Task(x))

      println("task created")

      implicit val timeout = Timeout(2.minutes)
      (jobScheduler ? ComputeJob(job)).onSuccess{
        case FinishedJob(id, r) =>
          result.update(i, id)
          println("updated " + result.mkString(", "))
        case x => println("x = " + x)
      }
    }

    awaitCond(p = result.zipWithIndex.forall{ case (a, b) => a == b }, max = 120.seconds )
    println(result.mkString(", "))
  }

}
