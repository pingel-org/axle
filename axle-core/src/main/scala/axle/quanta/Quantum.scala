package axle.quanta

/**
 * Quantum
 *
 * Used in the sense of the World English Dictionary's 4th definition:
 *
 * 4. something that can be quantified or measured
 *
 * [[http://dictionary.reference.com/browse/quantum]]
 *
 */

import spire.algebra.Field
import spire.algebra.Eq
import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import axle.algebra.Vertex
import spire.implicits._
import axle.syntax.directedgraph._

trait Quantum3

object Quantum3 {

  type CG[Q <: Quantum3, DG[_, _], N] = DG[UnitOfMeasurement3[Q, N], N => N]

//  private[quanta] def trip2fns[N: Field: Eq](trip: (Vertex[UnitOfMeasurement[Q, N]], Vertex[UnitOfMeasurement[Q, N]], N)): Seq[(Vertex[UnitOfMeasurement[Q, N]], Vertex[UnitOfMeasurement[Q, N]], N => N)] = {
//    val (from, to, multiplier) = trip
//    Vector(
//      (from, to, _ * multiplier),
//      (to, from, _ / multiplier))
//  }
//  
//  private[quanta] def trips2fns[N: Field: Eq](trips: Seq[(Vertex[UnitOfMeasurement[Q, N]], Vertex[UnitOfMeasurement[Q, N]], N)]) =
//    trips.flatMap(trip2fns(_))
//
//  def cgnDisconnected[N: Field: Eq, DG[_, _]: DirectedGraph]: CG[DG, N] = conversions(units, (vs: Seq[Vertex[UnitOfMeasurement[Q, N]]]) => Nil)
  
  private def conversions[Q <: Quantum3, N: Field: Eq, DG[_, _]](
    vps: Seq[UnitOfMeasurement3[Q, N]],
    ef: Seq[Vertex[UnitOfMeasurement3[Q, N]]] => Seq[(Vertex[UnitOfMeasurement3[Q, N]], Vertex[UnitOfMeasurement3[Q, N]], N => N)])(implicit evDG: DirectedGraph[DG]): DG[UnitOfMeasurement3[Q, N], N => N] =
    evDG.make[UnitOfMeasurement3[Q, N], N => N](vps, ef)

  private[quanta] def cgn[Q <: Quantum3, N: Field: Eq, DG[_, _]: DirectedGraph](
    units: List[UnitOfMeasurement3[Q, N]],
    links: Seq[(UnitOfMeasurement3[Q, N], UnitOfMeasurement3[Q, N], Bijection[N, N])]): CG[Q, DG, N] =
    conversions[Q, N, DG](
      units,
      (vs: Seq[Vertex[UnitOfMeasurement3[Q, N]]]) => {
        val name2vertex = vs.map(v => (v.payload.name, v)).toMap
        links.flatMap({
          case (x, y, bijection) => {
            val xv = name2vertex(x.name)
            val yv = name2vertex(y.name)
            List((xv, yv, bijection.apply _), (yv, xv, bijection.unapply _))
          }
        })
      })

      
}
