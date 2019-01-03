package com.matfyz.snarkmaster.test

import com.matfyz.snarkmaster.common.task
import com.matfyz.snarkmaster.configuration.Configuration
import com.matfyz.snarkmaster.graph.Graph
import com.matfyz.snarkmaster.solver.LingelingSolver
import com.matfyz.snarkmaster.test.SATColoringTest._
import sat.formulas.{CNF, Var}

case object StartRemovablePairOfVerticesTest extends StartTestMessage {
  override def start(graphs: Seq[Graph], configuration: Configuration) = {
    RemovablePairOfVerticesTest.test(graphs, configuration)
  }
}

object RemovablePairOfVerticesTest extends SnarkRemovabilityTest{
  override def test(graph: Graph, configuration: Configuration): SnarkTestResult = {

    val vertexIndices = graph.vertices.keys.toSeq

    val tests = {
      for {
        i <- vertexIndices.indices
        j <- (i + 1) until vertexIndices.size
      } yield {
        val g1 = graph.removeVertex(i)
        val g = if(j > i) g1.removeVertex(j-1) else g1.removeVertex(j)
        task ((vertexIndices(i), vertexIndices(j)), testGraph(g, configuration))
      }
    }.map(_.join())

    RemovablePairOfVerticesTestResult(graph, configuration, tests.filter(_._2).map(_._1).sorted)
  }

  def testGraph(graph: Graph, configuration: Configuration): Boolean  = {
    try{
      val vertices = graph.getSize
      val colors = configuration.colours

      // matrix vertex x vertex x color
      val edgeVars = (0 until vertices).map(i => (0 until vertices).map { j =>
        if (graph.areNeighbour(i, j)) (0 until colors.size).map(c => Var(s"edge $i $j -> $c"))
        else Nil
      })

      val allConditions = CNF(
        additionalConditions(edgeVars, graph, configuration).clauses ++
          symmetry(edgeVars).clauses ++
          onePerEdge(edgeVars).clauses ++
          uniquePerEdge(edgeVars).clauses ++
          blocksConditions(edgeVars, configuration).clauses
      )

      val solver = new LingelingSolver()

      val solved = solver.solve(edgeVars.flatMap(_.flatten).toSet, allConditions)

      solved match {
        case Some(model) => false
        case None => true
      }
    } catch {
      case e: Throwable =>
        e.printStackTrace()
        false
    }
  }
}
