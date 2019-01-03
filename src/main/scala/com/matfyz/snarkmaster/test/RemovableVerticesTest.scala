package com.matfyz.snarkmaster.test

import com.matfyz.snarkmaster.common.task
import com.matfyz.snarkmaster.configuration.Configuration
import com.matfyz.snarkmaster.graph.Graph
import com.matfyz.snarkmaster.solver.LingelingSolver
import com.matfyz.snarkmaster.test.SATColoringTest._
import sat.formulas.{CNF, Var}

case object StartRemovableVerticesTest extends StartTestMessage {
  override def start(graphs: Seq[Graph], configuration: Configuration) = {
    RemovableVerticesTest.test(graphs, configuration)
  }
}

object RemovableVerticesTest extends SnarkRemovabilityTest{
  override def test(graph: Graph, configuration: Configuration): SnarkTestResult = {

    val tests = graph.vertices.keys.map { v =>
      task{
        (v, testVertex(graph.removeVertex(v), configuration))
      }
    }.map(_.join())

    RemovableVerticesTestResult(graph, configuration, tests.filter(_._2).map(_._1).toSeq.sorted)
  }

  def testVertex(graph: Graph, configuration: Configuration): Boolean  = {
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
