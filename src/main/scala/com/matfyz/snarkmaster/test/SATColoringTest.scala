package com.matfyz.snarkmaster.test

import com.matfyz.snarkmaster.configuration.Configuration
import com.matfyz.snarkmaster.graph.Graph
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

    // matrix vertex x block
    val vertexVars = (0 until vertices).map{i =>
      (0 until configuration.blocks.size).map(b => propVar(s"vertex $i -> block $b"))
    }

    val allConditions = onePerEdge(edgeVars) ++
      uniquePerEdge(edgeVars) /*++
      onePerVertex(vertexVars) ++
      uniquePerVertex(vertexVars) ++ createBlocks
*/
      .filter(f => f != or())

    println("start")
    val result = solveForSatisfiability(and(allConditions:_*)).get.filter(_._2)
    println("end")
    println(result.mkString("\n"))
    WithoutColoring
  }

  def onePerEdge(vars: Seq[Seq[Seq[PropVar]]]) = vars.flatMap(row => row.map(cs => or(cs:_*)))

  def uniquePerEdge(vars: Seq[Seq[Seq[PropVar]]]) = vars.flatMap(row => row.flatMap{p =>
    for{ i <- p
         j <- p
         if i != j
    } yield or(!i, !j)
  })

  def onePerVertex(vars: Seq[Seq[PropVar]]) = vars.map(cs => or(cs:_*))

  def uniquePerVertex(vars: Seq[Seq[PropVar]]) = vars.flatMap{p =>
    for{ i <- p
         j <- p
         if i != j
    } yield or(!i, !j)
  }

  def connectBlockWithColors(edgeVars: Seq[Seq[Seq[PropVar]]],
                             vertexVars: Seq[Seq[PropVar]],
                             configuration: Configuration) = {
    edgeVars.zipWithIndex.flatMap{case (row, vertex) =>
      val indexes = row.zipWithIndex.filter(_._1.nonEmpty).map(_._2)

      configuration.blocks.zipWithIndex.flatMap{case (block, blockIndex) =>
        val col = block.points.toSeq
        for{
          i <- indexes
          j <- indexes
          if i != j
          k <- indexes
          if k != i && k != j
        } yield {
          val free = row(i)(col(0)) && row(j)(col(1)) && row(k)(col(2))
          and(
            or(!free, vertexVars(vertex)(blockIndex)),
            or(free, !vertexVars(vertex)(blockIndex))
          )
        }
      }
    }
  }
}
