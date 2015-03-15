package axle.quanta

import axle.algebra.Bijection
import axle.algebra.Vertex
import axle.algebra.DirectedGraph
import spire.algebra.Field
import spire.algebra.Eq

abstract class QuantumMetadata[Q, N: Field: Eq, DG[_, _]: DirectedGraph]() {

  def units: List[UnitOfMeasurement[Q, N]]

  def links: Seq[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])]

  private def conversions[DG[_, _]](
    vps: Seq[UnitOfMeasurement[Q, N]],
    ef: Seq[Vertex[UnitOfMeasurement[Q, N]]] => Seq[(Vertex[UnitOfMeasurement[Q, N]], Vertex[UnitOfMeasurement[Q, N]], N => N)])(
      implicit evDG: DirectedGraph[DG]): DG[UnitOfMeasurement[Q, N], N => N] =
    evDG.make[UnitOfMeasurement[Q, N], N => N](vps, ef)

  private def cgn[DG[_, _]: DirectedGraph](
    units: List[UnitOfMeasurement[Q, N]],
    links: Seq[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])]): CG[Q, DG, N] =
    conversions[DG](
      units,
      (vs: Seq[Vertex[UnitOfMeasurement[Q, N]]]) => {
        val name2vertex = vs.map(v => (v.payload.name, v)).toMap
        links.flatMap({
          case (x, y, bijection) => {
            val xv = name2vertex(x.name)
            val yv = name2vertex(y.name)
            List((xv, yv, bijection.apply _), (yv, xv, bijection.unapply _))
          }
        })
      })

  val conversionGraph = cgn(units, links)

}
