package axle.quanta

import java.math.BigDecimal
import axle.graph.JungDirectedGraph._

class Speed extends Quantum {

  type Q = SpeedQuantity
  type UOM = SpeedUnit

  class SpeedUnit(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None)
    extends UnitOfMeasurementImpl(name, symbol, link)

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): SpeedUnit = new SpeedUnit(name, symbol, link)

  class SpeedQuantity(magnitude: BigDecimal, unit: SpeedUnit) extends QuantityImpl(magnitude, unit)

  def newQuantity(magnitude: BigDecimal, unit: SpeedUnit): SpeedQuantity = new SpeedQuantity(magnitude, unit)

  import Distance.{ meter, mile, ft }
  import Time.{ second, hour }

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Speed"

  def conversionGraph() = _conversionGraph

  lazy val _conversionGraph = JungDirectedGraph[SpeedUnit, BigDecimal](
    List(
      derive(meter.over[Time.type, this.type](second, this)),
      derive(ft.over[Time.type, this.type](second, this)),
      derive(mile.over[Time.type, this.type](hour, this)),
      unit("Light Speed", "c", Some("http://en.wikipedia.org/wiki/Speed_of_light")),
      unit("Speed limit", "speed limit")
    ),
    (vs: Seq[JungDirectedGraphVertex[SpeedUnit]]) => vs match {
      case mps :: fps :: mph :: c :: speedLimit :: Nil => List(
        (c, mps, "299792458"),
        (mph, speedLimit, "65")
      )
    }
  )

  lazy val mps = byName("mps")
  lazy val fps = byName("fps")
  lazy val mph = byName("mph")
  lazy val c = byName("c")
  lazy val speedLimit = byName("Speed limit")

}

object Speed extends Speed()
