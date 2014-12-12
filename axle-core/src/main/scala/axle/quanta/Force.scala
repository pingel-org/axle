package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case object Force extends Quantum {

  type Q = Force.type

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Force"
  
  def unit[N](name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[Q, N](name, symbol, wiki)

  def pound[N]: UnitOfMeasurement[Q, N] = unit("pound", "lb", Some("http://en.wikipedia.org/wiki/Pound-force"))
  def newton[N]: UnitOfMeasurement[Q, N] = unit("newton", "N", Some("http://en.wikipedia.org/wiki/Newton_(unit)"))
  def dyne[N]: UnitOfMeasurement[Q, N] = unit("dyne", "dyn", Some("http://en.wikipedia.org/wiki/Dyne"))

  def units[N]: List[UnitOfMeasurement[Q, N]] =
    List(pound, newton, dyne)

  def links[N: Field]: Seq[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])] =
    List.empty

//  implicit def conversionGraph[N: Field: Eq, DG[_, _]: DirectedGraph] =
//    cgn(units[N], links)

}
