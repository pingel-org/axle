package axle.quanta

import axle.algebra.Vertex
import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field
import spire.math.Rational
import spire.math.Real

case class Information() extends Quantum("http://en.wikipedia.org/wiki/Information")

object Information {

  type Q = Information

  def unit[N](name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[Q, N](name, symbol, wiki)

  def bit[N]: UnitOfMeasurement[Q, N] = unit("bit", "b")
  def nibble[N]: UnitOfMeasurement[Q, N] = unit("nibble", "nibble")
  def byte[N]: UnitOfMeasurement[Q, N] = unit("byte", "B", Some("http://en.wikipedia.org/wiki/Byte"))
  def kilobyte[N]: UnitOfMeasurement[Q, N] = unit("kilobyte", "KB")
  def megabyte[N]: UnitOfMeasurement[Q, N] = unit("megabyte", "MB")
  def gigabyte[N]: UnitOfMeasurement[Q, N] = unit("gigabyte", "GB")
  def terabyte[N]: UnitOfMeasurement[Q, N] = unit("terabyte", "TB")
  def petabyte[N]: UnitOfMeasurement[Q, N] = unit("petabyte", "PB")

  // TODO PB TB GB MB KB

  def units[N]: List[UnitOfMeasurement[Q, N]] =
    List(bit, nibble, byte, kilobyte, megabyte, gigabyte, terabyte, petabyte)

  def links[N: Field]: Seq[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])] =
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])](
      (bit, byte, Scale2s(3)),
      (byte, kilobyte, Scale2s(10)),
      (kilobyte, megabyte, Scale2s(10)),
      (megabyte, gigabyte, Scale2s(10)),
      (gigabyte, terabyte, Scale2s(10)),
      (terabyte, petabyte, Scale2s(10)))

  implicit def conversionGraph[N: Field: Eq, DG[_, _]: DirectedGraph] =
    Quantum.cgn(units[N], links)

}
