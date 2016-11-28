package com.matfyz.snarkmaster.cluster.node

import akka.actor.{ActorSystem, Props}
import akka.cluster.Cluster
import com.matfyz.snarkmaster.cluster.Roles
import com.matfyz.snarkmaster.cluster.leader.Leader._
import com.typesafe.config.ConfigFactory

object NodeApp {
  def main(args: Array[String]) {
    println(args.mkString(", "))
    assert(args.length == 3)

    val clusterHostName = args(0)
    val clusterPort = args(1).toInt
    val parallelism = args(2).toInt

    val conf = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=4456,"+
      s"akka.remote.netty.tcp.hostname=127.0.1.1," +
      "akka.actor.provider=\"akka.cluster.ClusterActorRefProvider\"," +
      s"akka.cluster.roles = [${Roles.worker}]")

    val system = ActorSystem(systemName, conf)
    val cluster = Cluster(system)

    val leaderAddress = cluster.selfAddress.copy(
      host = Some(clusterHostName),
      port = Some(clusterPort))

  //  cluster.joinSeedNodes(Vector(leaderAddress))

    val worker = system.actorOf(Props(new WorkerActor(parallelism, leaderAddress)), WorkerActor.name)
  }
}
