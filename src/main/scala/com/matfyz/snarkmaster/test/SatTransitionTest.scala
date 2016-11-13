package com.matfyz.snarkmaster.test
import cafesat.api.FormulaBuilder._
import cafesat.api.Formulas.{Formula, PropVar}
import cafesat.api.Solver._
import cafesat.sat.Solver
import com.matfyz.snarkmaster.configuration.Configuration
import com.matfyz.snarkmaster.graph.{Component, Graph}
import com.matfyz.snarkmaster.common._
import com.matfyz.snarkmaster.configuration.Configuration.THFactors

case object StartTransitionTest extends StartComponentTestMessage{
  override def start(component: Component, configuration: Configuration): Seq[SnarkTestResult] = {
    SATTransitionTest.findTransition(component, configuration)
  }
}

object SATTransitionTest extends SnarkTest {
  def findTransition(component: Component, configuration: Configuration): Seq[SnarkTestResult] = {
    val graph = component.graph
    val vertices = graph.getSize
    val colors = configuration.colours

    val normalVerices = graph.vertices.filter(x => graph.getNeighbour(x._1).size == 3).keys.toSeq.sorted
    val edgeVerices = component.connectors.flatMap(t => Seq(t._1, t._2))

    println(edgeVerices)

    val combinations =
    {for{ i <- 0 until colors
      j <- edgeVerices.indices}
        yield i
    }.combinations(edgeVerices.size)
      .flatMap(_.permutations)
      .toSeq
      .filter(tHTransitionFilter)

    // matrix vertex x vertex x color
    val edgeVars = (0 until vertices).map(i => (0 until vertices).map{j =>
      if(graph.areNeighbour(i, j)) (0 until colors).map(c => propVar(s"edge $i $j -> $c"))
      else Nil
    })

    val baseConditions = {SATColoringTest.symmetry(edgeVars) ++
      SATColoringTest.onePerEdge(edgeVars) ++
      SATColoringTest.uniquePerEdge(edgeVars) ++
      SATColoringTest.blocksConditions(edgeVars, configuration)
    }.filter(f => f != or())

    val result = new Array[Boolean](combinations.size)

    val jobs = combinations.indices.map{ i =>
      task{
        val r = tryToColor(combinations(i), edgeVerices, baseConditions, edgeVars)
        result.update(i, r)
        println(combinations(i))
      }
    }.foreach(_.join())

    val goodColorings = combinations.zip(result).filter(_._2).map(_._1)
    println("good: \n" + goodColorings.mkString("\n"))

    val resTrans = goodColorings.map{ col =>
      col.sliding(2, 2).map(t => Configuration.mapToFactor((t(0), t(1)))).toSeq
    }.toSet


    Seq(TransitionResult(graph, configuration, resTrans, goodColorings, edgeVerices))
  }

  def tryToColor(colors: Seq[Int], vertices: Seq[Int], conditions: Seq[Formula], vars: Seq[Seq[Seq[PropVar]]]): Boolean = {
    val allConditions = conditions ++ setTransitionEdges(colors, vertices, vars)
    solveForSatisfiability(and(allConditions:_*)) match {
      case None => false
      case Some(model) => true
    }
  }

  def setTransitionEdges(colors: Seq[Int], vertices: Seq[Int], vars: Seq[Seq[Seq[PropVar]]]) = {
    colors.zip(vertices).map{case (c, v) =>
      val neigh = vars(v).zipWithIndex.filter(_._1.nonEmpty).map(_._2).head
      and(vars(neigh)(v)(c), vars(v)(neigh)(c))
    }
  }


  def tHTransitionFilter(colors: Seq[Int]): Boolean = {
    (colors(0), colors(1)) match {
      case (0, 9) | (9, 0) | (0, 1) | (1, 0) | (1, 3) | (3, 1) | (3, 6) | (6, 3) |
           (1, 7) | (7,1) | (0, 0) | (1, 1) => true
    //  case (0, 9) | (0, 1) | (1, 3) | (3, 6) | (1, 7)  => true
      case _ => false
    }
  }
}
