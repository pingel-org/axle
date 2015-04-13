package axle.pgm.docalculus

import axle.algebra.DirectedGraph
import axle.algebra.Vertex
import axle.stats.Distribution
import spire.algebra.Eq
import spire.algebra.Field
import spire.implicits.BooleanStructure
import spire.implicits.StringOrder
import spire.implicits.eqOps
import axle.syntax.directedgraph._

case class CausalModelNode[T: Eq, N: Field](rv: Distribution[T, N], observable: Boolean = true)

object CausalModelNode {
  implicit def cmnEq[T: Eq, N: Field]: Eq[CausalModelNode[T, N]] = new Eq[CausalModelNode[T, N]] {
    def eqv(x: CausalModelNode[T, N], y: CausalModelNode[T, N]): Boolean =
      (x.rv === y.rv) && (x.observable === y.observable)
  }
}

trait PFunction[T, N] {

  def rv: Distribution[T, N]

  def inputs: Seq[Distribution[T, N]]
}

case class CausalModel[T: Eq, N: Field, DG[_, _]: DirectedGraph](
  val name: String, graph: DG[CausalModelNode[T, N], String]) {
  import graph._

  def duplicate: CausalModel[T, N, DG] = ???

  def randomVariables: Vector[Distribution[T, N]] =
    graph.vertices.map(_.payload.rv).toVector

  // TODO: this should probably be Option[Boolean] ?
  def observes(rv: Distribution[T, N]): Boolean =
    graph.findVertex(_.payload.rv === rv).map(_.payload.observable).getOrElse(false)

  def nodesFor(rvs: Set[Distribution[T, N]]): Set[Vertex[CausalModelNode[T, N]]] =
    rvs.flatMap(rv => graph.findVertex(_.payload.rv === rv))

  def nodeFor(rv: Distribution[T, N]): Option[Vertex[CausalModelNode[T, N]]] =
    graph.findVertex((n: Vertex[CausalModelNode[T, N]]) => n.payload.rv === rv)

  // def vertexPayloadToDistribution(cmn: CausalModelNode[T]): Distribution[T] = cmn.rv

  def addFunctions(pf: Seq[PFunction[T, N]]): CausalModel[T, N, DG] = ???

  def getVariable(name: String): Int = ??? // TODO

}

object CausalModel {

  def apply[T: Eq, N: Field, DG[_, _]: DirectedGraph](name: String, vps: Seq[CausalModelNode[T, N]]): CausalModel[T, N, DG] =
    CausalModel(name, DirectedGraph[DG].make[CausalModelNode[T, N], String](vps, vs => Nil))

}
