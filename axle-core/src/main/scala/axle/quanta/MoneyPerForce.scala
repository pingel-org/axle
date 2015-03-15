package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class MoneyPerForce() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Degree_(MoneyPerForce)"

}

abstract class MoneyPerForceMetadata[N: Field: Eq, DG[_, _]: DirectedGraph]
  extends QuantumMetadata[MoneyPerForce, N, DG] {

  type U = UnitOfMeasurement[MoneyPerForce, N]

  def USDperPound: U
}


object MoneyPerForce {

  def metadata[N: Field: Eq, DG[_, _]: DirectedGraph] = new MoneyPerForceMetadata[N, DG] {

    def unit(name: String, symbol: String, wiki: Option[String] = None) =
      UnitOfMeasurement[MoneyPerForce, N](name, symbol, wiki)

    lazy val _USDperPound = unit("$/lb", "$/lb") // derive

    def USDperPound = _USDperPound
    
    def units: List[UnitOfMeasurement[MoneyPerForce, N]] =
      List(USDperPound)

    def links: Seq[(UnitOfMeasurement[MoneyPerForce, N], UnitOfMeasurement[MoneyPerForce, N], Bijection[N, N])] =
      List.empty

  }

}