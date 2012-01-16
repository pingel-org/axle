package org.pingel.gestalt.core

import java.awt.Point
import org.pingel.axle.util.Printable

case class SimpleTransform(override val guardName: Name, outName: Name, map: Map[Name, Name], cost: Double)
  extends Transform(guardName) {

  center = new Point(0, 0)

  addVertex(new TransformVertex(new Name("in"), true, false))

  GLogger.global.entering("SimpleSystm", "<init>: in = " + guardName.toString() +
    ", out = " + outName.toString())

  // TODO sort of a trivial case of super.exits; maybe I should split them:
  val exitNode = addVertex(new TransformVertex(new Name("out"), false, true))

  addEdge(new TransformEdge(new Name(), null, start, exitNode))

  // removed for Scala conversion:
  //    public SimpleTransform()
  //    {
  //        super(new Name()); // TODO this should be the name of the precondition form
  //    }

  def constructCall(id: Int, history: History, lexicon: Lexicon, macro: TransformEdge) = {
    GLogger.global.entering("SimpleSystm", "constructCall")
    new SimpleTransformCall(id, history, lexicon, this, macro)
  }

  override def toString() = {
    // this may not be right... do we want the names of the guard
    // and the output, 
    // or do we want the structural description?? !!!
    guardName + " " + outName + " {" +
      map.keySet.map(from => {
        val to = map.get(from)
        from + "/" + to
      }).mkString(" ") + "}"
  }

  def printToStream(name: Name, p: Printable): Unit = {
    p.print("transform " + name.base + " " + this.toString())
    p.println()
  }

}
