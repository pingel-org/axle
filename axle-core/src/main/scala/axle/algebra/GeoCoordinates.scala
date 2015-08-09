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

  implicit def geoCoordinatesLengthSpace[N: Field: Eq: ConvertableFrom: ConvertableTo](
    implicit angleConverter: AngleConverter[N],
    distanceConverter: DistanceConverter[N]): LengthSpace[GeoCoordinates[N], UnittedQuantity[Distance, N]] =
    new LengthSpace[GeoCoordinates[N], UnittedQuantity[Distance, N]] {

      import distanceConverter.km
      val ctn = ConvertableTo[N]
      val cfn = ConvertableFrom[N]
      val earthRadius = ctn.fromDouble(6371d) *: km
      val half = ctn.fromRational(Rational(1, 2))

      // Haversine distance

      def distance(v: GeoCoordinates[N], w: GeoCoordinates[N]): UnittedQuantity[Distance, N] = {
        val dLat = w.latitude - v.latitude
        val dLon = w.longitude - v.longitude
        val a = square(sine(dLat :* half)) + square(sine(dLon :* half)) * cosine(v.latitude) * cosine(w.latitude)
        val c = ctn.fromDouble(2 * atan2(sqrt(a), sqrt(1 - a)))
        earthRadius :* c
      }

      def onPath(left: GeoCoordinates[N], right: GeoCoordinates[N], p: Double): GeoCoordinates[N] = {
        val portion = ctn.fromDouble(p)
        GeoCoordinates(???, ???)
      }

      def portion(left: GeoCoordinates[N], v: GeoCoordinates[N], right: GeoCoordinates[N]): Double = {
        val num = (distance(left, v) in km).magnitude
        val denom = (distance(left, right) in km).magnitude
        cfn.toDouble(num / denom)
      }
    }
}
