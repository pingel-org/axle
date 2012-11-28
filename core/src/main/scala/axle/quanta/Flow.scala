package axle.quanta

import java.math.BigDecimal
import axle.graph.JungDirectedGraph._

class Flow extends Quantum {

  type Q = FlowQuantity
  type UOM = FlowUnit

  class FlowUnit(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None)
    extends UnitOfMeasurementImpl(name, symbol, link)

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): FlowUnit = new FlowUnit(name, symbol, link)

  class FlowQuantity(magnitude: BigDecimal, unit: FlowUnit) extends QuantityImpl(magnitude, unit)

  def newQuantity(magnitude: BigDecimal, unit: FlowUnit): FlowQuantity = new FlowQuantity(magnitude, unit)

  def conversionGraph() = _conversionGraph

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Volumetric_flow_rate"

  import Volume.{ m3 }
  import Time.{ second }

  lazy val _conversionGraph = JungDirectedGraph[FlowUnit, BigDecimal](
    List(
      derive(m3.over[Time.type, this.type](second, this), Some("cubic meters per second"), Some("m^3/s")),
      unit("Niagara Falls Flow", "Niagara Falls Flow", Some("http://en.wikipedia.org/wiki/Niagara_Falls"))
    ),
    (vs: Seq[JungDirectedGraphVertex[FlowUnit]]) => vs match {
      case m3s :: niagaraFalls :: Nil => List(
        (m3s, niagaraFalls, 1834)
      )
    }
  )

  lazy val m3s = byName("cubic meters per second")
  lazy val niagaraFalls = byName("Niagara Falls Flow")

}

object Flow extends Flow()
