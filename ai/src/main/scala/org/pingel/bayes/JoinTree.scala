package org.pingel.bayes;

import scala.collection._
import axle.graph.JungDirectedGraphFactory
import axle.graph.JungUndirectedGraphFactory

object JoinTree {

  // TODO:
  def fromEliminationOrder(m: Model, pi: List[RandomVariable]): JoinTree = {
    val G = m.g // Note: G used ot be passed in as DirectedGraph[_, _]
    // returns a jointree for DAG G with width equal to width(pi, G)
    val T = new JoinTree()
    val Gm = G.moralGraph() // UndirectedGraph
    var clusterSequence: List[Set[RandomVariable]] = null // Gm.induceClusterSequence(pi);
    T
  }

}

class JoinTree {

  val g = JungUndirectedGraphFactory.graph[mutable.Set[RandomVariable], String]()

  type GV = g.type#V
  type GE = g.type#E

  def setCluster(n: GV, cluster: Set[RandomVariable]): Unit = n.setPayload(cluster)

  def addToCluster(n: GV, v: RandomVariable): Unit = n.getPayload += v

  def constructEdge(n1: GV, n2: GV): GE = g += ((n1, n2), "")

  def separate(n1: GV, n2: GV): Set[RandomVariable] = n1.getPayload.intersect(n2.getPayload)

  def toEliminationOrder(r: GV): List[RandomVariable] = {
    var result = new mutable.ListBuffer[RandomVariable]()
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
