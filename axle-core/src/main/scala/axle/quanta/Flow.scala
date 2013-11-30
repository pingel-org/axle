package axle.quanta

import spire.algebra._
import spire.math._
import axle.graph._

class Flow extends Quantum {

  class FlowQuantity(
    magnitude: Number = one,
    _unit: Option[Q] = None,
    _name: Option[String] = None,
    _symbol: Option[String] = None,
    _link: Option[String] = None) extends Quantity(magnitude, _unit, _name, _symbol, _link)

  type Q = FlowQuantity

  implicit def eqTypeclass: Eq[Q] = new Eq[Q] {
    def eqv(x: Q, y: Q): Boolean = x equals y // TODO
  }
  
  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): FlowQuantity =
    new FlowQuantity(one, None, name, symbol, link)

  def newQuantity(magnitude: Number, unit: FlowQuantity): FlowQuantity =
    new FlowQuantity(magnitude, Some(unit), None, None, None)

  def conversionGraph() = _conversionGraph

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Volumetric_flow_rate"

  import Volume.{ m3 }
  import Time.{ second }

  lazy val _conversionGraph = conversions(
    List(
      derive(m3.over[Time.type, this.type](second, this), Some("cubic meters per second"), Some("m^3/s")),
      unit("Niagara Falls Flow", "Niagara Falls Flow", Some("http://en.wikipedia.org/wiki/Niagara_Falls"))
    ),
    (vs: Seq[Vertex[FlowQuantity]]) => vs match {
      case m3s :: niagaraFalls :: Nil => trips2fns(List(
        (m3s, niagaraFalls, 1834)
      ))
      case _ => Nil
    }
  )

  lazy val m3s = byName("cubic meters per second")
  lazy val niagaraFalls = byName("Niagara Falls Flow")

}

object Flow extends Flow()
