package axle.quanta

import axle.algebra.Vertex
import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field
import spire.math.Rational
import spire.math.Real

object InformationDouble extends Information[Double]()

case class Information[N]() extends Quantum4[N] {

  type Q = Information[N]

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Information"

  def unit(name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement4[Information[N], N](name, symbol, wiki)

  lazy val bit = unit("bit", "b")
  lazy val nibble = unit("nibble", "nibble")
  lazy val byte = unit("byte", "B", Some("http://en.wikipedia.org/wiki/Byte"))
  lazy val kilobyte = unit("kilobyte", "KB")
  lazy val megabyte = unit("megabyte", "MB")
  lazy val gigabyte = unit("gigabyte", "GB")
  lazy val terabyte = unit("terabyte", "TB")
  lazy val petabyte = unit("petabyte", "PB")

  // TODO PB TB GB MB KB

  def units: List[UnitOfMeasurement4[Information[N], N]] =
    List(bit, nibble, byte, kilobyte, megabyte, gigabyte, terabyte, petabyte)

  def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement4[Information[N], N], UnitOfMeasurement4[Information[N], N], Bijection[N, N])] =
    List[(UnitOfMeasurement4[Information[N], N], UnitOfMeasurement4[Information[N], N], Bijection[N, N])](
      (bit, byte, Scale2s(3)),
      (byte, kilobyte, Scale2s(10)),
      (kilobyte, megabyte, Scale2s(10)),
      (megabyte, gigabyte, Scale2s(10)),
      (gigabyte, terabyte, Scale2s(10)),
      (terabyte, petabyte, Scale2s(10)))

}
