package com.matfyz.snarkmaster.test

import com.matfyz.snarkmaster.configuration.Configuration
import com.matfyz.snarkmaster.graph.{Component, Graph}

trait SnarkTest

trait StartTestMessage{
  def start(graphs: Seq[Graph], configuration: Configuration): Seq[SnarkTestResult]
}

trait SnarkColoringTest extends SnarkTest{
  def test(graph: Seq[Graph], configuration: Configuration): Seq[SnarkTestResult] =
    graph.map(g => test(g, configuration))

  def test(graph: Graph, configuration: Configuration): SnarkTestResult
}

trait StartComponentTestMessage{
  def start(component: Component, configuration: Configuration): Seq[SnarkTestResult]
}

trait SnarkTransitionTest extends SnarkTest{
  def test(component: Component, configuration: Configuration): Seq[SnarkTestResult]
}

trait SnarkTestResult
case class WithoutColoring(graph: Graph) extends SnarkTestResult
case class ColoringExists(coloring: Seq[(Int, Int , Int)], graph: Graph) extends SnarkTestResult
case class TransitionResult(graph: Graph,
                            configuration: Configuration,
                            transitions: Set[Seq[Configuration.THFactors]],
                            edgeVertices: Seq[Int]) extends SnarkTestResult


