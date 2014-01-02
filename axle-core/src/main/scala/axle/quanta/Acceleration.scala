package axle.quanta

import spire.algebra._
import spire.math._
import axle.graph._

class Acceleration extends Quantum {

  class AccelerationQuantity(
    magnitude: Number = one,
    _unit: Option[Q] = None,
    _name: Option[String] = None,
    _symbol: Option[String] = None,
    _link: Option[String] = None) extends Quantity(magnitude, _unit, _name, _symbol, _link)

  type Q = AccelerationQuantity

  implicit def eqTypeclass: Eq[Q] = new Eq[Q] {
    def eqv(x: Q, y: Q): Boolean = x equals y // TODO
  }

  def newUnitOfMeasurement(
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): AccelerationQuantity =
    new AccelerationQuantity(one, None, name, symbol, link)

  def newQuantity(magnitude: Number, unit: AccelerationQuantity): AccelerationQuantity =
    new AccelerationQuantity(magnitude, Some(unit), None, None, None)

  import Speed.{ mps, fps }
  import Time.{ second }

  val wikipediaUrl = "http://en.wikipedia.org/wiki/Acceleration"

  def conversionGraph: DirectedGraph[Q, Number => Number] = _conversionGraph

  lazy val _conversionGraph = conversions(
    List(
      derive(mps.over[Time.type, this.type](second, this)),
      derive(fps.over[Time.type, this.type](second, this)),
      unit("g", "g", Some("http://en.wikipedia.org/wiki/Standard_gravity"))
    ),
    (vs: Seq[Vertex[AccelerationQuantity]]) => vs match {
      case mpsps :: fpsps :: g :: Nil => trips2fns(List(
        (mpsps, g, 9.80665)
      ))
      case _ => Nil
    }
  )

  lazy val mpsps = byName("mpsps")
  lazy val fpsps = byName("fpsps")
  lazy val g = byName("g")

}

object Acceleration extends Acceleration()
