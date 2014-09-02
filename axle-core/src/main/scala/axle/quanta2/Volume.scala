package axle.quanta2

import axle.graph.DirectedGraph
import axle.graph.Vertex
import spire.algebra.Eq
import spire.algebra.Field
import spire.implicits.DoubleAlgebra
import spire.implicits._
import spire.math.Rational
import spire.math.Real

abstract class Volume extends Quantum {
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(time)"
}

object Volume extends Volume {

  type Q = Volume

  def units[N: Field: Eq] = List[UnitOfMeasurement[Q, N]](
    unit("greatLakes", "greatLakes"),
    unit("wineBottle", "wineBottle"),
    unit("nebuchadnezzar", "nebuchadnezzar")
    )

  def links[N: Field: Eq] = {
    implicit val baseCG = cgnDisconnected[N]
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], N => N, N => N)](
      (wineBottle, nebuchadnezzar, _ * 20, _ / 20))
  }

  implicit val cgVolumeRational = cgn[Rational]
  implicit val cgVolumeReal = cgn[Real]
  implicit val cgVolumeDouble = cgn[Double]

  implicit val mtRational = modulize[Volume, Rational]
  implicit val mtReal = modulize[Volume, Real]
  implicit val mtDouble = modulize[Volume, Double]

  def greatLakes[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[UnitOfMeasurement[Volume, N], N => N]) = byName(cg, "greatLakes")
  def wineBottle[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[UnitOfMeasurement[Volume, N], N => N]) = byName(cg, "wineBottle")
  def nebuchadnezzar[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[UnitOfMeasurement[Volume, N], N => N]) = byName(cg, "nebuchadnezzar")

}
