package com.matfyz.snarkmaster.test
import cafesat.api.FormulaBuilder._
import cafesat.api.Formulas.{Formula, PropVar}
import cafesat.api.Solver._
import com.matfyz.snarkmaster.common._
import com.matfyz.snarkmaster.configuration.{Configuration, Point}
import com.matfyz.snarkmaster.graph.Component

case object StartTransitionTest extends StartComponentTestMessage{
  override def start(component: Component, configuration: Configuration): Seq[SnarkTestResult] = {
    SATTransitionTest.findTransition(component, configuration)
  }
}

object SATTransitionTest extends SnarkTest {
  def findTransition(component: Component, configuration: Configuration): Seq[SnarkTestResult] = {
    try {
      val graph = component.graph
      val vertices = graph.getSize
      val colors = configuration.colours

      val normalVerices = graph.vertices.filter(x => graph.getNeighbour(x._1).size == 3).keys.toSeq.sorted
      val connectorVerices = component.connectors.flatMap(t => Seq(t._1, t._2))
      val residual = (graph.vertices.keys.toSet -- normalVerices) -- connectorVerices
      val edgeVerices = connectorVerices ++ residual.toSeq

      println(edgeVerices)

      val combinations = {
        for {i <- colors.toSeq
             j <- edgeVerices}
          yield i
      }.combinations(edgeVerices.size)
        .flatMap(_.permutations)
        .toSeq
        .filter(tHTransitionFilter)

      // matrix vertex x vertex x color
      val edgeVars = (0 until vertices).map(i => (0 until vertices).map { j =>
        if (graph.areNeighbour(i, j)) (0 until colors.size).map(c => propVar(s"edge $i $j -> $c"))
        else Nil
      })

      val baseConditions = {
        SATColoringTest.symmetry(edgeVars) ++
          SATColoringTest.onePerEdge(edgeVars) ++
          SATColoringTest.uniquePerEdge(edgeVars) ++
          SATColoringTest.blocksConditions(edgeVars, configuration)
      }.filter(f => f != or())

      val result = new Array[Boolean](combinations.size)

      val jobs = combinations.indices.map { i =>
        task {
          val r = tryToColor(combinations(i), edgeVerices, baseConditions, edgeVars)
          result.update(i, r)
          println(combinations(i) + " res = " + r)
        }
      }.foreach(_.join())

      val goodColorings = combinations.zip(result).filter(_._2).map(_._1)
      //println("good: \n" + goodColorings.mkString("\n"))

      val resTrans = goodColorings.map { col =>
        col.take(component.connectors.size * 2).sliding(2, 2).map(t => Configuration.mapToFactor((t(0), t(1)))).toSeq
      }.toSet


      Seq(TransitionResult(graph, configuration, resTrans, goodColorings, edgeVerices))
    } catch {
      case e =>
        e.printStackTrace()
        Nil
    }
  }

  def tryToColor(colors: Seq[Point], vertices: Seq[Int], conditions: Seq[Formula], vars: Seq[Seq[Seq[PropVar]]]): Boolean = {
    val allConditions = conditions ++ setTransitionEdges(colors, vertices, vars)
    solveForSatisfiability(and(allConditions: _*)) match {
      case None => false
      case Some(model) => true
    }
  }

  def setTransitionEdges(colors: Seq[Point], vertices: Seq[Int], vars: Seq[Seq[Seq[PropVar]]]) = {
    colors.zip(vertices).map { case (c, v) =>
      val neigh = vars(v).zipWithIndex.filter(_._1.nonEmpty).map(_._2).head
      and(vars(neigh)(v)(c.id), vars(v)(neigh)(c.id))
    }
  }


  def tHTransitionFilter(colors: Seq[Point]): Boolean = {
    val isomorphism = (colors(0).id, colors(1).id) match {
      case (0, 9) | (9, 0) | (0, 1) | (1, 0) | (1, 3) | (3, 1) | (3, 6) | (6, 3) |
           (1, 7) | (7, 1) | (0, 0) | (1, 1) => true
      case _ => false
    }

    val zeroSum = colors.map(_.value).reduce(_ ^ _) == 0

    isomorphism && zeroSum
  }
}
