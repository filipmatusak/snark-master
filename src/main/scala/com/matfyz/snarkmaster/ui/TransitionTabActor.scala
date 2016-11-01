package com.matfyz.snarkmaster.ui

import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.JFileChooser

import akka.actor.{Actor, ActorRef}
import akka.event.LoggingReceive
import com.matfyz.snarkmaster.configuration.Configuration
import com.matfyz.snarkmaster.graph.Graph
import com.matfyz.snarkmaster.model.{TestGraphs, _}
import com.matfyz.snarkmaster.parser.format.SimpleGraphFromat
import com.matfyz.snarkmaster.test.{SnarkTestResult, StartTransitionTest}

class TransitionTabActor(uIActor: ActorRef, mainForm: MainForm) extends Actor{
  var component: Option[Graph] = None
  var configuration: Option[Configuration] = Some(Configuration.THConfiguration)

  override def receive: Receive = LoggingReceive{
    case m: ParsedGraphs =>
      component = m.graphs.headOption
      mainForm.inputComponentName.setText(m.file.getName)
      uIActor ! LogText("Parsed component from " + m.file.getName)
    case r: Seq[SnarkTestResult] =>
      mainForm.transitionTestStatus.setText("")
      uIActor ! LogResult(r)
    case _ =>
  }

  mainForm.selectComponentButton.addActionListener(new ActionListener() {
    def actionPerformed(e: ActionEvent): Unit = {
      val fileChooser = new JFileChooser()
      fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY)
      fileChooser.showOpenDialog(mainForm.tabbedPane1)
      if(fileChooser.getSelectedFile != null)
        uIActor ! ParseGraph(fileChooser.getSelectedFile, SimpleGraphFromat)
    }
  })

  mainForm.startTransitionTestButton.addActionListener(new ActionListener() {
    def actionPerformed(e: ActionEvent): Unit = {
      if(component.isEmpty) uIActor ! LogException("Select component")
      else if(configuration.isEmpty) uIActor ! LogException("Select configuration")
      else {
        uIActor ! TestGraphs(component.toSeq, configuration.get, Seq(StartTransitionTest))
        mainForm.transitionTestStatus.setText("processing")
      }
    }
  })

}

object TransitionTabActor {
  val actorName = "transition-tab"
}