package com.matfyz.snarkmaster.model

import java.io.File

import com.matfyz.snarkmaster.configuration.Configuration
import com.matfyz.snarkmaster.graph.Graph
import com.matfyz.snarkmaster.parser.format.GraphFileFormat
import com.matfyz.snarkmaster.test.{SnarkTestResult, StartTestMessage}

sealed trait Message

sealed trait ControlPanelMessage extends Message
case class TestGraphs(graphs: Seq[Graph], configuration: Configuration, tests: Seq[StartTestMessage]) extends ControlPanelMessage
case class StartTestGraphs(tests: Seq[StartTestMessage]) extends ControlPanelMessage

case class ParseGraph(file: File, format: GraphFileFormat) extends Message

case class ParsedGraphs(graphs: Seq[Graph], file: File) extends Message

sealed trait LogMessage extends Message
case class LogText(msg: String) extends LogMessage
case class LogException(msg: String, cause: Option[Throwable] = None) extends LogMessage
case class LogResult(results: Seq[SnarkTestResult]) extends LogMessage