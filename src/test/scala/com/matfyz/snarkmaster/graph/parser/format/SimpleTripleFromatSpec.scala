package com.matfyz.snarkmaster.graph.parser.format

import org.scalatest.FlatSpec

class SimpleTripleFromatSpec extends FlatSpec{
  behavior of "parse"

  it should "parse simple graph" in {

  }
}

object SimpleTripleFromatSpec{
  val rawData1 =
    """1
      |{comment}
      |1
      |4
      |1 2 3
      |2 3 0
      |0 3 1
      |0 2 1
    """.stripMargin

  val graphs1 = Seq()
}