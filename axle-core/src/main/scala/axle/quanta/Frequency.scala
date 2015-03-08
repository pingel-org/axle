package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Frequency[N]() extends Quantum[N] {

  type Q = Frequency[N]

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Frequency"
  
  def unit(name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[Frequency[N], N](name, symbol, wiki)

  lazy val degree = unit("degree", "°", Some("http://en.wikipedia.org/wiki/Degree_(Frequency)"))
  lazy val hertz = unit("Hertz", "Hz", Some("http://en.wikipedia.org/wiki/Hertz"))
  lazy val Hz = hertz
  lazy val kilohertz = unit("Kilohertz", "KHz")
  lazy val KHz = kilohertz
  lazy val megahertz = unit("Megahertz", "MHz")
  lazy val MHz = megahertz
  lazy val gigahertz = unit("Gigahertz", "GHz")
  lazy val GHz = gigahertz

  def units: List[UnitOfMeasurement[Frequency[N], N]] =
    List(degree)

  def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement[Frequency[N], N], UnitOfMeasurement[Frequency[N], N], Bijection[N, N])] =
    List[(UnitOfMeasurement[Frequency[N], N], UnitOfMeasurement[Frequency[N], N], Bijection[N, N])](
      (Hz, KHz, Scale10s(3)),
      (Hz, MHz, Scale10s(9)),
      (Hz, GHz, Scale10s(12)))

}
