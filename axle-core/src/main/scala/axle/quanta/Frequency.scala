package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Frequency() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Frequency"

}

trait FrequencyUnits[N] {

  type U = UnitOfMeasurement[Frequency, N]

  def degree: U
  def hertz: U
  def Hz: U
  def kilohertz: U
  def KHz: U
  def megahertz: U
  def MHz: U
  def gigahertz: U
  def GHz: U
}

trait FrequencyMetadata[N] extends QuantumMetadata[Frequency, N] with FrequencyUnits[N]

object Frequency {

  def metadata[N: Field: Eq, DG[_, _]: DirectedGraph] =
    new QuantumMetadataGraph[Frequency, N, DG] with FrequencyMetadata[N] {

      def unit(name: String, symbol: String, wiki: Option[String] = None) =
        UnitOfMeasurement[Frequency, N](name, symbol, wiki)

      lazy val _degree = unit("degree", "°", Some("http://en.wikipedia.org/wiki/Degree_(Frequency)"))
      lazy val _hertz = unit("Hertz", "Hz", Some("http://en.wikipedia.org/wiki/Hertz"))
      lazy val _kilohertz = unit("Kilohertz", "KHz")
      lazy val _megahertz = unit("Megahertz", "MHz")
      lazy val _gigahertz = unit("Gigahertz", "GHz")

      def degree = _degree
      def hertz = _hertz
      def Hz = _hertz
      def kilohertz = _kilohertz
      def KHz = _kilohertz
      def megahertz = _megahertz
      def MHz = _megahertz
      def gigahertz = _gigahertz
      def GHz = _gigahertz

      def units: List[UnitOfMeasurement[Frequency, N]] =
        List(degree)

      def links: Seq[(UnitOfMeasurement[Frequency, N], UnitOfMeasurement[Frequency, N], Bijection[N, N])] =
        List[(UnitOfMeasurement[Frequency, N], UnitOfMeasurement[Frequency, N], Bijection[N, N])](
          (Hz, KHz, Scale10s(3)),
          (Hz, MHz, Scale10s(9)),
          (Hz, GHz, Scale10s(12)))

    }

}