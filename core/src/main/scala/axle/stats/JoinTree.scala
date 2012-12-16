package axle.stats

import collection._
import axle.graph._

object JoinTree {

  import axle.stats.Model._

  // [immutable.Set[RandomVariable[_]], String]
  def apply(
    vps: Seq[immutable.Set[RandomVariable[_]]],
    ef: Seq[UndirectedGraphVertex[immutable.Set[RandomVariable[_]]]] => Seq[(UndirectedGraphVertex[immutable.Set[RandomVariable[_]]], UndirectedGraphVertex[immutable.Set[RandomVariable[_]]], String)]): JoinTree =
    new JoinTree(JungUndirectedGraph(vps, ef))

  // returns a jointree for DAG G with width equal to width(π, G)
  def fromEliminationOrder[MVP](m: Model[MVP], π: List[RandomVariable[_]]): JoinTree = {
    // val Gm = Gv.moralGraph()
    // val clusterSequence: List[Set[RandomVariable[_]]] = null // Gm.induceClusterSequence(pi)
    null.asInstanceOf[JoinTree] // TODO
  }

}

case class JoinTree(graph: UndirectedGraph[immutable.Set[RandomVariable[_]], String]) {

  //  def setCluster(n: GV, cluster: mutable.Set[RandomVariable[_]]): Unit = n.setPayload(cluster)
  //
  //  def addToCluster(n: GV, v: RandomVariable[_]): Unit = n.getPayload += v
  //
  //  def constructEdge(n1: GV, n2: GV): JoinTree.G#E = g += ((n1, n2), "")
  //
  //  def separate(n1: GV, n2: GV): Set[RandomVariable[_]] = n1.getPayload.intersect(n2.getPayload)

  //  def toEliminationOrder(r: GV): List[RandomVariable[_]] = {
  //    val result = new mutable.ListBuffer[RandomVariable[_]]()
  //    val T: JoinTree = new JoinTree(graphFrom(getGraph())(v => v, e => e))
  //    while (T.getGraph.size > 1) {
  //      val i = T.getGraph.firstLeafOtherThan(r)
  //      val j = null // TODO theNeighbor() a JoinTreeNode
  //      result ++= i.getPayload - j.getPayload
  //    }
  //    result ++= r.getPayload
  //    result.toList
  //  }

  //  def embeds(eTree: EliminationTree, embedding: Map[JoinTree.G#V, EliminationTree#GV]): Boolean =
  //    g.getVertices().forall(jtn =>
  //      eTree.getFactor(embedding(jtn)).getVariables.forall(ev => jtn.getPayload.contains(ev)))

}
