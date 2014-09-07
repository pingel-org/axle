package axle.quanta

import axle.graph.DirectedGraph
import axle.graph.Vertex
import spire.algebra.Eq
import spire.algebra.Field
import spire.implicits.DoubleAlgebra
import spire.implicits._
import spire.math.Rational
import spire.math.Real

abstract class Volume extends Quantum {
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Volume"
}

object Volume extends Volume {

  type Q = Volume

  def units[N: Field: Eq] = List[UnitOfMeasurement[Q, N]](
    //      derive(m2.by[Distance.type, this.type](meter, this), Some("m3"), Some("m3")),
    //      derive(km2.by[Distance.type, this.type](km, this), Some("km3"), Some("km3")),
    //      derive(cm2.by[Distance.type, this.type](cm, this), Some("cm3"), Some("cm3")),
    unit("Great Lakes Volume", "Great Lakes Volume", Some("http://en.wikipedia.org/wiki/Great_Lakes")),
    unit("liter", "L", Some("http://en.wikipedia.org/wiki/Liter")), // TODO: also symbol ℓ
    unit("milliliter", "mL"),
    unit("wine bottle", "wineBottle", Some("http://en.wikipedia.org/wiki/Wine_bottle")),
    unit("magnum", "magnum"),
    unit("jeroboam", "jeroboam"),
    unit("rehoboam", "rehoboam"),
    unit("methuselah", "methuselah"),
    unit("salmanazar", "salmanazar"),
    unit("balthazar", "balthazar"),
    unit("nebuchadnezzar", "nebuchadnezzar"))

  def links[N: Field: Eq] = {
    implicit val baseCG = cgnDisconnected[N]
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], N => N, N => N)](
      (km3, greatLakes, _ * 22671, _ / 22671),
      (milliliter, liter, _ * 1000, _ / 1000),
      (cm3, milliliter, identity, identity),
      (milliliter, wineBottle, _ * 750, _ / 750),
      (wineBottle, magnum, _ * 2, _ / 2),
      (wineBottle, jeroboam, _ * 4, _ / 4),
      (wineBottle, rehoboam, _ * 6, _ / 6),
      (wineBottle, methuselah, _ * 8, _ / 8),
      (wineBottle, salmanazar, _ * 12, _ / 12),
      (wineBottle, balthazar, _ * 16, _ / 16),
      (wineBottle, nebuchadnezzar, _ * 20, _ / 20))
  }

  def m3[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "m3")
  def km3[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "km3")
  def cm3[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "cm3")
  def milliliter[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "cm3")
  def greatLakes[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "Great Lakes Volume")
  def liter[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "liter")
  def L[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "liter")
  def mL[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "milliter")

  def wineBottle[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "wineBottle")
  def magnum[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "magnum")
  def jeroboam[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "jeroboam")
  def rehoboam[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "rehoboam")
  def methuselah[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "methuselah")
  def salmanazar[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "salmanazar")
  def balthazar[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "balthazar")
  def nebuchadnezzar[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "nebuchadnezzar")

}
