package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field
import spire.math.Rational

object FlowDouble extends Flow[Double]()

object FlowRational extends Flow[Rational]()

case class Flow[N]() extends Quantum4[N] {

  type Q = Flow[N]

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Volumetric_flow_rate"

  def unit(name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement4[Flow[N], N](name, symbol, wiki)

  lazy val m3s = unit("m3s", "m3s") // derive
  lazy val niagaraFalls = unit("Niagara Falls Flow", "Niagara Falls Flow", Some("http://en.wikipedia.org/wiki/Niagara_Falls"))

  def units: List[UnitOfMeasurement4[Flow[N], N]] =
    List(m3s, niagaraFalls)

  def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement4[Flow[N], N], UnitOfMeasurement4[Flow[N], N], Bijection[N, N])] =
    List[(UnitOfMeasurement4[Flow[N], N], UnitOfMeasurement4[Flow[N], N], Bijection[N, N])](
      (m3s, niagaraFalls, ScaleInt(1834)))

}