package axle.pgm

import axle._
import axle.graph._
import axle.stats._
import spire.implicits._
import spire.algebra._
import axle.pgm._

import BayesianNetworkModule._

object JoinTree {
  
  def apply[T: Eq: Manifest](
    vps: Vector[Set[RandomVariable[T]]],
    ef: Seq[Vertex[Set[RandomVariable[T]]]] => Seq[(Vertex[Set[RandomVariable[T]]], Vertex[Set[RandomVariable[T]]], String)]): JoinTree[T] =
    JoinTree[T](JungUndirectedGraph(vps, ef))

  // returns a jointree for DAG G with width equal to width(π, G)
  def fromEliminationOrder[T](m: BayesianNetwork[T], π: List[RandomVariable[T]]): JoinTree[T] = {
    // val Gm = Gv.moralGraph()
    // val clusterSequence: List[Set[RandomVariable[_]]] = Gm.induceClusterSequence(pi)
    ???
  }

}

case class JoinTree[T: Eq](graph: UndirectedGraph[Set[RandomVariable[T]], String]) {

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
