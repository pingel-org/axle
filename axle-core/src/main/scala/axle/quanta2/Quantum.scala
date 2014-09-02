package axle.quanta2

import axle.graph.DirectedGraph
import spire.algebra.Field
import spire.algebra.Eq
import axle.graph.Vertex
import axle.graph.JungDirectedGraph
import spire.implicits._

trait Quantum {

  def wikipediaUrl: String

  type Q <: Quantum
  
  type CG[N] = DirectedGraph[UnitOfMeasurement[Q, N], N => N]

  def units[N: Field: Eq]: List[UnitOfMeasurement[Q, N]]

  def links[N: Field: Eq]: Seq[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], N => N, N => N)]
  
  private[quanta2] def conversions[Q <: Quantum, N: Field: Eq](vps: Seq[UnitOfMeasurement[Q, N]], ef: Seq[Vertex[UnitOfMeasurement[Q, N]]] => Seq[(Vertex[UnitOfMeasurement[Q, N]], Vertex[UnitOfMeasurement[Q, N]], N => N)]): DirectedGraph[UnitOfMeasurement[Q, N], N => N] =
    JungDirectedGraph(vps, ef)

  private[quanta2] def trip2fns[Q <: Quantum, N: Field: Eq](trip: (Vertex[UnitOfMeasurement[Q, N]], Vertex[UnitOfMeasurement[Q, N]], N)): Seq[(Vertex[UnitOfMeasurement[Q, N]], Vertex[UnitOfMeasurement[Q, N]], N => N)] = {
    val (from, to, multiplier) = trip
    Vector(
      (from, to, _ * multiplier),
      (to, from, _ / multiplier))
  }

  private[quanta2] def trips2fns[Q <: Quantum, N: Field: Eq](trips: Seq[(Vertex[UnitOfMeasurement[Q, N]], Vertex[UnitOfMeasurement[Q, N]], N)]) =
    trips.flatMap(trip2fns(_))

  private[quanta2] def byName[Q <: Quantum, N: Field: Eq](cg: DirectedGraph[UnitOfMeasurement[Q, N], N => N], unitName: String): UnitOfMeasurement[Q, N] =
    cg.findVertex(_.payload.name === unitName).get.payload
  
  def cgnDisconnected[N: Field: Eq]: CG[N] = conversions(units, (vs: Seq[Vertex[UnitOfMeasurement[Q, N]]]) => Nil)
  
  def cgn[N: Field: Eq]: CG[N] = conversions(
    units,
    (vs: Seq[Vertex[UnitOfMeasurement[Q, N]]]) => {
      val name2vertex = vs.map(v => (v.payload.name, v)).toMap
      links[N].flatMap({
        case (x, y, forward, backward) => {
          val xv = name2vertex(x.name)
          val yv = name2vertex(y.name)
          List((xv, yv, forward), (yv, xv, backward))
        }
      })
    })
  
}
