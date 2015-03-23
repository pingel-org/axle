package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import axle.algebra.ScaleInt
import axle.algebra.ScaleDouble
import axle.algebra.Scale10s
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

  def units: List[UnitOfMeasurement[Speed]] =
    List(mps, fps, mph, kph, knot, c)

}

trait SpeedConverter[N] extends UnitConverter[Speed, N] with SpeedUnits

object Speed {

  def converterGraph[N: Field: Eq, DG[_, _]: DirectedGraph] =
    new UnitConverterGraph[Speed, N, DG] with SpeedConverter[N] {

      def links: Seq[(UnitOfMeasurement[Speed], UnitOfMeasurement[Speed], Bijection[N, N])] =
        List[(UnitOfMeasurement[Speed], UnitOfMeasurement[Speed], Bijection[N, N])](
          (knot, kph, ScaleDouble(1.852)),
          (mps, c, ScaleInt(299792458)))

    }

}