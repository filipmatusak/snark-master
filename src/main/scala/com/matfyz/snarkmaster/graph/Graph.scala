package com.matfyz.snarkmaster.graph

import com.matfyz.snarkmaster.SnarkMasterException

case class Graph(name: String,
                 vertices: Map[Int, Vertex] = Map(),
                 edges: Set[Edge] = Set()){

  def addVertex(id: Int, failIfExists: Boolean = false): Graph = {
    vertices.get(id) match {
      case None => Graph(name, vertices + (id -> Vertex(id)), edges)
      case Some(v) if failIfExists => throw new SnarkMasterException(s"Vertex $v already exists")
      case _ => this
    }
  }

  def addBidirectionalEdge(uId: Int, vId: Int, failIfExists: Boolean = false): Graph = {
    (vertices.get(uId), vertices.get(vId)) match {
      case (Some(u), Some(v)) =>
        if(edges.contains(Edge(u, v)))
          if(failIfExists) throw new SnarkMasterException("Bidirectional edge already exist")
          else this
        else Graph(name, vertices, edges + Edge(u, v))
      case _ => throw new SnarkMasterException("Vertex doesn't exist")
    }
  }

  def getSize = vertices.size

  def areNeighbour(u: Int, v: Int): Boolean = {
    edges.contains(Edge(vertices(u), vertices(v)))
  }

  def getNeighbour(x: Int): Set[Vertex] = {
    edges.filter(_.vertices.contains(vertices(x))).flatMap(_.vertices).filter(_.id != x)
  }
}

case class Vertex(id: Int)

object Graph{
  def apply(edges: (Int, Int)*): Graph = {
    edges.foldLeft(new Graph("")){case (graph, (u, v)) =>
      graph
        .addVertex(u)
        .addVertex(v)
        .addBidirectionalEdge(u, v)
    }
  }
}

