package axle.quanta

import cats.kernel.Eq

import spire.algebra.Field
import spire.math.{ pi => π }

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import axle.algebra.Scale
import axle.algebra.BijectiveIdentity

case class Angle() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Degree_(angle)"

}

trait AngleUnits extends QuantumUnits[Angle] {

  lazy val degree = unit("degree", "°", Some("http://en.wikipedia.org/wiki/Degree_(angle)"))
  lazy val radian = unit("radian", "rad", Some("http://en.wikipedia.org/wiki/Radian"))
  lazy val circleDegrees = unit("circleDegrees", "circle", Some("http://en.wikipedia.org/wiki/Circle"))
  lazy val circleRadians = unit("circleRadians", "circle", Some("http://en.wikipedia.org/wiki/Circle"))

  lazy val ° = degree
  lazy val rad = radian

  //  def clockwise90[N: Field: Eq] = -90 *: °[N]
  //  def counterClockwise90[N: Field: Eq] = 90 *: °[N]

  def units: List[UnitOfMeasurement[Angle]] =
    List(degree, radian, circleDegrees, circleRadians)

}

trait AngleConverter[N] extends UnitConverter[Angle, N] with AngleUnits {

  def defaultUnit = radian
}

object Angle {

  import axle.algebra.Module
  import spire.math._

  def converterGraphK2[N: Field: Eq, DG[_, _]](
    implicit
    moduleRational: Module[N, Rational],
    evDG:           DirectedGraph[DG[UnitOfMeasurement[Angle], N => N], UnitOfMeasurement[Angle], N => N]) =
    converterGraph[N, DG[UnitOfMeasurement[Angle], N => N]]

  def converterGraph[N: Field: Eq, DG](
    implicit
    moduleRational: Module[N, Rational],
    evDG:           DirectedGraph[DG, UnitOfMeasurement[Angle], N => N]) =
    new UnitConverterGraph[Angle, N, DG] with AngleConverter[N] {

      def links: Seq[(UnitOfMeasurement[Angle], UnitOfMeasurement[Angle], Bijection[N, N])] =
        List[(UnitOfMeasurement[Angle], UnitOfMeasurement[Angle], Bijection[N, N])](
          (degree, circleDegrees, Scale(Rational(360))),
          (radian, circleRadians, Scale(2 * π)),
          (circleDegrees, circleRadians, BijectiveIdentity[N]))

    }

}
