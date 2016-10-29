package com.matfyz.snarkmaster.test
import cafesat.api.FormulaBuilder._
import cafesat.api.Formulas.PropVar
import cafesat.api.Solver._
import cafesat.sat.Solver
import com.matfyz.snarkmaster.configuration.Configuration
import com.matfyz.snarkmaster.graph.Graph

case object StartTransitionTest extends StartTestMessage {
  override def start(graphs: Seq[Graph], configuration: Configuration): Seq[SnarkTestResult] = {
    SATTransitionTest.findTransition(graphs.head, configuration)
  }
}

object SATTransitionTest extends SnarkTest {
  def findTransition(graph: Graph, configuration: Configuration): Seq[SnarkTestResult] = {
    val vertices = graph.getSize
    val colors = configuration.colours

    val testedVerices = graph.vertices.filter(x => graph.getNeighbour(x._1).size == 3).keys.toSeq

    Nil
  }
}
