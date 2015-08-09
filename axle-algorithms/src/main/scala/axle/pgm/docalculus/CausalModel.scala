package axle.pgm.docalculus

import axle.algebra.DirectedGraph
import axle.stats.Distribution
import spire.algebra.Eq
import spire.algebra.Field
import spire.implicits.BooleanStructure
import spire.implicits.StringOrder
import spire.implicits.eqOps
import axle.syntax.directedgraph._

case class CausalModelNode[T: Eq, N: Field](rv: Distribution[T, N], observable: Boolean = true)

class CausalModelEdge

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

case class CausalModel[T: Eq, N: Field, DG](
    val name: String, graph: DG)(
        implicit dg: DirectedGraph[DG, CausalModelNode[T, N], CausalModelEdge]) {

  import graph._

  def duplicate: CausalModel[T, N, DG] = ???

  def randomVariables: Vector[Distribution[T, N]] =
    graph.vertices.map(_.rv).toVector

  // TODO: this should probably be Option[Boolean] ?
  def observes(rv: Distribution[T, N]): Boolean =
    graph.findVertex(_.rv === rv).map(_.observable).getOrElse(false)

  def nodesFor(rvs: Set[Distribution[T, N]]): Set[CausalModelNode[T, N]] =
    rvs.flatMap(rv => graph.findVertex(_.rv === rv))

  def nodeFor(rv: Distribution[T, N]): Option[CausalModelNode[T, N]] =
    graph.findVertex((n: CausalModelNode[T, N]) => n.rv === rv)

  // def vertexPayloadToDistribution(cmn: CausalModelNode[T]): Distribution[T] = cmn.rv

  def addFunctions(pf: Seq[PFunction[T, N]]): CausalModel[T, N, DG] = ???

  def getVariable(name: String): Int = ??? // TODO

}

object CausalModel {

  def apply[T: Eq, N: Field, DG](
    name: String,
    vps: Seq[CausalModelNode[T, N]])(
      implicit dg: DirectedGraph[DG, CausalModelNode[T, N], CausalModelEdge]): CausalModel[T, N, DG] =
    CausalModel(name, dg.make(vps, Nil))

}
