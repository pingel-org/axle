package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Acceleration() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Degree_(angle)"

}

trait AccelerationUnits {

  def unit(name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[Acceleration](name, symbol, wiki)

  lazy val mpsps = unit("mps", "mps") // derive

  lazy val fpsps = unit("fps", "fps") // derive

  lazy val g = unit("g", "g", Some("http://en.wikipedia.org/wiki/Standard_gravity"))

}

trait AccelerationMetadata[N] extends QuantumMetadata[Acceleration, N] with AccelerationUnits

object Acceleration {

  def metadata[N: Field: Eq, DG[_, _]: DirectedGraph] =
    new QuantumMetadataGraph[Acceleration, N, DG] with AccelerationMetadata[N] {

      def units: List[UnitOfMeasurement[Acceleration]] =
        List(mpsps, fpsps, g)

      def links: Seq[(UnitOfMeasurement[Acceleration], UnitOfMeasurement[Acceleration], Bijection[N, N])] =
        List[(UnitOfMeasurement[Acceleration], UnitOfMeasurement[Acceleration], Bijection[N, N])](
          (mpsps, g, ScaleDouble(9.80665)))
    }

}
