package com.matfyz.snarkmaster.graph.parser.format

import java.io.File

import com.matfyz.snarkmaster.SnarkMasterException
import com.matfyz.snarkmaster.graph.Graph

import scala.io.Source

trait GraphFileFormat {
  def parse(file: File): Seq[Graph] = {
    try{
      val in = Source.fromFile(file)

      val lines = in.getLines()

      parse(file, lines)
    } catch {
      //todo log
      case _: Exception => throw new SnarkMasterException("Parsing error")
    }
  }

  def parse(file: File, lines: Iterator[String]): Seq[Graph]
}
