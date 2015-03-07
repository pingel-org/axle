package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Frequency[N]() extends Quantum4[N] {

  type Q = Frequency[N]

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Frequency"
  
  def unit(name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement4[Q, N](name, symbol, wiki)

  def degree: UnitOfMeasurement4[Q, N] = unit("degree", "°", Some("http://en.wikipedia.org/wiki/Degree_(Frequency)"))
  def hertz: UnitOfMeasurement4[Q, N] = unit("Hertz", "Hz", Some("http://en.wikipedia.org/wiki/Hertz"))
  def Hz = hertz
  def kilohertz: UnitOfMeasurement4[Q, N] = unit("Kilohertz", "KHz")
  def KHz = kilohertz
  def megahertz: UnitOfMeasurement4[Q, N] = unit("Megahertz", "MHz")
  def MHz = megahertz
  def gigahertz: UnitOfMeasurement4[Q, N] = unit("Gigahertz", "GHz")
  def GHz = gigahertz

  def units: List[UnitOfMeasurement4[Q, N]] =
    List(degree)

  def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement4[Q, N], UnitOfMeasurement4[Q, N], Bijection[N, N])] =
    List[(UnitOfMeasurement4[Q, N], UnitOfMeasurement4[Q, N], Bijection[N, N])](
      (Hz, KHz, Scale10s(3)),
      (Hz, MHz, Scale10s(9)),
      (Hz, GHz, Scale10s(12)))

}
