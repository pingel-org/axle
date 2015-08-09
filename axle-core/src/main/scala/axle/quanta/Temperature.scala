package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import axle.algebra.Transform
import axle.algebra.Scale
import spire.algebra.Eq
import spire.algebra.Field
import spire.algebra.Group

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

  def converterGraph[N: ConvertableTo: Eq, DG](
    implicit module: Module[N, Rational], field: Field[N],
    evDG: DirectedGraph[DG, UnitOfMeasurement[Temperature], N => N]) =
    new UnitConverterGraph[Temperature, N, DG] with TemperatureConverter[N] {

      def links: Seq[(UnitOfMeasurement[Temperature], UnitOfMeasurement[Temperature], Bijection[N, N])] =
        List[(UnitOfMeasurement[Temperature], UnitOfMeasurement[Temperature], Bijection[N, N])](
          (celsius, kelvin, Transform[N](ConvertableTo[N].fromDouble(-273.15))(field.additive)),
          (celsius, fahrenheit, Transform[N](-32)(field.additive).bidirectionallyAndThen(Scale[N, Rational](Rational(5, 9)))))

    }

}