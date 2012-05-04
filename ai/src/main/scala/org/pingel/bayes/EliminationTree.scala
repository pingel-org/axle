package org.pingel.bayes

import scala.collection._
import axle.graph.JungUndirectedGraphFactory._

class EliminationTree {

  val g = graph[Factor, String]()

  type GV = g.type#V
  type GE = g.type#E

  def gatherVars(stop: GV, node: GV, result: mutable.Set[RandomVariable]): Unit = {
    result ++= node.getPayload.getVariables
    g.getNeighbors(node).filter(!_.equals(stop)).map(gatherVars(node, _, result))
  }

  def cluster(i: GV): Set[RandomVariable] = {
    val result = mutable.Set[RandomVariable]()
    g.getNeighbors(i).map(j => result ++= separate(i, j))
    result ++= i.getPayload.getVariables
    result
  }

  def separate(i: GV, j: GV): Set[RandomVariable] = {
    val iSide = mutable.Set[RandomVariable]()
    gatherVars(j, i, iSide)
    val jSide = mutable.Set[RandomVariable]()
    gatherVars(i, j, jSide)
    iSide.intersect(jSide)
  }

  def constructEdge(v1: GV, v2: GV): GE = g += ((v1, v2), "")

  def delete(node: GV): Unit = g.delete(node)

  def getAllVariables(): Set[RandomVariable] = g.getVertices.flatMap(_.getPayload.getVariables)

  // Note: previous version also handled case where 'node' wasn't in the graph
  def addFactor(node: GV, f: Factor): Unit = node.setPayload(node.getPayload.multiply(f))

  def getFactor(node: GV): Factor = node.getPayload

  def setFactor(node: GV, f: Factor): Unit = node.setPayload(f)

}
