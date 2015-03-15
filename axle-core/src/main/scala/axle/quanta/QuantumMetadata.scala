package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import axle.algebra.Vertex
import axle.syntax.directedgraph.directedGraphOps
import spire.algebra.Eq
import spire.algebra.MultiplicativeMonoid
import spire.implicits.StringOrder
import spire.implicits.eqOps
import spire.implicits.multiplicativeSemigroupOps

abstract class QuantumMetadata[Q, N, DG[_, _]: DirectedGraph]() {

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

  private[this] val conversionGraph = cgn(units, links)

  private[this] def vertex[DG[_, _]: DirectedGraph](
    cg: DG[UnitOfMeasurement[Q, N], N => N],
    query: UnitOfMeasurement[Q, N])(implicit ev: Eq[N]): Vertex[UnitOfMeasurement[Q, N]] =
    directedGraphOps(cg).findVertex(_.payload.name === query.name).get

  def convert(orig: UnittedQuantity[Q, N], newUnit: UnitOfMeasurement[Q, N])(implicit ev: MultiplicativeMonoid[N], ev2: Eq[N]): UnittedQuantity[Q, N] =
    directedGraphOps(conversionGraph).shortestPath(vertex(conversionGraph, newUnit), vertex(conversionGraph, orig.unit))
      .map(
        _.map(_.payload).foldLeft(ev.one)((n, convert) => convert(n)))
      .map(n => UnittedQuantity((orig.magnitude * n), newUnit))
      .getOrElse(throw new Exception("no conversion path from " + orig.unit + " to " + newUnit))

}
