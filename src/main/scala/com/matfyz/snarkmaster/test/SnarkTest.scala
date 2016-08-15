package com.matfyz.snarkmaster.test

import com.matfyz.snarkmaster.configuration.Configuration
import com.matfyz.snarkmaster.graph.{Edge, Graph}
import com.matfyz.snarkmaster.model.Coloring


trait SnarkTest

trait SnarkColoringTest extends SnarkTest{
  def test(graph: Seq[Graph], configuration: Configuration): Seq[SnarkTestResult] =
    graph.map(g => test(g, configuration))

  def test(graph: Graph, configuration: Configuration): SnarkTestResult
}


trait SnarkTestResult
case object WithoutColoring extends SnarkTestResult
case class ColoringExists(coloring: Seq[(Int, Int , Int)]) extends SnarkTestResult


