package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Speed() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Speed"

}

trait SpeedUnits extends QuantumUnits[Speed] {

  lazy val mps = unit("mps", "mps") // derive
  lazy val fps = unit("fps", "fps") // derive
  lazy val mph = unit("mph", "mph") // derive
  lazy val kph = unit("kph", "kph") // derive
  lazy val knot = unit("knot", "kn", Some("http://en.wikipedia.org/wiki/Knot_(unit)"))
  lazy val kn = knot
  lazy val c = unit("Light Speed", "c", Some("http://en.wikipedia.org/wiki/Speed_of_light"))
  lazy val speedLimit = unit("Speed limit", "speed limit")

  def units: List[UnitOfMeasurement[Speed]] =
    List(mps, fps, mph, kph, knot, c, speedLimit)

}

trait SpeedMetadata[N] extends QuantumMetadata[Speed, N] with SpeedUnits

object Speed {

  def metadata[N: Field: Eq, DG[_, _]: DirectedGraph] =
    new QuantumMetadataGraph[Speed, N, DG] with SpeedMetadata[N] {

      def links: Seq[(UnitOfMeasurement[Speed], UnitOfMeasurement[Speed], Bijection[N, N])] =
        List[(UnitOfMeasurement[Speed], UnitOfMeasurement[Speed], Bijection[N, N])](
          (knot, kph, ScaleDouble(1.852)),
          (mps, c, ScaleInt(299792458)),
          (mph, speedLimit, ScaleInt(65)))

    }

}