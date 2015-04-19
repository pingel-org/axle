package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import axle.algebra.Vertex
import axle.syntax.directedgraph.directedGraphOps
import spire.algebra.Eq
import spire.algebra.MultiplicativeMonoid
import spire.implicits.StringOrder
import spire.implicits.eqOps

abstract class UnitConverterGraph[Q, N, DG[_, _]: DirectedGraph]()
  extends UnitConverter[Q, N] {

  private def conversions(
    vps: Seq[UnitOfMeasurement[Q]],
    ef: Seq[Vertex[UnitOfMeasurement[Q]]] => Seq[(Vertex[UnitOfMeasurement[Q]], Vertex[UnitOfMeasurement[Q]], N => N)])(
      implicit evDG: DirectedGraph[DG]): DG[UnitOfMeasurement[Q], N => N] =
    evDG.make[UnitOfMeasurement[Q], N => N](vps, ef)

  private def cgn(
    units: List[UnitOfMeasurement[Q]],
    links: Seq[(UnitOfMeasurement[Q], UnitOfMeasurement[Q], Bijection[N, N])]): CG[Q, DG, N] =
    conversions(
      units,
      (vs: Seq[Vertex[UnitOfMeasurement[Q]]]) => {
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

  private[this] def vertex(
    cg: DG[UnitOfMeasurement[Q], N => N],
    query: UnitOfMeasurement[Q])(implicit ev: Eq[N]): Vertex[UnitOfMeasurement[Q]] =
    directedGraphOps(cg).findVertex(_.payload.name === query.name).get

  val memo = collection.mutable.Map.empty[(UnitOfMeasurement[Q], UnitOfMeasurement[Q]), N => N]

  val combine = (f: N => N, g: N => N) => f andThen g

  def convert(orig: UnittedQuantity[Q, N], newUnit: UnitOfMeasurement[Q])(
    implicit ev: MultiplicativeMonoid[N], ev2: Eq[N]): UnittedQuantity[Q, N] = {

    val memoKey = (newUnit, orig.unit)
    if (memo.contains(memoKey)) {
      val convert = memo(memoKey)
      UnittedQuantity(convert(orig.magnitude), newUnit)
    } else {
      val pathOpt = directedGraphOps(conversionGraph).shortestPath(vertex(conversionGraph, newUnit), vertex(conversionGraph, orig.unit))
      if (pathOpt.isDefined) {
        val path = pathOpt.get.map(_.payload)
        val convert: N => N = path.reduceOption(combine).getOrElse(identity)
        memo += memoKey -> convert
        UnittedQuantity(convert(orig.magnitude), newUnit)
      } else {
        throw new Exception("no conversion path from " + orig.unit + " to " + newUnit)
      }
    }

  }
}