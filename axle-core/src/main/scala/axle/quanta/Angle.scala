package axle.quanta

import scala.math.{ Pi => π }

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field
import spire.math.Rational

object AngleDouble extends Angle[Double]()

object AngleFloat extends Angle[Float]()

object AngleRational extends Angle[Rational]()

case class Angle[N]() extends Quantum[N] {

  type Q = Angle[N]

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Degree_(angle)"

  def unit(name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[Angle[N], N](name, symbol, wiki)

  lazy val degree = unit("degree", "°", Some("http://en.wikipedia.org/wiki/Degree_(angle)"))
  lazy val ° = degree
  lazy val radian = unit("radian", "rad", Some("http://en.wikipedia.org/wiki/Radian"))
  lazy val rad = radian
  lazy val circleDegrees = unit("circleDegrees", "circle", Some("http://en.wikipedia.org/wiki/Circle"))
  lazy val circleRadians = unit("circleRadians", "circle", Some("http://en.wikipedia.org/wiki/Circle"))

  //  def clockwise90[N: Field: Eq] = -90 *: °[N]
  //  def counterClockwise90[N: Field: Eq] = 90 *: °[N]

  def units: List[UnitOfMeasurement[Angle[N], N]] =
    List(degree, radian, circleDegrees, circleRadians)

  def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement[Angle[N], N], UnitOfMeasurement[Angle[N], N], Bijection[N, N])] =
    List[(UnitOfMeasurement[Angle[N], N], UnitOfMeasurement[Angle[N], N], Bijection[N, N])](
      (degree, circleDegrees, ScaleInt(360)),
      (radian, circleRadians, ScaleDouble(2 * π)),
      (circleDegrees, circleRadians, BijectiveIdentity[N]))

}
