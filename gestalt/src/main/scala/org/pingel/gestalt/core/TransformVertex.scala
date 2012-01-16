package org.pingel.gestalt.core

import org.pingel.axle.graph.DirectedGraphVertex

case class TransformVertex(name: Name, var isStart: Boolean, var isExit: Boolean)
  extends DirectedGraphVertex[TransformEdge] {
  def setIsStart(is: Boolean): Unit = {
    isStart = is
  }

  def setIsExit(is: Boolean): Unit = {
    isExit = is
  }

  override def toString() = name.toString()

}
