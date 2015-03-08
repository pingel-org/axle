package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Energy[N]() extends Quantum[N] {

  type Q = Energy[N]

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Energy"

  def unit(name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[Energy[N], N](name, symbol, wiki)

  lazy val kwh = unit("kwh", "kwh") // derive
  lazy val joule = unit("joule", "J")
  lazy val kilojoule = unit("kilojoule", "KJ")
  lazy val megajoule = unit("megajoule", "MJ")
  lazy val tonTNT = unit("ton TNT", "T", Some("http://en.wikipedia.org/wiki/TNT_equivalent"))
  lazy val t = tonTNT
  lazy val kiloton = unit("kiloton", "KT")
  lazy val kt = kiloton
  lazy val megaton = unit("megaton", "MT")
  lazy val mt = megaton
  lazy val gigaton = unit("gigaton", "GT")
  lazy val gt = gigaton

  // TODO lazy val castleBravo = 15 *: megaton // Some("Castle Bravo Thermonuclear Bomb"), None, Some("http://en.wikipedia.org/wiki/Castle_Bravo"))

  def units: List[UnitOfMeasurement[Energy[N], N]] =
    List(kwh, joule, kilojoule, megajoule, tonTNT, kiloton, megaton, gigaton)

  def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement[Energy[N], N], UnitOfMeasurement[Energy[N], N], Bijection[N, N])] =
    List[(UnitOfMeasurement[Energy[N], N], UnitOfMeasurement[Energy[N], N], Bijection[N, N])](
      (megajoule, t, ScaleDouble(4.184)),
      (joule, kilojoule, Scale10s(3)),
      (joule, megajoule, Scale10s(6)),
      (t, kt, Scale10s(3)),
      (t, mt, Scale10s(6)),
      (t, gt, Scale10s(9)))

}
