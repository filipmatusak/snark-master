package com.matfyz.snarkmaster.graph.parser.format

import com.matfyz.snarkmaster.graph.Graph

object SimpleTripleFormat extends GraphFileFormat{
  override def parse(lines: Iterator[String]): Seq[Graph] = {
    val in = lines.filter(!_.startsWith("{"))
      .flatMap(_.split(" ").map(_.toInt))

    val numberOfGraphs = in.next()

    for(graphNumber <- 0 until numberOfGraphs) yield {
      val graph = new Graph

      in.next() // graph number
      val numberOfVertices = in.next

      (0 until numberOfVertices).foreach(graph.addVertex)

      in.take(numberOfVertices * 3)
        .sliding(3, 3)
        .zipWithIndex
        .foreach{ case (n, v) =>
          n.foreach(graph.addEdge(v, _))
        }

      graph
    }
  }
}
