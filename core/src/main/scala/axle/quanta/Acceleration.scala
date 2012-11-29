package axle.quanta

import java.math.BigDecimal
import axle.graph.JungDirectedGraph._

class Acceleration extends Quantum {

  class AccelerationQuantity(
    magnitude: BigDecimal = oneBD,
    _unit: Option[Q] = None,
    _name: Option[String] = None,
    _symbol: Option[String] = None,
    _link: Option[String] = None) extends Quantity(magnitude, _unit, _name, _symbol, _link)

  type Q = AccelerationQuantity

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): AccelerationQuantity =
    new AccelerationQuantity(oneBD, None, name, symbol, link)

  def newQuantity(magnitude: BigDecimal, unit: AccelerationQuantity): AccelerationQuantity =
    new AccelerationQuantity(magnitude, Some(unit), None, None, None)

  import Speed.{ mps, fps }
  import Time.{ second }

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Acceleration"

  def conversionGraph() = _conversionGraph

  lazy val _conversionGraph = JungDirectedGraph[AccelerationQuantity, BigDecimal](
    List(
      derive(mps.over[Time.type, this.type](second, this)),
      derive(fps.over[Time.type, this.type](second, this)),
      unit("g", "g", Some("http://en.wikipedia.org/wiki/Standard_gravity"))
    ),
    (vs: Seq[JungDirectedGraphVertex[AccelerationQuantity]]) => vs match {
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
