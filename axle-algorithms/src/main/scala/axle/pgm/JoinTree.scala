package axle.pgm

import axle.eqSet
import axle.jung.JungUndirectedGraph
import axle.graph.UndirectedGraph
import axle.graph.Vertex
import axle.stats.Distribution
import spire.algebra.Eq
import spire.algebra.Field
import spire.implicits.StringOrder

trait JoinTreeModule extends BayesianNetworkModule {

  def makeJoinTree[T: Eq: Manifest, N: Field: Manifest](
    vps: Vector[Set[Distribution[T, N]]],
    ef: Seq[Vertex[Set[Distribution[T, N]]]] => Seq[(Vertex[Set[Distribution[T, N]]], Vertex[Set[Distribution[T, N]]], String)]): JoinTree[T, N] =
    JoinTree[T, N](JungUndirectedGraph(vps, ef))

  // returns a jointree for DAG G with width equal to width(π, G)
  def fromEliminationOrder[T, N: Field](m: BayesianNetwork[T, N], π: List[Distribution[T, N]]): JoinTree[T, N] = {
    // val Gm = Gv.moralGraph()
    // val clusterSequence: List[Set[Distribution[_]]] = Gm.induceClusterSequence(pi)
    ???
  }

  case class JoinTree[T: Eq, N: Field](graph: UndirectedGraph[Set[Distribution[T, N]], String]) {

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

}
