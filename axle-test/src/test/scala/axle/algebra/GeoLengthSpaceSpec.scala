package axle.algebra

import org.scalatest._

import edu.uci.ics.jung.graph.DirectedSparseGraph
import spire.implicits.DoubleAlgebra
import spire.implicits.metricSpaceOps
import axle.algebra.GeoCoordinates.geoCoordinatesLengthSpace
import axle.algebra.modules.doubleDoubleModule
import axle.algebra.modules.doubleRationalModule
import axle.math.distanceOnSphere
import axle.jung.directedGraphJung
import axle.quanta.Angle
import axle.quanta.Distance

class GeoLengthSpaceSpec extends FunSuite with Matchers {

  implicit val angleConverter = Angle.converterGraphK2[Double, DirectedSparseGraph]
  import angleConverter.°
  import angleConverter.radian

  implicit val distanceConverter = Distance.converterGraphK2[Double, DirectedSparseGraph]

  val sf = GeoCoordinates(37.7833 *: °, 122.4167 *: °)
  val ny = GeoCoordinates(40.7127 *: °, 74.0059 *: °)
  val sfo = GeoCoordinates(37.6189 *: °, 122.3750 *: °)
  val hel = GeoCoordinates(60.3172 *: °, -24.9633 *: °)
  val lax = GeoCoordinates(0.592539 *: radian, 2.066470 *: radian)
  val jfk = GeoCoordinates(0.709186 *: radian, 1.287762 *: radian)

  test("geo metric space: distance from San Francisco to New York") {

    val degreesDistance = ((sf distance ny) in °).magnitude

    import distanceConverter.km
    val earthRadius = 6371d *: km
    val kmDistance = (distanceOnSphere(sf distance ny, earthRadius) in km).magnitude

    degreesDistance should be(37.12896941431725)
    kmDistance should be(4128.553030413071)
  }

  test("geo metric space: angular distance from LAX to JFK") {

    ((lax distance jfk) in radian).magnitude should be(0.6235849243922914)
  }

  // See http://williams.best.vwh.net/avform.htm

  test("geo length space: way-point 40% from LAX to JFK correctly") {

    import axle.algebra.GeoCoordinates.geoCoordinatesLengthSpace

    val waypoint = geoCoordinatesLengthSpace.onPath(lax, jfk, 0.4)

    (waypoint.latitude in °).magnitude should be(38.66945192546367)
    (waypoint.longitude in °).magnitude should be(101.6261713931811)
  }

}
