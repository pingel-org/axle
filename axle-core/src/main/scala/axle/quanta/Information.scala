package axle.quanta

import axle.algebra.Vertex
import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field
import spire.math.Rational
import spire.math.Real

case class Information() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Information"

}

trait InformationMetadata[N] extends QuantumMetadata[Information, N] {

  def bit: UnitOfMeasurement[Information, N]

  def nibble: UnitOfMeasurement[Information, N]

  def byte: UnitOfMeasurement[Information, N]

}

object Information {

  def metadata[N] = new InformationMetadata[N] {

    def unit(name: String, symbol: String, wiki: Option[String] = None) =
      UnitOfMeasurement[Information, N](name, symbol, wiki)

    lazy val _bit = unit("bit", "b")
    lazy val _nibble = unit("nibble", "nibble")
    lazy val _byte = unit("byte", "B", Some("http://en.wikipedia.org/wiki/Byte"))
    lazy val kilobyte = unit("kilobyte", "KB")
    lazy val megabyte = unit("megabyte", "MB")
    lazy val gigabyte = unit("gigabyte", "GB")
    lazy val terabyte = unit("terabyte", "TB")
    lazy val petabyte = unit("petabyte", "PB")

    // TODO PB TB GB MB KB

    def bit = _bit
    def nibble = _nibble
    def byte = _byte

    def units: List[UnitOfMeasurement[Information, N]] =
      List(_bit, _nibble, _byte, kilobyte, megabyte, gigabyte, terabyte, petabyte)

    def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement[Information, N], UnitOfMeasurement[Information, N], Bijection[N, N])] =
      List[(UnitOfMeasurement[Information, N], UnitOfMeasurement[Information, N], Bijection[N, N])](
        (_bit, _byte, Scale2s(3)),
        (_byte, kilobyte, Scale2s(10)),
        (kilobyte, megabyte, Scale2s(10)),
        (megabyte, gigabyte, Scale2s(10)),
        (gigabyte, terabyte, Scale2s(10)),
        (terabyte, petabyte, Scale2s(10)))

  }

}