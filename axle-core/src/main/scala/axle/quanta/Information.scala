package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import axle.algebra.Scale2s
import spire.algebra.Eq
import spire.algebra.Field

case class Information() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Information"

}

trait InformationUnits extends QuantumUnits[Information] {

  lazy val bit = unit("bit", "b")
  lazy val nibble = unit("nibble", "nibble")
  lazy val byte = unit("byte", "B", Some("http://en.wikipedia.org/wiki/Byte"))
  lazy val kilobyte = unit("kilobyte", "KB")
  lazy val megabyte = unit("megabyte", "MB")
  lazy val gigabyte = unit("gigabyte", "GB")
  lazy val terabyte = unit("terabyte", "TB")
  lazy val petabyte = unit("petabyte", "PB")

  // TODO PB TB GB MB KB

  def units: List[UnitOfMeasurement[Information]] =
    List(bit, nibble, byte, kilobyte, megabyte, gigabyte, terabyte, petabyte)

}

trait InformationConverter[N] extends UnitConverter[Information, N] with InformationUnits {

  def defaultUnit = byte
}

object Information {

  def converterGraph[N: Field: Eq, DG[_, _]: DirectedGraph] =
    new UnitConverterGraph[Information, N, DG] with InformationConverter[N] {

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