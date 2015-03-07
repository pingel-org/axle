package axle.quanta

import axle.algebra.Vertex
import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field
import spire.math.Rational
import spire.math.Real

case class Information[N]() extends Quantum4[N] {

  type Q = Information[N]

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Information"

  def unit(name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement4[Q, N](name, symbol, wiki)

  def bit: UnitOfMeasurement4[Q, N] = unit("bit", "b")
  def nibble: UnitOfMeasurement4[Q, N] = unit("nibble", "nibble")
  def byte: UnitOfMeasurement4[Q, N] = unit("byte", "B", Some("http://en.wikipedia.org/wiki/Byte"))
  def kilobyte: UnitOfMeasurement4[Q, N] = unit("kilobyte", "KB")
  def megabyte: UnitOfMeasurement4[Q, N] = unit("megabyte", "MB")
  def gigabyte: UnitOfMeasurement4[Q, N] = unit("gigabyte", "GB")
  def terabyte: UnitOfMeasurement4[Q, N] = unit("terabyte", "TB")
  def petabyte: UnitOfMeasurement4[Q, N] = unit("petabyte", "PB")

  // TODO PB TB GB MB KB

  def units: List[UnitOfMeasurement4[Q, N]] =
    List(bit, nibble, byte, kilobyte, megabyte, gigabyte, terabyte, petabyte)

  def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement4[Q, N], UnitOfMeasurement4[Q, N], Bijection[N, N])] =
    List[(UnitOfMeasurement4[Q, N], UnitOfMeasurement4[Q, N], Bijection[N, N])](
      (bit, byte, Scale2s(3)),
      (byte, kilobyte, Scale2s(10)),
      (kilobyte, megabyte, Scale2s(10)),
      (megabyte, gigabyte, Scale2s(10)),
      (gigabyte, terabyte, Scale2s(10)),
      (terabyte, petabyte, Scale2s(10)))

}
