package com.matfyz.snarkmaster.graph

import com.matfyz.snarkmaster.SnarkMasterException

class Graph {
  var vertices: Map[Int, Vertex] = Map()
  var edges: Set[Edge] = Set()

  def addVertex(id: Int) = {
    vertices.get(id) match {
      case Some(v) => throw new SnarkMasterException("Vertex already exists")
      case _ => vertices = vertices + (id -> Vertex(id))
    }
  }

  def addEdge(uId: Int, vID: Int) = {
    (vertices.get(uId), vertices.get(vID)) match {
      case (Some(u), Some(v)) => edges += Edge(u, v)
      case _ => throw new SnarkMasterException("Vertex doesn't exist")
    }
  }
}

case class Vertex(id: Int)

case class Edge(u: Vertex, v: Vertex)

object Graph{
  def apply(edges: (Int, Int)*) = {}
}

