package axle.pgm

import axle.eqSet
import axle.algebra.UndirectedGraph
import axle.algebra.DirectedGraph
import axle.algebra.Vertex
import axle.stats.Distribution
import spire.algebra.Eq
import spire.algebra.Field
import spire.implicits.StringOrder

object JoinTree {

  def makeJoinTree[T: Eq: Manifest, N: Field: Manifest, UG[_, _]: UndirectedGraph](
    vps: Vector[Set[Distribution[T, N]]],
    ef: Seq[Vertex[Set[Distribution[T, N]]]] => Seq[(Vertex[Set[Distribution[T, N]]], Vertex[Set[Distribution[T, N]]], String)]): JoinTree[T, N, UG] =
    JoinTree[T, N, UG](implicitly[UndirectedGraph[UG]].make(vps, ef))

  // returns a jointree for DAG G with width equal to width(π, G)
  def fromEliminationOrder[T, N: Field, UG[_, _], UndirectedGraph, DG[_, _]: DirectedGraph](m: BayesianNetwork[T, N, DG], π: List[Distribution[T, N]]): JoinTree[T, N, UG] = {
    // val Gm = Gv.moralGraph()
    // val clusterSequence: List[Set[Distribution[_]]] = Gm.induceClusterSequence(pi)
    ???
  }
}

case class JoinTree[T: Eq, N: Field, UG[_, _]: UndirectedGraph](graph: UG[Set[Distribution[T, N]], String]) {

  //  def addToCluster(n: GV, v: Distribution[_]): Unit = n.getPayload += v
  //
  //  def constructEdge(n1: GV, n2: GV): JoinTree.G#E = g += ((n1, n2), "")
  //
  //  def separate(n1: GV, n2: GV): Set[Distribution[_]] = n1.getPayload.intersect(n2.getPayload)

  //  def toEliminationOrder(r: GV): List[Distribution[_]] = {
  //    val T: JoinTree = new JoinTree(graphFrom(getGraph())(v => v, e => e))
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
