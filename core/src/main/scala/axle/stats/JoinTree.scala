package axle.stats

import collection._
import axle.graph._
import axle._

object JoinTree {

  import axle.stats.Model._

  def apply(
    vps: Seq[immutable.Set[RandomVariable[_]]],
    ef: Seq[Vertex[immutable.Set[RandomVariable[_]]]] => Seq[(Vertex[immutable.Set[RandomVariable[_]]], Vertex[immutable.Set[RandomVariable[_]]], String)]): JoinTree =
    new JoinTree(JungUndirectedGraph(vps, ef))

  // returns a jointree for DAG G with width equal to width(π, G)
  def fromEliminationOrder[MVP](m: Model[MVP], π: List[RandomVariable[_]]): JoinTree = {
    // val Gm = Gv.moralGraph()
    // val clusterSequence: List[Set[RandomVariable[_]]] = Gm.induceClusterSequence(pi)
    ???
  }

}

case class JoinTree(graph: UndirectedGraph[immutable.Set[RandomVariable[_]], String]) {

  //  def addToCluster(n: GV, v: RandomVariable[_]): Unit = n.getPayload += v
  //
  //  def constructEdge(n1: GV, n2: GV): JoinTree.G#E = g += ((n1, n2), "")
  //
  //  def separate(n1: GV, n2: GV): Set[RandomVariable[_]] = n1.getPayload.intersect(n2.getPayload)

  //  def toEliminationOrder(r: GV): List[RandomVariable[_]] = {
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
