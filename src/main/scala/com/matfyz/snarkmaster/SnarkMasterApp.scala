package com.matfyz.snarkmaster

import akka.actor.{ActorSystem, Props}
import com.matfyz.snarkmaster.ui.MainForm

object SnarkMasterApp extends App{
 val system = ActorSystem("SnarkMaster")

 val mainFrame = new MainForm()
   mainFrame.run()

 val nodeGuardian = system.actorOf(Props(new NodeGuardian(mainFrame)), NodeGuardian.name)

}
