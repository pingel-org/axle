package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import axle.syntax.directedgraph._
import spire.algebra.Eq
import spire.algebra.MultiplicativeMonoid
import spire.implicits.StringOrder
import spire.implicits.eqOps

abstract class UnitConverterGraph[Q, N, DG]()(
  implicit evDG: DirectedGraph[DG, UnitOfMeasurement[Q], N => N])
    extends UnitConverter[Q, N] {

  private def conversions(
    vps: Seq[UnitOfMeasurement[Q]],
    ef: Seq[(UnitOfMeasurement[Q], UnitOfMeasurement[Q], N => N)]): DG =
    evDG.make(vps, ef)

  private def cgn(
    units: List[UnitOfMeasurement[Q]],
    links: Seq[(UnitOfMeasurement[Q], UnitOfMeasurement[Q], Bijection[N, N])]): DG =
    conversions(
      units,
      {
        val name2uom = units.map(u => (u.name, u)).toMap
        links.flatMap({
          case (x, y, bijection) => {
            val xv = name2uom(x.name)
            val yv = name2uom(y.name)
            List((xv, yv, bijection.apply _), (yv, xv, bijection.unapply _))
          }
        })
      })

  val conversionGraph = cgn(units, links)

  private[this] def vertex(
    cg: DG,
    query: UnitOfMeasurement[Q]): UnitOfMeasurement[Q] =
    evDG.findVertex(cg, _.name === query.name).get

  val memo = collection.mutable.Map.empty[(UnitOfMeasurement[Q], UnitOfMeasurement[Q]), N => N]

  val combine = (f: N => N, g: N => N) => f andThen g

  def convert(orig: UnittedQuantity[Q, N], newUnit: UnitOfMeasurement[Q])(
    implicit ev: MultiplicativeMonoid[N]): UnittedQuantity[Q, N] = {

    val memoKey = (newUnit, orig.unit)
    val convert: N => N =
      if (memo.contains(memoKey)) {
        memo(memoKey)
      } else {
        val pathOpt = evDG.shortestPath(conversionGraph, vertex(conversionGraph, newUnit), vertex(conversionGraph, orig.unit))
        if (pathOpt.isDefined) {
          val path = pathOpt.get
          val convert: N => N = path.reduceOption(combine).getOrElse(identity)
          memo += memoKey -> convert
          convert
        } else {
          throw new Exception("no conversion path from " + orig.unit + " to " + newUnit)
        }
      }
    UnittedQuantity(convert(orig.magnitude), newUnit)
  }
}