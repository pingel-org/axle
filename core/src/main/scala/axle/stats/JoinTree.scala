package axle.stats

import collection._
import axle.graph.JungDirectedGraphFactory
import axle.graph.JungUndirectedGraphFactory

object JoinTree {

  // TODO:
  def fromEliminationOrder(m: Model, pi: List[RandomVariable[_]]): JoinTree = {
    val G = m.getGraph // Note: G used ot be passed in as DirectedGraph[_, _]
    // returns a jointree for DAG G with width equal to width(pi, G)
    val T = new JoinTree()
    val Gm = G.moralGraph()
    val clusterSequence: List[Set[RandomVariable[_]]] = null // Gm.induceClusterSequence(pi)
    T
  }

}

class JoinTree {

  val g = JungUndirectedGraphFactory.graph[mutable.Set[RandomVariable[_]], String]()

  type GV = g.type#V
  type GE = g.type#E

  def setCluster(n: GV, cluster: Set[RandomVariable[_]]): Unit = n.setPayload(cluster)

  def addToCluster(n: GV, v: RandomVariable[_]): Unit = n.getPayload += v

  def constructEdge(n1: GV, n2: GV): GE = g += ((n1, n2), "")

  def separate(n1: GV, n2: GV): Set[RandomVariable[_]] = n1.getPayload.intersect(n2.getPayload)

  def toEliminationOrder(r: GV): List[RandomVariable[_]] = {
    val result = new mutable.ListBuffer[RandomVariable[_]]()
    val T: JoinTree = this.duplicate()
    while (T.g.getVertices().size > 1) {
      val i = T.g.firstLeafOtherThan(r)
      val j = null // TODO theNeighbor(); a JoinTreeNode
      result ++= i.getPayload - j.getPayload
    }
    result ++= r.getPayload
    result.toList
  }

  def embeds(eTree: EliminationTree, embedding: Map[GV, EliminationTree#GV]): Boolean =
    g.getVertices().forall(jtn =>
      eTree.getFactor(embedding(jtn)).getVariables.forall(ev => jtn.getPayload.contains(ev)))

}
