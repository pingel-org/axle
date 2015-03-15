package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Field
import spire.algebra.Eq

case class Information() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Information"

}

trait InformationUnits {

  def unit(name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[Information](name, symbol, wiki)

  lazy val bit = unit("bit", "b")
  lazy val nibble = unit("nibble", "nibble")
  lazy val byte = unit("byte", "B", Some("http://en.wikipedia.org/wiki/Byte"))
  lazy val kilobyte = unit("kilobyte", "KB")
  lazy val megabyte = unit("megabyte", "MB")
  lazy val gigabyte = unit("gigabyte", "GB")
  lazy val terabyte = unit("terabyte", "TB")
  lazy val petabyte = unit("petabyte", "PB")

  // TODO PB TB GB MB KB

}

trait InformationMetadata[N] extends QuantumMetadata[Information, N] with InformationUnits

object Information {

  def metadata[N: Field: Eq, DG[_, _]: DirectedGraph] =
    new QuantumMetadataGraph[Information, N, DG] with InformationMetadata[N] {

      def units: List[UnitOfMeasurement[Information]] =
        List(bit, nibble, byte, kilobyte, megabyte, gigabyte, terabyte, petabyte)

      def links: Seq[(UnitOfMeasurement[Information], UnitOfMeasurement[Information], Bijection[N, N])] =
        List[(UnitOfMeasurement[Information], UnitOfMeasurement[Information], Bijection[N, N])](
          (bit, byte, Scale2s(3)),
          (byte, kilobyte, Scale2s(10)),
          (kilobyte, megabyte, Scale2s(10)),
          (megabyte, gigabyte, Scale2s(10)),
          (gigabyte, terabyte, Scale2s(10)),
          (terabyte, petabyte, Scale2s(10)))

    }

}