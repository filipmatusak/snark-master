package com.matfyz.snarkmaster

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, Matchers, WordSpecLike}


abstract class BaseActorISpec extends TestKit(ActorSystem("MySpec")) with ImplicitSender with Matchers{

}

