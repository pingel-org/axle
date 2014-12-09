package axle.quanta

import axle.algebra.Vertex
import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field
import spire.math.Rational
import spire.math.Real

case class Information3() extends Quantum3

object Information3 extends Quantum3 {

  def wikipediaUrl = "http://en.wikipedia.org/wiki/Information"

  def unit[N: Field: Eq](name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement3[Information3, N](name, symbol, wiki)

  def bit[N: Field: Eq] = unit("bit", "b")
  def nibble[N: Field: Eq] = unit("nibble", "nibble")
  def byte[N: Field: Eq] = unit("byte", "B", Some("http://en.wikipedia.org/wiki/Byte"))
  def kilobyte[N: Field: Eq] = unit("kilobyte", "KB")
  def megabyte[N: Field: Eq] = unit("megabyte", "MB")
  def gigabyte[N: Field: Eq] = unit("gigabyte", "GB")
  def terabyte[N: Field: Eq] = unit("terabyte", "TB")
  def petabyte[N: Field: Eq] = unit("petabyte", "PB")

  // TODO PB TB GB MB KB

  def units[N: Field: Eq]: List[UnitOfMeasurement3[Information3, N]] =
    List(bit, nibble, byte, kilobyte, megabyte, gigabyte, terabyte, petabyte)

  def links[N: Field: Eq]: Seq[(UnitOfMeasurement3[Information3, N], UnitOfMeasurement3[Information3, N], Bijection[N, N])] =
    List[(UnitOfMeasurement3[Information3, N], UnitOfMeasurement3[Information3, N], Bijection[N, N])](
      (bit, byte, Scale2s(3)),
      (byte, kilobyte, Scale2s(10)),
      (kilobyte, megabyte, Scale2s(10)),
      (megabyte, gigabyte, Scale2s(10)),
      (gigabyte, terabyte, Scale2s(10)),
      (terabyte, petabyte, Scale2s(10)))

  implicit def conversionGraph[N: Field: Eq, DG[_, _]: DirectedGraph] =
    Quantum3.cgn(units, links)

}
