package axle.algebra

import scala.annotation.implicitNotFound

import org.specs2.mutable.Specification

import axle.algebra.GeoCoordinates.geoCoordinatesLengthSpace
import axle.algebra.modules.doubleDoubleModule
import axle.algebra.modules.doubleRationalModule
import axle.jung.directedGraphJung
import axle.quanta.Angle
import axle.quanta.Distance
import edu.uci.ics.jung.graph.DirectedSparseGraph
import spire.implicits.DoubleAlgebra

class GeoLengthSpaceSpec extends Specification {

  "geo length space" should {
    "measure distance from San Francisco to New York" in {

      implicit val angleConverter = Angle.converterGraph[Double, DirectedSparseGraph]
      import angleConverter.degree
      import angleConverter.°

      val sf = GeoCoordinates(37.7833 *: °, 122.4167 *: °)
      val ny = GeoCoordinates(40.7127 *: °, 74.0059 *: °)

      import axle.algebra.GeoCoordinates.geoCoordinatesLengthSpace

      implicit val distanceConverter = Distance.converterGraph[Double, DirectedSparseGraph]
      import distanceConverter.km

      val geoLengthSpace = geoCoordinatesLengthSpace[Double]

      (geoLengthSpace.distance(sf, ny) in km).magnitude must be equalTo 4128.553030413071
    }
  }

}