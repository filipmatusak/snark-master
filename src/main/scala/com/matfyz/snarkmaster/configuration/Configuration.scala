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

  val extendedTHConfiguration = Configuration(Set(
    Block(0, 1, 2),
    Block(0, 3, 7),
    Block(0, 4, 9),
    Block(2, 5, 7),
    Block(2, 6, 9),
    Block(7, 8, 9),
    Block(1, 3, 5),
    Block(3, 4, 8),
    Block(5, 6, 8),
    Block(1, 4, 6)
  ))

  sealed trait THFactors
  case object Edge extends THFactors
  case object MidPoint extends THFactors
  case object CornerPoint extends THFactors
  case object Angle extends THFactors
  case object Half_line extends THFactors
  case object Axis extends THFactors
  case object Altitude extends THFactors

  val mids = Seq(1, 3, 4, 5, 6, 8)
  val corners = Seq(0, 2, 7, 9)

  def mapToFactor(t: (Int, Int)): THFactors = {
    val norm = if(t._1 > t._2) t.swap else t
    norm match {
      case (a, b) if a != b && corners.contains(a) && corners.contains(b) => Edge
      case (a, b) if a == b && mids.contains(a) => MidPoint
      case (a, b) if a == b && corners.contains(a) => CornerPoint
      case (1, 8) | (3, 4) | (4, 5) => Axis
      case (a, b) if mids.contains(a) && mids.contains(b) => Angle
      case (a, b) if THConfiguration.blocks.exists(x => x.points.contains(a) && x.points.contains(b)) => Half_line
      case (a, b) if (mids.contains(a) && corners.contains(b)) || (mids.contains(b) && corners.contains(a)) &&
        !THConfiguration.blocks.exists(x => x.points.contains(a) && x.points.contains(b)) => Altitude

    }
  }
}


