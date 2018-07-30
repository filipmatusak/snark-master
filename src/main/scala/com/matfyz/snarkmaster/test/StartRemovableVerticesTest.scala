package com.matfyz.snarkmaster.test

import com.matfyz.snarkmaster.common.task
import com.matfyz.snarkmaster.configuration.Configuration
import com.matfyz.snarkmaster.graph.{Graph, Vertex}
import com.matfyz.snarkmaster.solver.LingelingSolver
import com.matfyz.snarkmaster.test.SATColoringTest._
import sat.formulas.{CNF, Var}

case object StartRemovableVerticesTest extends StartTestMessage {
  override def start(graphs: Seq[Graph], configuration: Configuration) = {
    StartRemovableVertices.test(graphs, configuration)
  }
}

object StartRemovableVertices extends SnarkRemovabilityTest{
  override def test(graph: Graph, configuration: Configuration): SnarkTestResult = {

    val tests = graph.vertices.keys.map { v =>
      task{
        (v, testVertex(removeVertex(v, graph), configuration))
      }
    }.map(_.join())

    RemovableVertices(graph, configuration, tests.filter(_._2).map(_._1).toSeq.sorted)
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
      case e =>
        e.printStackTrace()
        false
    }
  }

  def removeVertex(vertex: Int, graph: Graph): Graph = {
    graph.copy(
      vertices = graph.vertices.toSeq
        .filter(_._1 != vertex)
        .map(x => if(x._1 > vertex) x.copy(_1 = x._1 -1, _2 = x._2.copy(id = x._2.id-1)) else x).toMap,
      edges = graph.edges
        .filter(!_.vertices.map(_.id).contains(vertex))
        .map( e => e.copy(vertices = e.vertices.map(x => if(x.id > vertex) x.copy(id = x.id -1) else x)))
    )
  }
}
