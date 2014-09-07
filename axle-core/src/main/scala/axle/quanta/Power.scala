package axle.quanta

import axle.graph.DirectedGraph
import spire.algebra._
import spire.math.Rational
import spire.implicits.eqOps
import spire.implicits.moduleOps
import spire.implicits.groupOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.additiveGroupOps
import spire.implicits.additiveSemigroupOps

abstract class Power extends Quantum {
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Power_(physics)"
}

object Power extends Power {

  type Q = Power

  def units[N: Field: Eq] = List[UnitOfMeasurement[Q, N]](
    unit("watt", "W"),
    unit("kilowatt", "KW"),
    unit("megawatt", "MW"),
    unit("gigawatt", "GW"),
    unit("milliwatt", "mW"),
    unit("horsepower", "hp"),
    unit("light bulb", "light bulb"),
    unit("Hoover Dam", "Hoover Dam", Some("http://en.wikipedia.org/wiki/Hoover_Dam")),
    unit("2012 Mustang GT", "2012 Mustang GT", Some("http://en.wikipedia.org/wiki/Ford_Mustang")))

  def links[N: Field: Eq] = {
    implicit val baseCG = cgnDisconnected[N]
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], N => N, N => N)](
      (watt, kilowatt, _ * 1E3, _ / 1E3),
      (kilowatt, megawatt, _ * 1E3, _ / 1E3),
      (megawatt, gigawatt, _ * 1E3, _ / 1E3),
      (milliwatt, watt, _ * 1E3, _ / 1E3),
      (watt, lightBulb, _ * 60, _ / 60),
      (megawatt, hooverDam, _ * 2080, _ / 2080),
      (horsepower, mustangGT, _ * 420, _ / 420))
  }

  def milliwatt[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "milliwatt")
  def watt[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "watt")
  def W[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "watt")
  def kilowatt[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "kilowatt")
  def kW[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "kilowatt")
  def megawatt[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "megawatt")
  def MW[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "megawatt")
  def gigawatt[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "gigawatt")
  def GW[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "gigawatt")
  def lightBulb[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "lightBulb")
  def hooverDam[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "hooverDam")
  def horsepower[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "horsepower")
  def mustangGT[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "mustangGT")

}
