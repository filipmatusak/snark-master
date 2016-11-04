package com.matfyz.snarkmaster.parser.format

import java.io.File

import com.matfyz.snarkmaster.graph.{Component, Graph}

object SimpleComponentFormat extends ComponentFileFormat{
  override def parse(file: File, lines: Iterator[String]): Component = {
    val in = lines.filter(!_.startsWith("{"))
      .map(_.split(" ").map(_.toInt))

    val numberOfVertices = in.next.head

    val graph =
      in.take(numberOfVertices).zipWithIndex.flatMap{ case(neigh, v) => neigh.map((_, v))}
        .foldLeft(new Graph(file.getName)){ case(g, (x,y)) =>
          g.addVertex(x)
            .addVertex(y)
            .addBidirectionalEdge(x, y)
        }


    val numberOfConnectors = in.next().head

    val connectors = in.take(numberOfConnectors).map( x => (x(0), x(1))).toSeq

    Component(graph, connectors)
  }
}
