package com.matfyz.snarkmaster.ui


import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.JFileChooser

import akka.actor.{Actor, ActorRef}
import akka.event.LoggingReceive
import com.matfyz.snarkmaster.configuration.Configuration
import com.matfyz.snarkmaster.graph.Graph
import com.matfyz.snarkmaster.model._
import com.matfyz.snarkmaster.parser.format.TripleGraphFormat
import com.matfyz.snarkmaster.test.{SnarkTestResult, StartSatColoringTest}

class ColoringTabActor(uIActor: ActorRef, mainForm: MainForm) extends Actor{
  var graphs: Seq[Graph] = Nil
  var configuration: Option[Configuration] = Some(Configuration.THConfiguration)

  override def receive: Receive = LoggingReceive{
    case m: ParsedGraphs =>
      graphs = m.graphs
      mainForm.graphInputName.setText(m.file.getName)
      uIActor ! LogText("Parsed graphs from " + m.file.getName)
    case r: Seq[SnarkTestResult] =>
      mainForm.coloringTestStatus.setText("")
      uIActor ! LogResult(r)
    case _ =>
  }

  mainForm.selectGraphButton.addActionListener(new ActionListener() {
    def actionPerformed(e: ActionEvent): Unit = {
      val fileChooser = new JFileChooser()
      fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY)
      fileChooser.showOpenDialog(mainForm.tabbedPane1)
      if(fileChooser.getSelectedFile != null)
        uIActor ! ParseGraph(fileChooser.getSelectedFile, TripleGraphFormat)
    }
  })

  mainForm.startColoringTestButton.addActionListener(new ActionListener() {
    def actionPerformed(e: ActionEvent): Unit = {
      if(graphs.isEmpty) uIActor ! LogException("Select graph")
      else if(configuration.isEmpty) uIActor ! LogException("Select configuration")
      else {
        uIActor ! TestGraphs(graphs, configuration.get, Seq(StartSatColoringTest))
        mainForm.coloringTestStatus.setText("processing")
      }
    }
  })

}

object ColoringTabActor{
  val actorName = "coloring-tab"
}