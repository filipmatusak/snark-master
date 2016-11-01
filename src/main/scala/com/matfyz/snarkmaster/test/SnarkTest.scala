package com.matfyz.snarkmaster.test

import com.matfyz.snarkmaster.configuration.Configuration
import com.matfyz.snarkmaster.graph.Graph

trait SnarkTest

trait StartTestMessage{
  def start(graphs: Seq[Graph], configuration: Configuration): Seq[SnarkTestResult]
}

trait SnarkColoringTest extends SnarkTest{
  def test(graph: Seq[Graph], configuration: Configuration): Seq[SnarkTestResult] =
    graph.map(g => test(g, configuration))

  def test(graph: Graph, configuration: Configuration): SnarkTestResult
}

trait SnarkTestResult
case class WithoutColoring(graph: Graph) extends SnarkTestResult
case class ColoringExists(coloring: Seq[(Int, Int , Int)], graph: Graph) extends SnarkTestResult
case class TransitionResult(graph: Graph,
                            configuration:
                            Configuration, colorings: Seq[Seq[Int]],
                            edgeVertices: Seq[Int]) extends SnarkTestResult


