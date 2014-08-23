package axle.quanta

import spire.algebra._
import spire.math._
import spire.implicits._
import axle.graph._

abstract class Flow[N: Field: Order: Eq] extends Quantum[N] {
  
  class FlowQuantity(
    magnitude: N = field.one,
    _unit: Option[Q] = None,
    _name: Option[String] = None,
    _symbol: Option[String] = None,
    _link: Option[String] = None) extends Quantity(magnitude, _unit, _name, _symbol, _link)

  type Q = FlowQuantity

  implicit def eqTypeclass: Eq[Q] = new Eq[Q] {
    def eqv(x: Q, y: Q): Boolean =
      (x.magnitude === y.magnitude) &&
        ((x.unitOption.isDefined && y.unitOption.isDefined && (x.unitOption.get === y.unitOption.get)) ||
          (x.unitOption.isEmpty && y.unitOption.isEmpty && x.equals(y)))
  }

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): FlowQuantity =
    new FlowQuantity(field.one, None, name, symbol, link)

  def newQuantity(magnitude: N, unit: FlowQuantity): FlowQuantity =
    new FlowQuantity(magnitude, Some(unit), None, None, None)
  
  val wikipediaUrl = "http://en.wikipedia.org/wiki/Volumetric_flow_rate"

}

object Flow extends Flow[Rational] {


  import Volume.{ m3 }
  import Time.{ second }

  lazy val _conversionGraph = conversions(
    List(
      derive(m3.over[Time.type, this.type](second, this), Some("cubic meters per second"), Some("m^3/s")),
      unit("Niagara Falls Flow", "Niagara Falls Flow", Some("http://en.wikipedia.org/wiki/Niagara_Falls"))),
    (vs: Seq[Vertex[FlowQuantity]]) => vs match {
      case m3s :: niagaraFalls :: Nil => trips2fns(List(
        (m3s, niagaraFalls, 1834)))
      case _ => Nil
    })

  lazy val m3s = byName("cubic meters per second")
  lazy val niagaraFalls = byName("Niagara Falls Flow")

  def conversionGraph: DirectedGraph[Q, Rational => Rational] = _conversionGraph
  
}
