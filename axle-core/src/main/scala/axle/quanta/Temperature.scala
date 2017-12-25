package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import axle.algebra.Transform
import axle.algebra.Scale
import cats.kernel.Eq
import spire.algebra.Field

case class Temperature() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Temperature"

}

trait TemperatureUnits extends QuantumUnits[Temperature] {

  lazy val kelvin = unit("kelvin", "K")
  lazy val celsius = unit("celsius", "C")
  lazy val fahrenheit = unit("fahrenheit", "f")

  def units: List[UnitOfMeasurement[Temperature]] =
    List(kelvin, celsius, fahrenheit)

}

trait TemperatureConverter[N] extends UnitConverter[Temperature, N] with TemperatureUnits {

  def defaultUnit = celsius
}

object Temperature {

  import spire.algebra.Module
  import spire.math._
  import spire.implicits._

  def converterGraphK2[N: ConvertableTo: Eq, DG[_, _]](
    implicit
    module: Module[N, Rational], field: Field[N],
    evDG: DirectedGraph[DG[UnitOfMeasurement[Temperature], N => N], UnitOfMeasurement[Temperature], N => N]) =
    converterGraph[N, DG[UnitOfMeasurement[Temperature], N => N]]

  def converterGraph[N: ConvertableTo: Eq, DG](
    implicit
    module: Module[N, Rational], field: Field[N],
    evDG: DirectedGraph[DG, UnitOfMeasurement[Temperature], N => N]) =
    new UnitConverterGraph[Temperature, N, DG] with TemperatureConverter[N] {

      def links: Seq[(UnitOfMeasurement[Temperature], UnitOfMeasurement[Temperature], Bijection[N, N])] =
        List[(UnitOfMeasurement[Temperature], UnitOfMeasurement[Temperature], Bijection[N, N])](
          (celsius, kelvin, Transform[N](ConvertableTo[N].fromDouble(-273.15))(field.additive)),
          (celsius, fahrenheit, Transform[N](-32)(field.additive).bidirectionallyAndThen(Scale[N, Rational](Rational(5, 9)))))

    }

}
