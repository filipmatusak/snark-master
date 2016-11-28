package com.matfyz.snarkmaster

import java.util.concurrent.TimeoutException

import akka.actor.SupervisorStrategy._
import akka.actor._
import akka.util.Timeout

import scala.concurrent.duration._

trait BaseActor extends Actor with ActorLogging{
  implicit val timeout = Timeout(5.seconds)

  implicit val ctx = context.dispatcher

  implicit val Sender = self

  import context.dispatcher

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1.minute) {
      case _: ActorInitializationException => Restart
      case _: IllegalArgumentException => Restart
      case _: IllegalStateException    => Restart
      case _: TimeoutException         => Restart
      case _: Exception                => Restart
    }
}
