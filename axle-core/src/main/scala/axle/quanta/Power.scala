package axle.quanta

import axle.algebra.Vertex
import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field
import spire.math.Rational
import spire.math.Real
import spire.implicits._

//class Power[DG[_, _]: DirectedGraph] extends Quantum {
//
//  def wikipediaUrl = "http://en.wikipedia.org/wiki/Power_(physics)"
//
//  type Q = this.type
//
//  def units[N: Field: Eq] = List[UnitOfMeasurement[Q, N]](
//    unit("watt", "W"),
//    unit("kilowatt", "KW"),
//    unit("megawatt", "MW"),
//    unit("gigawatt", "GW"),
//    unit("milliwatt", "mW"),
//    unit("horsepower", "hp"),
//    unit("light bulb", "light bulb"),
//    unit("Hoover Dam", "Hoover Dam", Some("http://en.wikipedia.org/wiki/Hoover_Dam")),
//    unit("2012 Mustang GT", "2012 Mustang GT", Some("http://en.wikipedia.org/wiki/Ford_Mustang")))
//
//  def links[N: Field: Eq] = {
//    implicit val baseCG = cgnDisconnected[N, DG]
//    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])](
//      (watt, kilowatt, Scale10s(3)),
//      (kilowatt, megawatt, Scale10s(3)),
//      (megawatt, gigawatt, Scale10s(3)),
//      (milliwatt, watt, Scale10s(3)),
//      (watt, lightBulb, ScaleInt(60)),
//      (megawatt, hooverDam, ScaleInt(2080)),
//      (horsepower, mustangGT, ScaleInt(420)))
//  }
//
//  def milliwatt[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "milliwatt")
//  def watt[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "watt")
//  def W[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "watt")
//  def kilowatt[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "kilowatt")
//  def kW[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "kilowatt")
//  def megawatt[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "megawatt")
//  def MW[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "megawatt")
//  def gigawatt[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "gigawatt")
//  def GW[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "gigawatt")
//  def lightBulb[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "lightBulb")
//  def hooverDam[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "hooverDam")
//  def horsepower[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "horsepower")
//  def mustangGT[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "mustangGT")
//
//}
