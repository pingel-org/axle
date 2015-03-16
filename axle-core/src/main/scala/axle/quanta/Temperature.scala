package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
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

trait TemperatureConverter[N] extends UnitConverter[Temperature, N] with TemperatureUnits

object Temperature {

  import spire.math._
  import spire.implicits._

  def converterGraph[N: Field: Eq, DG[_, _]: DirectedGraph] =
    new UnitConverterGraph[Temperature, N, DG] with TemperatureConverter[N] {

      def links: Seq[(UnitOfMeasurement[Temperature], UnitOfMeasurement[Temperature], Bijection[N, N])] =
        List[(UnitOfMeasurement[Temperature], UnitOfMeasurement[Temperature], Bijection[N, N])](
          (celsius, kelvin, new Bijection[N, N] {
            def apply(c: N) = c + 273.15d
            def unapply(k: N) = k - 273.15d
          }),
          (celsius, fahrenheit, new Bijection[N, N] {
            def apply(c: N) = (c * 9 / 5) + 32
            def unapply(f: N) = (f - 32) * 5 / 9
          }))

    }

}