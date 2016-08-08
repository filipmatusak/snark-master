package com.matfyz.snarkmaster.model

import java.io.File

sealed trait Message

sealed trait FrameMessage extends Message
case class GraphFileSelected(file: File) extends FrameMessage

case class ParseGraph(file: File) extends Message