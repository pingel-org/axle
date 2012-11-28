package axle.quanta

import java.math.BigDecimal
import axle.graph.JungDirectedGraph._

class Acceleration extends Quantum {

  type Q = AccelerationQuantity
  type UOM = AccelerationUnit

  class AccelerationUnit(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None)
    extends UnitOfMeasurementImpl(name, symbol, link)

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): AccelerationUnit = new AccelerationUnit(name, symbol, link)

  class AccelerationQuantity(magnitude: BigDecimal, unit: AccelerationUnit) extends QuantityImpl(magnitude, unit)

  def newQuantity(magnitude: BigDecimal, unit: AccelerationUnit): AccelerationQuantity = new AccelerationQuantity(magnitude, unit)

  import Speed.{ mps, fps }
  import Time.{ second }

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Acceleration"

  def conversionGraph() = _conversionGraph

  lazy val _conversionGraph = JungDirectedGraph[AccelerationUnit, BigDecimal](
    List(
      derive(mps.over[Time.type, this.type](second, this)),
      derive(fps.over[Time.type, this.type](second, this)),
      unit("g", "g", Some("http://en.wikipedia.org/wiki/Standard_gravity"))
    ),
    (vs: Seq[JungDirectedGraphVertex[AccelerationUnit]]) => vs match {
      case mpsps :: fpsps :: g :: Nil => List(
        (mpsps, g, "9.80665")
      )
    }
  )

  lazy val mpsps = byName("mpsps")
  lazy val fpsps = byName("fpsps")
  lazy val g = byName("g")

}

object Acceleration extends Acceleration()
