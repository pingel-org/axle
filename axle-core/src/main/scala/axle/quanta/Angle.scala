package axle.quanta

import scala.math.{ Pi => π }

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field
import spire.math.Rational

case class Angle() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Degree_(angle)"

}

trait AngleUnits {

  def unit(name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[Angle](name, symbol, wiki)

  lazy val degree = unit("degree", "°", Some("http://en.wikipedia.org/wiki/Degree_(angle)"))
  lazy val radian = unit("radian", "rad", Some("http://en.wikipedia.org/wiki/Radian"))
  lazy val circleDegrees = unit("circleDegrees", "circle", Some("http://en.wikipedia.org/wiki/Circle"))
  lazy val circleRadians = unit("circleRadians", "circle", Some("http://en.wikipedia.org/wiki/Circle"))

  lazy val ° = degree
  lazy val rad = radian

  //  def clockwise90[N: Field: Eq] = -90 *: °[N]
  //  def counterClockwise90[N: Field: Eq] = 90 *: °[N]

}

trait AngleMetadata[N] extends QuantumMetadata[Angle, N] with AngleUnits

object Angle {

  def metadata[N: Field: Eq, DG[_, _]: DirectedGraph] =
    new QuantumMetadataGraph[Angle, N, DG] with AngleMetadata[N] {

      def units: List[UnitOfMeasurement[Angle]] =
        List(degree, radian, circleDegrees, circleRadians)

      def links: Seq[(UnitOfMeasurement[Angle], UnitOfMeasurement[Angle], Bijection[N, N])] =
        List[(UnitOfMeasurement[Angle], UnitOfMeasurement[Angle], Bijection[N, N])](
          (degree, circleDegrees, ScaleInt(360)),
          (radian, circleRadians, ScaleDouble(2 * π)),
          (circleDegrees, circleRadians, BijectiveIdentity[N]))

    }

}