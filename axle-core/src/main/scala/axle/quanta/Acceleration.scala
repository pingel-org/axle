package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Acceleration() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Degree_(angle)"

}

abstract class AccelerationMetadata[N: Field: Eq, DG[_, _]: DirectedGraph]
  extends QuantumMetadata[Acceleration, N, DG] {

  type U = UnitOfMeasurement[Acceleration, N]

  def mpsps: U
  def fpsps: U
  def g: U
}

object Acceleration {

  def metadata[N: Field: Eq, DG[_, _]: DirectedGraph] = new AccelerationMetadata[N, DG] {

    def unit(name: String, symbol: String, wiki: Option[String] = None) =
      UnitOfMeasurement[Acceleration, N](name, symbol, wiki)

    lazy val _mpsps = unit("mps", "mps") // derive
    lazy val _fpsps = unit("fps", "fps") // derive
    lazy val _g = unit("g", "g", Some("http://en.wikipedia.org/wiki/Standard_gravity"))

    def mpsps = _mpsps
    def fpsps = _fpsps
    def g = _g

    def units: List[UnitOfMeasurement[Acceleration, N]] =
      List(mpsps, fpsps, g)

    def links: Seq[(UnitOfMeasurement[Acceleration, N], UnitOfMeasurement[Acceleration, N], Bijection[N, N])] =
      List[(UnitOfMeasurement[Acceleration, N], UnitOfMeasurement[Acceleration, N], Bijection[N, N])](
        (mpsps, g, ScaleDouble(9.80665)))
  }

}
