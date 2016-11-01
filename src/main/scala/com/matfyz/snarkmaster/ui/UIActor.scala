package com.matfyz.snarkmaster.ui

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorRef, Props}
import com.matfyz.snarkmaster.model.{LogMessage, ParseGraph, TestGraphs}

class UIActor(listener: ActorRef, mainFrame: MainForm) extends Actor{
  val logActor = context.actorOf(Props(new LogActor(self, mainFrame)), LogActor.actorName)
  val coloringTabActor = context.actorOf(Props(new ColoringTabActor(self, mainFrame)), ColoringTabActor.actorName)
  val transitionTabActor = context.actorOf(Props(new TransitionTabActor(self, mainFrame)), TransitionTabActor.actorName)

  override def receive: Receive = {
    case m: LogMessage => logActor forward m
    case m: ParseGraph => listener forward m
    case m: TestGraphs => listener forward m
  }
}

object UIActor{
  val actorName = "ui-actor"
}