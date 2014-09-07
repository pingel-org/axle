package axle.quanta

import axle.graph.DirectedGraph
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
import spire.implicits._

abstract class Area extends Quantum {
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Area"
}

object Area extends Area {

  type Q = Area

  def units[N: Field: Eq] = List[UnitOfMeasurement[Q, N]]( //    derive(meter.by[Distance.type, this.type](meter, this), Some("m2"), Some("m2")),
  //    derive(km.by[Distance.type, this.type](km, this), Some("km2"), Some("km2")),
  //    derive(cm.by[Distance.type, this.type](cm, this), Some("cm2"), Some("cm2"))
  )

  def links[N: Field: Eq] = {
    implicit val baseCG = cgnDisconnected[N]
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], N => N, N => N)](
      (m2, km2, _ * 1E6, _ / 1E6),
      (cm2, m2, _ * 1E6, _ / 1E6))
  }

  def m2[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "m2")
  def km2[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "km2")
  def cm2[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "cm2")

}
