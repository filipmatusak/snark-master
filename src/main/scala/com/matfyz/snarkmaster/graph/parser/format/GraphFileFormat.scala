package com.matfyz.snarkmaster.graph.parser.format

import java.io.File

import com.matfyz.snarkmaster.SnarkMasterException
import com.matfyz.snarkmaster.graph.Graph

import scala.io.Source

trait GraphFileFormat {
  def parse(file: File): Seq[Graph] = {
    val in = Source.fromFile(file)

    val lines = in.getLines()

    parse(file, lines)
  }

  def parse(file: File, lines: Iterator[String]): Seq[Graph]
}
