package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case object Energy extends Quantum {

  type Q = Energy.type

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Energy"

  def unit[N](name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[Q, N](name, symbol, wiki)

  def kwh[N]: UnitOfMeasurement[Q, N] = unit("kwh", "kwh") // derive
  def joule[N]: UnitOfMeasurement[Q, N] = unit("joule", "J")
  def kilojoule[N]: UnitOfMeasurement[Q, N] = unit("kilojoule", "KJ")
  def megajoule[N]: UnitOfMeasurement[Q, N] = unit("megajoule", "MJ")
  def tonTNT[N]: UnitOfMeasurement[Q, N] = unit("ton TNT", "T", Some("http://en.wikipedia.org/wiki/TNT_equivalent"))
  def t[N]: UnitOfMeasurement[Q, N] = tonTNT[N]
  def kiloton[N]: UnitOfMeasurement[Q, N] = unit("kiloton", "KT")
  def kt[N] = kiloton[N]
  def megaton[N]: UnitOfMeasurement[Q, N] = unit("megaton", "MT")
  def mt[N] = megaton[N]
  def gigaton[N]: UnitOfMeasurement[Q, N] = unit("gigaton", "GT")
  def gt[N] = gigaton[N]

  // TODO lazy val castleBravo = 15 *: megaton // Some("Castle Bravo Thermonuclear Bomb"), None, Some("http://en.wikipedia.org/wiki/Castle_Bravo"))

  def units[N]: List[UnitOfMeasurement[Q, N]] =
    List(kwh, joule, kilojoule, megajoule, tonTNT, kiloton, megaton, gigaton)

  def links[N: Field]: Seq[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])] =
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])](
      (megajoule, t, ScaleDouble(4.184)),
      (joule, kilojoule, Scale10s(3)),
      (joule, megajoule, Scale10s(6)),
      (t, kt, Scale10s(3)),
      (t, mt, Scale10s(6)),
      (t, gt, Scale10s(9)))

//  implicit def conversionGraph[N: Field: Eq, DG[_, _]: DirectedGraph] =
//    cgn(units[N], links[N])

}
