package com.matfyz.snarkmaster.model.graph

import java.io.File

sealed trait Message

sealed trait FrameMessage extends Message
case class GraphFileSelected(file: File) extends FrameMessage

