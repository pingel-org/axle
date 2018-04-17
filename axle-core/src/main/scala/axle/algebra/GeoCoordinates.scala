package axle.algebra

import axle.math.arcTangent2
import axle.math.cosine
import axle.quanta.Angle
import axle.quanta.AngleConverter
import axle.quanta.UnittedQuantity
import axle.quanta.modulize
import axle.math.sine
import axle.math.square
import axle.math.{ √ => √ }
import spire.algebra.Field
import spire.algebra.MetricSpace
import spire.algebra.MultiplicativeMonoid
import spire.algebra.MultiplicativeSemigroup
import spire.algebra.NRoot
import spire.algebra.Trig
import spire.implicits.additiveGroupOps
import spire.implicits.additiveSemigroupOps
import spire.implicits.literalIntAdditiveGroupOps
import spire.implicits.moduleOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.math.ConvertableTo
import spire.math.Rational
import spire.math.sqrt
import cats.kernel.Eq
import cats.Show
import cats.implicits._

case class GeoCoordinates[N](
  latitude:  UnittedQuantity[Angle, N],
  longitude: UnittedQuantity[Angle, N]) {

  def φ: UnittedQuantity[Angle, N] = latitude

  def λ: UnittedQuantity[Angle, N] = longitude
}

object GeoCoordinates {

  implicit def showGC[N: MultiplicativeMonoid: Eq](
    implicit
    converter: AngleConverter[N]): Show[GeoCoordinates[N]] =
    p => {
      import converter.°
      (p.latitude in °).magnitude + "° N " + (p.longitude in °).magnitude + "° W"
    }

  implicit def eqgcd[N: Eq: MultiplicativeMonoid](
    implicit
    ac: AngleConverter[N]): Eq[GeoCoordinates[N]] =
    (x, y) => {
      val lateq: Boolean = (x.latitude.magnitude === (y.latitude in x.latitude.unit).magnitude)
      val longeq: Boolean = (x.longitude.magnitude === (y.longitude in x.latitude.unit).magnitude)
      lateq && longeq
    }

  implicit def geoCoordinatesMetricSpace[N: Field: Eq: Trig: ConvertableTo: MultiplicativeSemigroup: NRoot](
    implicit
    angleConverter: AngleConverter[N]): MetricSpace[GeoCoordinates[N], UnittedQuantity[Angle, N]] =
    new MetricSpace[GeoCoordinates[N], UnittedQuantity[Angle, N]] {

      val ctn = ConvertableTo[N]
      // val cfn = ConvertableFrom[N]
      val half = ctn.fromRational(Rational(1, 2))

      // Haversine distance

      def distance(v: GeoCoordinates[N], w: GeoCoordinates[N]): UnittedQuantity[Angle, N] = {
        val dLat = w.latitude - v.latitude
        val dLon = w.longitude - v.longitude
        val a = square(sine(dLat :* half)) + square(sine(dLon :* half)) * cosine(v.latitude) * cosine(w.latitude)
        arcTangent2(sqrt(a), sqrt(1 - a)) :* 2
      }
    }

  implicit def geoCoordinatesLengthSpace[N: Field: Eq: Trig: ConvertableTo: MultiplicativeMonoid: NRoot](
    implicit
    angleConverter: AngleConverter[N]): LengthSpace[GeoCoordinates[N], UnittedQuantity[Angle, N], N] =
    new LengthSpace[GeoCoordinates[N], UnittedQuantity[Angle, N], N] {

      val metricSpace: MetricSpace[GeoCoordinates[N], UnittedQuantity[Angle, N]] =
        geoCoordinatesMetricSpace[N]

      import angleConverter.radian
      // val ctn = ConvertableTo[N]
      // val half: N = ctn.fromRational(Rational(1, 2))

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

      def onPath(p1: GeoCoordinates[N], p2: GeoCoordinates[N], f: N): GeoCoordinates[N] = {

        val d = distance(p1, p2)

        val A = sine(d :* (Field[N].one - f)) / sine(d)
        val B = sine(d :* f) / sine(d)
        val x = A * cosine(p1.latitude) * cosine(p1.longitude) + B * cosine(p2.latitude) * cosine(p2.longitude)
        val y = A * cosine(p1.latitude) * sine(p1.longitude) + B * cosine(p2.latitude) * sine(p2.longitude)
        val z = A * sine(p1.latitude) + B * sine(p2.latitude)

        val latitude = arcTangent2(z, √(square(x) + square(y)))
        val longitude = arcTangent2(y, x)

        GeoCoordinates(latitude, longitude)
      }

      // assumes that v is on the path from p1 to p2
      def portion(p1: GeoCoordinates[N], v: GeoCoordinates[N], p2: GeoCoordinates[N]): N = {
        val num = (distance(p1, v) in radian).magnitude
        val denom = (distance(p1, p2) in radian).magnitude
        num / denom
      }
    }
}
