package com.matfyz.snarkmaster.test

import com.matfyz.snarkmaster.configuration.Configuration
import com.matfyz.snarkmaster.graph.{Edge, Graph}
import cafesat.api.FormulaBuilder._
import cafesat.api.Formulas.PropVar
import cafesat.api.Solver._

object SATColoringTest extends SnarkColoringTest{
  def test(graph: Graph, configuration: Configuration) = {
    val vertices = graph.getSize
    val colors = configuration.colours

    // matrix vertex x vertex x color
    val edgeVars = (0 until vertices).map(i => (0 until vertices).map{j =>
      if(graph.areNeighbour(i, j)) (0 until colors).map(c => propVar(s"edge $i $j -> $c"))
      else Nil
    })

    val allConditions = {
      symmetry(edgeVars) ++
      onePerEdge(edgeVars) ++
        uniquePerEdge(edgeVars) ++
        blocksConditions(edgeVars, configuration)
    }.filter(f => f != or())

    solveForSatisfiability(and(allConditions:_*)) match {
      case None => WithoutColoring(graph)
      case Some(model) =>
        ColoringExists(model
          .filter(_._2)
          .map{ x =>
            val row = x._1.toString().split(" ")
              val u = row(1).toInt
              val v = row(2).toInt
              val c = row(4).split("_").head.toInt
              (Math.min(u, v), Math.max(u, v), c)
          }.toSet.toSeq,
          graph
        )
    }

  }

  def symmetry(vars: Seq[Seq[Seq[PropVar]]]) = {
    for{
      i <- vars.indices
      j <- vars(i).indices
      k <- vars(i)(j).indices
      if i < j
    } yield vars(i)(j)(k) iff vars(j)(i)(k)
  }

  def onePerEdge(vars: Seq[Seq[Seq[PropVar]]]) = vars.flatMap(row => row.map(cs => or(cs:_*)))

  def uniquePerEdge(vars: Seq[Seq[Seq[PropVar]]]) = vars.flatMap(row => row.flatMap{p =>
    for{ i <- p
         j <- p
         if i != j
    } yield or(!i, !j)
  })

  def blocksConditions(edgeVars: Seq[Seq[Seq[PropVar]]],
                       configuration: Configuration) = {
    edgeVars.map { row =>
      val n = row.zipWithIndex.filter(_._1.nonEmpty).map(_._2)
      or({
          for{
            i <- n
            j <- n
            if i != j
            k <- n
            if i != k && j != k
            b <- configuration.blocks.map(_.points.toSeq)
          } yield and(
            row(i)(b(0)),
            row(j)(b(1)),
            row(k)(b(2))
          )
        }:_*)
    }
  }
}
