package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Volume() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Volume"

}

trait VolumeUnits extends QuantumUnits[Volume] {

  lazy val m3 = unit("m3", "m3") // derive
  lazy val km3 = unit("km3", "km3") // derive
  lazy val cm3 = unit("cm3", "cm3") // derive
  lazy val liter = unit("liter", "L", Some("http://en.wikipedia.org/wiki/Liter"))
  lazy val L = liter
  lazy val ℓ = liter
  lazy val milliliter = unit("milliliter", "mL")

  def units: List[UnitOfMeasurement[Volume]] =
    List(m3, km3, cm3, liter, milliliter)

}

trait VolumeConverter[N] extends UnitConverter[Volume, N] with VolumeUnits

object Volume {

  def converterGraph[N: Field: Eq, DG[_, _]: DirectedGraph] =
    new UnitConverterGraph[Volume, N, DG] with VolumeConverter[N] {

      def links: Seq[(UnitOfMeasurement[Volume], UnitOfMeasurement[Volume], Bijection[N, N])] =
        List[(UnitOfMeasurement[Volume], UnitOfMeasurement[Volume], Bijection[N, N])](
          (milliliter, liter, Scale10s(3)),
          (cm3, milliliter, BijectiveIdentity[N]))

    }
}