package com.matfyz.snarkmaster.test

import com.matfyz.snarkmaster.configuration.Configuration
import com.matfyz.snarkmaster.graph.{Component, Graph}

case object StartTransitionExistTest extends StartComponentTestMessage {
  override def start(component: Component, configuration: Configuration) = {
    SATColoringTest.test(component.graph, configuration) match {
      case WithoutColoring(graph) => Seq(TransitionDoesntExists(graph, configuration))
      case ColoringExists(coloring, graph) =>
        println(coloring.mkString("\n"))
        Seq(TransitionExists(graph, configuration))
    }
  }
}