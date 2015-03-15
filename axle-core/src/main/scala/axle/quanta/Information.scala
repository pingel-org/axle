package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Field
import spire.algebra.Eq

case class Information() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Information"

}

abstract class InformationMetadata[N: Field: Eq, DG[_, _]: DirectedGraph]
  extends QuantumMetadataGraph[Information, N, DG] {

  type U = UnitOfMeasurement[Information, N]

  def bit: U
  def nibble: U
  def byte: U
  def kilobyte: U
  def megabyte: U
  def gigabyte: U
  def terabyte: U
  def petabyte: U

}

object Information {

  def metadata[N: Field: Eq, DG[_, _]: DirectedGraph] = new InformationMetadata[N, DG] {

    def unit(name: String, symbol: String, wiki: Option[String] = None) =
      UnitOfMeasurement[Information, N](name, symbol, wiki)

    lazy val _bit = unit("bit", "b")
    lazy val _nibble = unit("nibble", "nibble")
    lazy val _byte = unit("byte", "B", Some("http://en.wikipedia.org/wiki/Byte"))
    lazy val _kilobyte = unit("kilobyte", "KB")
    lazy val _megabyte = unit("megabyte", "MB")
    lazy val _gigabyte = unit("gigabyte", "GB")
    lazy val _terabyte = unit("terabyte", "TB")
    lazy val _petabyte = unit("petabyte", "PB")

    // TODO PB TB GB MB KB

    def bit = _bit
    def nibble = _nibble
    def byte = _byte
    def kilobyte = _kilobyte
    def megabyte = _megabyte
    def gigabyte = _gigabyte
    def terabyte = _terabyte
    def petabyte = _petabyte

    def units: List[UnitOfMeasurement[Information, N]] =
      List(bit, nibble, byte, kilobyte, megabyte, gigabyte, terabyte, petabyte)

    def links: Seq[(UnitOfMeasurement[Information, N], UnitOfMeasurement[Information, N], Bijection[N, N])] =
      List[(UnitOfMeasurement[Information, N], UnitOfMeasurement[Information, N], Bijection[N, N])](
        (bit, byte, Scale2s(3)),
        (byte, kilobyte, Scale2s(10)),
        (kilobyte, megabyte, Scale2s(10)),
        (megabyte, gigabyte, Scale2s(10)),
        (gigabyte, terabyte, Scale2s(10)),
        (terabyte, petabyte, Scale2s(10)))

  }

}