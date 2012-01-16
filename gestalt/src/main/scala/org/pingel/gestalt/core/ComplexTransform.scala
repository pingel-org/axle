package org.pingel.gestalt.core

import org.pingel.axle.util.Printable

case class ComplexTransform(override val guardName: Name)
  extends Transform(guardName) {

  def constructCall(id: Int, history: History, lexicon: Lexicon, macro: TransformEdge) = {
    GLogger.global.entering("ComplexTransform", "constructCall")
    new ComplexTransformCall(id, history, lexicon, this, macro)
  }

  override def toString(): String = {
    guardName + " {\n" +
      getEdges().map("   " + _.toString() + "\n").mkString("") +
      "} <" +
      getVertices().filter(_.isExit).map(_.name).mkString(" ") +
      ">\n"
  }

  def printToStream(name: Name, p: Printable): Unit = {
    p.print("transform " + name.base + " " + this.toString())
    p.println()
  }

}
