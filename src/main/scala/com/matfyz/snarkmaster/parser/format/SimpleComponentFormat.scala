package com.matfyz.snarkmaster.parser.format

import java.io.File

import com.matfyz.snarkmaster.graph.{Component, Graph}

object SimpleComponentFormat extends ComponentFileFormat{
  override def parse(file: File, lines: Iterator[String]): Seq[Component] = {
    val in = lines.filter(!_.startsWith("{"))
      .map(_.split(" ").map(_.toInt))

    val numberOfComponents = in.next.head

    for ( i <- 0 until numberOfComponents ) yield {
      println("i = " + i)
      // read graph number
      val graphIndex = in.next.head
      println("graph index = " + graphIndex)

      val numberOfVertices = in.next.head
      println("vertices " + numberOfVertices)

      val graph =
        in.take(numberOfVertices).toList.zipWithIndex.flatMap { case (neigh, v) => neigh.map((_, v)) }
          .foldLeft(new Graph(file.getName)) { case (g, (x, y)) =>
            g.addVertex(x)
              .addVertex(y)
              .addBidirectionalEdge(x, y)
          }


      val numberOfConnectors = in.next().head

      println("numberOfConnectors " + numberOfConnectors)

      val connectors = in.take(numberOfConnectors).toList.map(x => (x(0), x(1)))

      println("connectors " + connectors)

      Component(graph, connectors)
    }
  }
}
