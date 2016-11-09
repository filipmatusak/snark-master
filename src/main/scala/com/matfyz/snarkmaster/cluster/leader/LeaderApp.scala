package com.matfyz.snarkmaster.cluster.leader

import akka.actor.{ActorSystem, Props}

object LeaderApp extends App{
  val system = ActorSystem("SnarkMaster")

  val clusterGuardian = system.actorOf(Props(new ClusterGuardian))
}
