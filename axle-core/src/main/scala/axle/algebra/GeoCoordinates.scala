package axle.algebra

/**
 *
 * http://www.movable-type.co.uk/scripts/latlong.html
 *
 */

import java.lang.Math.atan2
import java.lang.Math.sqrt

import axle.cosine
import axle.quanta.Angle
import axle.quanta.AngleConverter
import axle.quanta.Distance
import axle.quanta.DistanceConverter
import axle.quanta.UnittedQuantity
import axle.quanta.modulize
import axle.sine
import axle.square
import spire.algebra.Eq
import spire.algebra.Field
import spire.algebra.MetricSpace
import spire.implicits.DoubleAlgebra
import spire.implicits.additiveGroupOps
import spire.implicits.additiveSemigroupOps
import spire.implicits.moduleOps
import spire.implicits.multiplicativeGroupOps
import spire.math.ConvertableFrom
import spire.math.ConvertableTo
import spire.math.Rational

case class GeoCoordinates[N](
    latitude: UnittedQuantity[Angle, N],
    longitude: UnittedQuantity[Angle, N]) {

  def φ: UnittedQuantity[Angle, N] = latitude

  def λ: UnittedQuantity[Angle, N] = longitude
}

object GeoCoordinates {

  implicit def geoCoordinatesMetricSpace[N: Field: Eq: ConvertableFrom: ConvertableTo](
    implicit angleConverter: AngleConverter[N]): MetricSpace[GeoCoordinates[N], UnittedQuantity[Angle, N]] =
    new MetricSpace[GeoCoordinates[N], UnittedQuantity[Angle, N]] {

      import angleConverter.radian
      val ctn = ConvertableTo[N]
      val cfn = ConvertableFrom[N]
      val half = ctn.fromRational(Rational(1, 2))

      // Haversine distance

      def distance(v: GeoCoordinates[N], w: GeoCoordinates[N]): UnittedQuantity[Angle, N] = {
        val dLat = w.latitude - v.latitude
        val dLon = w.longitude - v.longitude
        val a = square(sine(dLat :* half)) + square(sine(dLon :* half)) * cosine(v.latitude) * cosine(w.latitude)
        val c = ctn.fromDouble(atan2(sqrt(a), sqrt(1 - a)))
        c *: radian
      }

    }

  implicit def geoCoordinatesLengthSpace[N: Field: Eq: ConvertableFrom: ConvertableTo](
    implicit angleConverter: AngleConverter[N]): LengthSpace[GeoCoordinates[N], UnittedQuantity[Angle, N]] =
    new LengthSpace[GeoCoordinates[N], UnittedQuantity[Angle, N]] {

      val metricSpace: MetricSpace[GeoCoordinates[N], UnittedQuantity[Angle, N]] =
        geoCoordinatesMetricSpace[N]

      import angleConverter.radian
      val ctn = ConvertableTo[N]
      val cfn = ConvertableFrom[N]
      val half = ctn.fromRational(Rational(1, 2))

      def distance(v: GeoCoordinates[N], w: GeoCoordinates[N]): UnittedQuantity[Angle, N] =
        metricSpace.distance(v, w)

      /**
       * Source: http://williams.best.vwh.net/avform.htm
       *   "Intermediate points on a great circle"
       *
       * Note: The two points cannot be antipodal
       *  (i.e. lat1+lat2=0 and abs(lon1-lon2)=pi) because then the route is undefined
       *
       */

      def onPath(p1: GeoCoordinates[N], p2: GeoCoordinates[N], f: Double): GeoCoordinates[N] = {

        val d = distance(p1, p2)

        val A = sine(d :* ctn.fromDouble(1d - f)) / sine(d)
        val B = sine(d :* ctn.fromDouble(f)) / sine(d)
        val x = A * cosine(p1.latitude) * cosine(p1.longitude) + B * cosine(p2.latitude) * cosine(p2.longitude)
        val y = A * cosine(p1.latitude) * sine(p1.longitude) + B * cosine(p2.latitude) * sine(p2.longitude)
        val z = A * sine(p1.latitude) + B * sine(p2.latitude)

        val latitude = ctn.fromDouble(atan2(z, sqrt(square(x) + square(y)))) *: radian
        val longitude = ctn.fromDouble(atan2(y, x)) *: radian

        GeoCoordinates(latitude, longitude)
      }

      // assumes that v is on the path from p1 to p2
      def portion(p1: GeoCoordinates[N], v: GeoCoordinates[N], p2: GeoCoordinates[N]): Double = {
        val num = (distance(p1, v) in radian).magnitude
        val denom = (distance(p1, p2) in radian).magnitude
        cfn.toDouble(num / denom)
      }
    }
}
