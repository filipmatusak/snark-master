package com.matfyz.snarkmaster.ui

import akka.actor.{Actor, ActorRef, Props}
import com.matfyz.snarkmaster.model.graph.GraphFileSelected

class UIActor(listener: ActorRef) extends Actor{

  val frame = context.actorOf(Props(new FrameActor(self)), "")

  override def receive: Receive = {
    case m: GraphFileSelected => listener forward m
  }

}

object UIActor{
  val name = "ui"
}