package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class MoneyPerForce() extends Quantum("http://en.wikipedia.org/wiki/Degree_(MoneyPerForce)")

object MoneyPerForce {

  type Q = MoneyPerForce

  def unit[N](name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[Q, N](name, symbol, wiki)

  def USDperPound[N]: UnitOfMeasurement[Q, N] = unit("$/lb", "$/lb") // derive

  def units[N]: List[UnitOfMeasurement[Q, N]] =
    List(USDperPound)

  def links[N: Field]: Seq[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])] =
    List.empty

  implicit def conversionGraph[N: Field: Eq, DG[_, _]: DirectedGraph] =
    Quantum.cgn(units[N], links)

}
