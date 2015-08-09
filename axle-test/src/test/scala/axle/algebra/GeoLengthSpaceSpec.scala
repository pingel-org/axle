package axle.algebra

import org.specs2.mutable.Specification

import axle.algebra.GeoCoordinates.geoCoordinatesLengthSpace
import axle.algebra.modules.doubleDoubleModule
import axle.algebra.modules.doubleRationalModule
import axle.jung.directedGraphJung
import axle.quanta.Angle
import axle.quanta.Distance
import axle.quanta.UnitOfMeasurement
import edu.uci.ics.jung.graph.DirectedSparseGraph
import spire.implicits.DoubleAlgebra
import spire.implicits.metricSpaceOps

class GeoLengthSpaceSpec extends Specification {

  "geo length space" should {
    "measure distance from San Francisco to New York" in {

      implicit val angleConverter = Angle.converterGraph[Double, DirectedSparseGraph[UnitOfMeasurement[Angle], Double => Double]]
      import angleConverter.°

      implicit val distanceConverter = Distance.converterGraph[Double, DirectedSparseGraph[UnitOfMeasurement[Distance], Double => Double]]
      import distanceConverter.km

      val sf = GeoCoordinates(37.7833 *: °, 122.4167 *: °)
      val ny = GeoCoordinates(40.7127 *: °, 74.0059 *: °)

      // val geoLengthSpace = geoCoordinatesLengthSpace[Double]

      ((sf distance ny) in km).magnitude must be equalTo 4128.553030413071
    }
  }

}