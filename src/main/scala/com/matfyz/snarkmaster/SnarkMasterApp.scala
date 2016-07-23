package com.matfyz.snarkmaster

import akka.actor.{ActorSystem, Props}

object SnarkMasterApp extends App{
 val system = ActorSystem("SnarkMaster")

 val nodeGuardian = system.actorOf(Props(new NodeGuardian), NodeGuardian.name)



}

/*
  try {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
  } catch {
    case _: Exception => println("Cannot set look and feel, using the default one.")
  }*/

/* val frame = new Frame

 def main(args: Array[String]) {
   frame.repaint()
 }
*/