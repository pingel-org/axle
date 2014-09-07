package axle.quanta

import axle.graph.DirectedGraph
import axle.algebra.Bijection
import spire.math.Rational
import spire.algebra.Field
import spire.algebra.Eq
import spire.implicits.eqOps
import spire.implicits.moduleOps
import spire.implicits.groupOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.additiveGroupOps
import spire.implicits.additiveSemigroupOps

abstract class Acceleration extends Quantum {
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Acceleration"
}

object Acceleration extends Acceleration {

  type Q = Acceleration

  def units[N: Field: Eq] = List[UnitOfMeasurement[Q, N]](
    unit("mps", "mps"), // derive
    unit("fps", "fps"), // derive
    unit("g", "g", Some("http://en.wikipedia.org/wiki/Standard_gravity")))

  def links[N: Field: Eq] = {
    implicit val baseCG = cgnDisconnected[N]
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])](
      (mpsps, g, ScaleDouble(9.80665)))
  }

  def mpsps[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "mpsps")
  def fpsps[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "fpsps")
  def g[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "g")

}
