package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Frequency() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Frequency"

}

trait FrequencyMetadata[N] extends QuantumMetadata[Frequency, N] {

  type U = UnitOfMeasurement[Frequency, N]

}

object Frequency {

  def metadata[N] = new FrequencyMetadata[N] {

    def unit(name: String, symbol: String, wiki: Option[String] = None) =
      UnitOfMeasurement[Frequency, N](name, symbol, wiki)

    lazy val degree = unit("degree", "°", Some("http://en.wikipedia.org/wiki/Degree_(Frequency)"))
    lazy val hertz = unit("Hertz", "Hz", Some("http://en.wikipedia.org/wiki/Hertz"))
    lazy val Hz = hertz
    lazy val kilohertz = unit("Kilohertz", "KHz")
    lazy val KHz = kilohertz
    lazy val megahertz = unit("Megahertz", "MHz")
    lazy val MHz = megahertz
    lazy val gigahertz = unit("Gigahertz", "GHz")
    lazy val GHz = gigahertz

    def units: List[UnitOfMeasurement[Frequency, N]] =
      List(degree)

    def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement[Frequency, N], UnitOfMeasurement[Frequency, N], Bijection[N, N])] =
      List[(UnitOfMeasurement[Frequency, N], UnitOfMeasurement[Frequency, N], Bijection[N, N])](
        (Hz, KHz, Scale10s(3)),
        (Hz, MHz, Scale10s(9)),
        (Hz, GHz, Scale10s(12)))

  }

}