package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import axle.algebra.Scale
import cats.kernel.Eq
import spire.algebra.Field

case class Acceleration() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Degree_(angle)"

}

trait AccelerationUnits extends QuantumUnits[Acceleration] {

  lazy val mpsps = unit("mps", "mps") // derive

  lazy val fpsps = unit("fps", "fps") // derive

  lazy val g = unit("g", "g", Some("http://en.wikipedia.org/wiki/Standard_gravity"))

  def units: List[UnitOfMeasurement[Acceleration]] =
    List(mpsps, fpsps, g)

}

trait AccelerationConverter[N] extends UnitConverter[Acceleration, N] with AccelerationUnits {

  def defaultUnit = mpsps

}

object Acceleration {

  import axle.algebra.Module
  import spire.math._

  def converterGraphK2[N: Field: Eq, DG[_, _]](
    implicit
    module: Module[N, Rational],
    evDG:   DirectedGraph[DG[UnitOfMeasurement[Acceleration], N => N], UnitOfMeasurement[Acceleration], N => N]) =
    converterGraph[N, DG[UnitOfMeasurement[Acceleration], N => N]]

  def converterGraph[N: Field: Eq, DG](
    implicit
    module: Module[N, Rational],
    evDG:   DirectedGraph[DG, UnitOfMeasurement[Acceleration], N => N]) =
    new UnitConverterGraph[Acceleration, N, DG] with AccelerationConverter[N] {

      def links: Seq[(UnitOfMeasurement[Acceleration], UnitOfMeasurement[Acceleration], Bijection[N, N])] =
        List[(UnitOfMeasurement[Acceleration], UnitOfMeasurement[Acceleration], Bijection[N, N])](
          (mpsps, g, Scale(9.80665)))
    }

}
