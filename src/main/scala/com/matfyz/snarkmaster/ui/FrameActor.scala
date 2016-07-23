package com.matfyz.snarkmaster.ui

import java.awt.BorderLayout
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing._

import akka.actor.{Actor, ActorRef}
import com.matfyz.snarkmaster.model.graph._

class FrameActor(uiActor: ActorRef) extends JFrame("SnarkMaster\u2122") with Actor{
  val frame = this

  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  setSize(1024, 600)
  setLayout(new BorderLayout)


  val graphInputPanel = new JPanel()
  val graphInputName = new JLabel()
  val graphInputButton = new JButton("Open")

  val fileChooser = new JFileChooser()
  fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY)

  graphInputPanel.add(new JLabel("Graph: "))
  graphInputPanel.add(graphInputName)
  graphInputPanel.add(graphInputButton)

  graphInputButton.addActionListener(new ActionListener {
    override def actionPerformed(e: ActionEvent): Unit = {
      fileChooser.showOpenDialog(frame)
      graphInputName.setText("Loading")

      uiActor ! GraphFileSelected(fileChooser.getSelectedFile)
    }
  })

  add(graphInputPanel)


  setVisible(true)
  override def receive: Receive = ???
}

object FrameActor{
  val name = "frame"
}


/*  val rightpanel = new JPanel
  rightpanel.setBorder(BorderFactory.createEtchedBorder(border.EtchedBorder.LOWERED))
  rightpanel.setLayout(new BorderLayout)
  add(rightpanel, BorderLayout.EAST)

  val controls = new JPanel
  controls.setLayout(new GridLayout(0, 2))
  rightpanel.add(controls, BorderLayout.NORTH)

  val filterLabel = new JLabel("Filter")
  controls.add(filterLabel)

  val filterCombo = new JComboBox(Array(
    "horizontal-box-blur",
    "vertical-box-blur"
  ))
  controls.add(filterCombo)

  val radiusLabel = new JLabel("Radius")
  controls.add(radiusLabel)

  val radiusSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 16, 1))
  controls.add(radiusSpinner)

  val tasksLabel = new JLabel("Tasks")
  controls.add(tasksLabel)

  val tasksSpinner = new JSpinner(new SpinnerNumberModel(32, 1, 128, 1))
  controls.add(tasksSpinner)

  val stepbutton = new JButton("Apply filter")
  controls.add(stepbutton)

  val clearButton = new JButton("Reload")
  controls.add(clearButton)

  val info = new JTextArea("   ")
  info.setBorder(BorderFactory.createLoweredBevelBorder)
  rightpanel.add(info, BorderLayout.SOUTH)

  val mainMenuBar = new JMenuBar()

  val fileMenu = new JMenu("File")
  val openMenuItem = new JMenuItem("Open...")
  fileMenu.add(openMenuItem)
  val exitMenuItem = new JMenuItem("Exit")
  exitMenuItem.addActionListener(new ActionListener {
    def actionPerformed(e: ActionEvent) {
      sys.exit(0)
    }
  })
  fileMenu.add(exitMenuItem)

  mainMenuBar.add(fileMenu)

  val helpMenu = new JMenu("Help")
  val aboutMenuItem = new JMenuItem("About")
  aboutMenuItem.addActionListener(new ActionListener {
    def actionPerformed(e: ActionEvent) {
      JOptionPane.showMessageDialog(null, "ScalaShop, the ultimate image manipulation tool\nBrought to you by EPFL, 2015")
    }
  })
  helpMenu.add(aboutMenuItem)

  mainMenuBar.add(helpMenu)

  setJMenuBar(mainMenuBar)

  setVisible(true)

  def updateInformationBox(time: Double) {
    info.setText(s"Time: $time")
  }

  def getNumTasks: Int = tasksSpinner.getValue.asInstanceOf[Int]

  def getRadius: Int = radiusSpinner.getValue.asInstanceOf[Int]

  def getFilterName: String = {
    filterCombo.getSelectedItem.asInstanceOf[String]
  }*/
