package com.matfyz.snarkmaster.graph

sealed trait Edge
case class BidirectionalEdge(vertices: Set[Vertex]) extends Edge

object BidirectionalEdge{
  def apply(u: Vertex, v: Vertex): BidirectionalEdge = BidirectionalEdge(Set(u, v))
}
