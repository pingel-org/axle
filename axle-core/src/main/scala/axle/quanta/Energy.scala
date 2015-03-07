package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Energy[N]() extends Quantum4[N] {

  type Q = Energy[N]

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Energy"

  def unit(name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement4[Q, N](name, symbol, wiki)

  def kwh: UnitOfMeasurement4[Q, N] = unit("kwh", "kwh") // derive
  def joule: UnitOfMeasurement4[Q, N] = unit("joule", "J")
  def kilojoule: UnitOfMeasurement4[Q, N] = unit("kilojoule", "KJ")
  def megajoule: UnitOfMeasurement4[Q, N] = unit("megajoule", "MJ")
  def tonTNT: UnitOfMeasurement4[Q, N] = unit("ton TNT", "T", Some("http://en.wikipedia.org/wiki/TNT_equivalent"))
  def t: UnitOfMeasurement4[Q, N] = tonTNT
  def kiloton: UnitOfMeasurement4[Q, N] = unit("kiloton", "KT")
  def kt = kiloton
  def megaton: UnitOfMeasurement4[Q, N] = unit("megaton", "MT")
  def mt = megaton
  def gigaton: UnitOfMeasurement4[Q, N] = unit("gigaton", "GT")
  def gt = gigaton

  // TODO lazy val castleBravo = 15 *: megaton // Some("Castle Bravo Thermonuclear Bomb"), None, Some("http://en.wikipedia.org/wiki/Castle_Bravo"))

  def units: List[UnitOfMeasurement4[Q, N]] =
    List(kwh, joule, kilojoule, megajoule, tonTNT, kiloton, megaton, gigaton)

  def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement4[Q, N], UnitOfMeasurement4[Q, N], Bijection[N, N])] =
    List[(UnitOfMeasurement4[Q, N], UnitOfMeasurement4[Q, N], Bijection[N, N])](
      (megajoule, t, ScaleDouble(4.184)),
      (joule, kilojoule, Scale10s(3)),
      (joule, megajoule, Scale10s(6)),
      (t, kt, Scale10s(3)),
      (t, mt, Scale10s(6)),
      (t, gt, Scale10s(9)))

}
