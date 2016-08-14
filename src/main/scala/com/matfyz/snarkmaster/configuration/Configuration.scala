package com.matfyz.snarkmaster.configuration

case class Configuration(blocks: Set[Block]){
  def colours = blocks.flatMap(_.points).size
}

case class Block(points: Set[Int])

object Block{
  def apply(p: Int*): Block = Block(p.toSet)
}

object Configuration{
  val THConfiguration = Configuration(Set(
    Block(0, 1, 2),
    Block(0, 3, 7),
    Block(0, 4, 9),
    Block(2, 5, 7),
    Block(2, 6, 9),
    Block(7, 8, 9)
  ))
}


