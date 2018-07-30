package com.matfyz.snarkmaster.test

import com.matfyz.snarkmaster.configuration.{Configuration, Point}
import com.matfyz.snarkmaster.graph.{Component, Graph}

trait StartTestMessage{
  def start(graphs: Seq[Graph], configuration: Configuration): Seq[SnarkTestResult]
}

trait SnarkColoringTest {
  def test(graph: Seq[Graph], configuration: Configuration): Seq[SnarkTestResult] =
    graph.map(g => test(g, configuration))

  def test(graph: Graph, configuration: Configuration): SnarkTestResult
}

trait SnarkRemovabilityTest {
  def test(graph: Seq[Graph], configuration: Configuration): Seq[SnarkTestResult] =
    graph.map(g => test(g, configuration))

  def test(graph: Graph, configuration: Configuration): SnarkTestResult
}

trait StartComponentTestMessage{
  def start(component: Component, configuration: Configuration): Seq[SnarkTestResult]
}

trait SnarkTransitionTest {
  def test(component: Component, configuration: Configuration): Seq[SnarkTestResult]
}

sealed trait SnarkTestResult
case class WithoutColoring(graph: Graph) extends SnarkTestResult
case class ColoringExists(coloring: Seq[(Int, Int , Int)], graph: Graph) extends SnarkTestResult
case class TransitionResult(graph: Graph,
                            configuration: Configuration,
                            transitions: Set[Seq[Configuration.THFactors]],
                            rawTransitions: Seq[Seq[Point]],
                            edgeVertices: Seq[Int]) extends SnarkTestResult
case class TransitionExists(graph: Graph, configuration: Configuration) extends SnarkTestResult
case class TransitionDoesntExists(graph: Graph, configuration: Configuration) extends SnarkTestResult
case class RemovableVertices(graph: Graph, configuration: Configuration, vertices: Seq[Int]) extends SnarkTestResult


