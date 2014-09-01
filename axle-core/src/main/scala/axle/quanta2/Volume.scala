package axle.quanta2

import axle.graph.DirectedGraph
import axle.graph.Vertex
import spire.algebra.Eq
import spire.algebra.Field
import spire.implicits.DoubleAlgebra
import spire.math.Rational
import spire.math.Real

class Volume extends Quantum {
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(time)"
}

object Volume extends Volume {

  def cgn[N: Field: Eq]: DirectedGraph[UnitOfMeasurement[Volume, N], N => N] = conversions(
    List(
      unit("greatLakes", "greatLakes"),
      unit("wineBottle", "wineBottle"),
      unit("nebuchadnezzar", "nebuchadnezzar")), // 5 bottles of wine
    (vs: Seq[Vertex[UnitOfMeasurement[Volume, N]]]) => vs match {
      case greatLakes :: wineBottle :: nebuchadnezzar :: Nil => List(
          // TODO
          )
      case _ => Nil
    })

  implicit val cgVolumeRational: DirectedGraph[UnitOfMeasurement[Volume, Rational], Rational => Rational] = cgn[Rational]
  implicit val cgVolumeReal: DirectedGraph[UnitOfMeasurement[Volume, Real], Real => Real] = cgn[Real]
  implicit val cgVolumeDouble: DirectedGraph[UnitOfMeasurement[Volume, Double], Double => Double] = cgn[Double]

  implicit val mtRational = modulize[Volume, Rational]
  implicit val mtReal = modulize[Volume, Real]
  implicit val mtDouble = modulize[Volume, Double]

  def greatLakes[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[UnitOfMeasurement[Volume, N], N => N]) = byName(cg, "greatLakes")
  def wineBottle[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[UnitOfMeasurement[Volume, N], N => N]) = byName(cg, "wineBottle")
  def nebuchadnezzar[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[UnitOfMeasurement[Volume, N], N => N]) = byName(cg, "nebuchadnezzar")

}
