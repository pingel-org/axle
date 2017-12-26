package axle.pgm

import axle.algebra.UndirectedGraph
import axle.stats.Variable
import cats.kernel.Eq
import spire.algebra.Field

class JoinTreeEdge

object JoinTree {

  def makeJoinTree[T: Eq: Manifest, N: Field: Manifest, UG](
    vps: Vector[Set[Variable[T]]],
    ef:  Seq[(Set[Variable[T]], Set[Variable[T]])])(
    implicit
    ug: UndirectedGraph[UG, Set[Variable[T]], JoinTreeEdge]): JoinTree[T, N, UG] =
    JoinTree[T, N, UG](ug.make(vps, ef.map({ case (v1, v2) => (v1, v2, new JoinTreeEdge) })))

  // returns a jointree for DAG G with width equal to width(π, G)
  def fromEliminationOrder[T, N: Field, UG, DG](m: BayesianNetwork[T, N, DG], π: List[Variable[T]]): JoinTree[T, N, UG] = {
    // val Gm = Gv.moralGraph
    // val clusterSequence: List[Set[Distribution[_]]] = Gm.induceClusterSequence(pi)
    ???
  }
}

case class JoinTree[T: Eq, N: Field, UG](
  graph: UG) // (implicit ug: UndirectedGraph[UG, Set[Variable[T]], JoinTreeEdge])
  {

  //  def addToCluster(n: GV, v: Distribution[_]): Unit = n.getPayload += v
  //
  //  def constructEdge(n1: GV, n2: GV): JoinTree.G#E = g += ((n1, n2), "")
  //
  //  def separate(n1: GV, n2: GV): Set[Distribution[_]] = n1.getPayload.intersect(n2.getPayload)

  //  def toEliminationOrder(r: GV): List[Distribution[_]] = {
  //    val T: JoinTree = JoinTree(graphFrom(getGraph())(v => v, e => e))
  //    while (T.getGraph.size > 1) {
  //      val i = T.getGraph.firstLeafOtherThan(r)
  //      val j = theNeighbor() a JoinTreeNode
  //      result ++= i.getPayload - j.getPayload
  //    }
  //    result ++= r.getPayload
  //  }

  //  def embeds(eTree: EliminationTree, embedding: Map[JoinTree.G#V, EliminationTree#GV]): Boolean =
  //    g.getVertices().forall(jtn =>
  //      eTree.getFactor(embedding(jtn)).getVariables.forall(ev => jtn.getPayload.contains(ev)))

}
