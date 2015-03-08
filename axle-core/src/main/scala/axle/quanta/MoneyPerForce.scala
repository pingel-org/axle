package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class MoneyPerForce() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Degree_(MoneyPerForce)"

}

object MoneyPerForce {

  def metadata[N] = new QuantumMetadata[MoneyPerForce, N] {

    def unit(name: String, symbol: String, wiki: Option[String] = None) =
      UnitOfMeasurement[MoneyPerForce, N](name, symbol, wiki)

    lazy val USDperPound = unit("$/lb", "$/lb") // derive

    def units: List[UnitOfMeasurement[MoneyPerForce, N]] =
      List(USDperPound)

    def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement[MoneyPerForce, N], UnitOfMeasurement[MoneyPerForce, N], Bijection[N, N])] =
      List.empty

  }

}