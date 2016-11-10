package com.matfyz.snarkmaster.cluster.leader

import akka.actor.{ActorSystem, Props}
import akka.cluster.Cluster
import com.typesafe.config.ConfigFactory
import Leader._
import com.matfyz.snarkmaster.cluster.Roles

object LeaderApp extends App{

  val conf = ConfigFactory
    .parseString("akka.remote.netty.tcp.port=4455,"+
      "akka.remote.netty.tcp.hostname=127.0.0.1," +
      "akka.actor.provider=\"akka.cluster.ClusterActorRefProvider\"," +
      s"akka.cluster.roles = [${Roles.leader}]")

  val system = ActorSystem(systemName, conf)

  val clusterGuardian = system.actorOf(Props(new ClusterGuardian), ClusterGuardian.name)

  val cluster = Cluster(system)
  cluster.joinSeedNodes(Vector(cluster.selfAddress))

  println(system)
  println(cluster)
  println(cluster.selfAddress)
  println(clusterGuardian.path)
}

object Leader{
  val systemName = "SnarkMaster"
}