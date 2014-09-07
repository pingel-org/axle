package axle.quanta

import axle.graph.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field
import spire.math.Rational
import spire.implicits.eqOps
import spire.implicits.moduleOps
import spire.implicits.groupOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.additiveGroupOps
import spire.implicits.additiveSemigroupOps

abstract class Frequency extends Quantum {
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Frequency"
}

object Frequency extends Frequency {

  type Q = Frequency

  def units[N: Field: Eq] = List[UnitOfMeasurement[Q, N]](
    unit("Hertz", "Hz", Some("http://en.wikipedia.org/wiki/Hertz")),
    unit("Kilohertz", "KHz"),
    unit("Megahertz", "MHz"),
    unit("Gigahertz", "GHz"))

  def links[N: Field: Eq] = {
    implicit val baseCG = cgnDisconnected[N]
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], N => N, N => N)](
      (Hz, KHz, _ * 1E3, _ / 1E3),
      (Hz, MHz, _ * 1E9, _ / 1E9),
      (Hz, GHz, _ * 1E12, _ / 1E12))
  }

  def hertz[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "hertz")
  def Hz[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "Hz")
  def kilohertz[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "kilohertz")
  def KHz[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "KHz")
  def megahertz[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "megahertz")
  def MHz[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "MHz")
  def gigahertz[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "gigahertz")
  def GHz[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "GHz")

}
