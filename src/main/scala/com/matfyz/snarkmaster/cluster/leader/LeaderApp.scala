package com.matfyz.snarkmaster.cluster.leader

import akka.actor.{ActorSystem, Props}
import akka.cluster.Cluster
import com.matfyz.snarkmaster.cluster.Roles
import com.matfyz.snarkmaster.cluster.leader.Leader._
import com.typesafe.config.ConfigFactory

object LeaderApp extends App{

  val selfAddress = args(0)

  val conf = ConfigFactory
    .parseString("akka.remote.netty.tcp.port=4455,"+
      s"akka.remote.netty.tcp.hostname=$selfAddress," +
      "akka.actor.provider=\"akka.cluster.ClusterActorRefProvider\"," +
      s"akka.cluster.roles = [${Roles.leader}]")

  val system = ActorSystem(systemName, conf)

  val clusterGuardian = system.actorOf(Props(new ClusterGuardian), ClusterGuardian.name)

  val cluster = Cluster(system)
  cluster.joinSeedNodes(Vector(cluster.selfAddress))

  println(system)
  println(cluster)
}

object Leader{
  val systemName = "SnarkMaster"
}