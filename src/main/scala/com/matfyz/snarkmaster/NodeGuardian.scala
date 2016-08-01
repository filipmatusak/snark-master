package com.matfyz.snarkmaster

import akka.actor.{Actor, Props}
import akka.actor.Actor.Receive
import akka.event.LoggingReceive
import com.matfyz.snarkmaster.model.GraphFileSelected
import com.matfyz.snarkmaster.ui.UIActor

class NodeGuardian extends Actor{
  val uiActor = context.actorOf(Props(new UIActor(self)), UIActor.name)

  override def receive: Receive = LoggingReceive {
    case _ =>
  }

}

object NodeGuardian {

  val name = "node-guardian"

}
