package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case object Frequency extends Quantum {

  type Q = Frequency.type

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Frequency"
  
  def unit[N](name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[Q, N](name, symbol, wiki)

  def degree[N]: UnitOfMeasurement[Q, N] = unit("degree", "°", Some("http://en.wikipedia.org/wiki/Degree_(Frequency)"))
  def hertz[N]: UnitOfMeasurement[Q, N] = unit("Hertz", "Hz", Some("http://en.wikipedia.org/wiki/Hertz"))
  def Hz[N] = hertz[N]
  def kilohertz[N]: UnitOfMeasurement[Q, N] = unit("Kilohertz", "KHz")
  def KHz[N] = kilohertz[N]
  def megahertz[N]: UnitOfMeasurement[Q, N] = unit("Megahertz", "MHz")
  def MHz[N] = megahertz[N]
  def gigahertz[N]: UnitOfMeasurement[Q, N] = unit("Gigahertz", "GHz")
  def GHz[N] = gigahertz[N]

  def units[N]: List[UnitOfMeasurement[Q, N]] =
    List(degree)

  def links[N: Field]: Seq[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])] =
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])](
      (Hz, KHz, Scale10s(3)),
      (Hz, MHz, Scale10s(9)),
      (Hz, GHz, Scale10s(12)))

//  implicit def conversionGraph[N: Field: Eq, DG[_, _]: DirectedGraph] =
//    cgn(units[N], links)

}
