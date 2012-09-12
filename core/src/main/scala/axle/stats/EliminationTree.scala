package axle.stats

import collection._
import axle.graph.JungUndirectedGraphFactory._

class EliminationTree extends JungUndirectedGraph[Factor, String] {

  def gatherVars(stop: V, node: V, result: mutable.Set[RandomVariable[_]]): Unit = {
    result ++= node.payload.variables
    neighbors(node).filter(!_.equals(stop)).map(gatherVars(node, _, result))
  }

  def cluster(i: V): Set[RandomVariable[_]] = {
    val result = mutable.Set[RandomVariable[_]]()
    neighbors(i).map(j => result ++= separate(i, j))
    result ++= i.payload.variables
    result
  }

  def separate(i: V, j: V): Set[RandomVariable[_]] = {
    val iSide = mutable.Set[RandomVariable[_]]()
    gatherVars(j, i, iSide)
    val jSide = mutable.Set[RandomVariable[_]]()
    gatherVars(i, j, jSide)
    iSide.intersect(jSide)
  }

  // def constructEdge(v1: GV, v2: GV): GE = g += ((v1, v2), "")
  // def delete(node: GV): Unit = g.delete(node)

  def allVariables(): Set[RandomVariable[_]] = vertices.flatMap(_.payload.variables)

  // Note: previous version also handled case where 'node' wasn't in the graph
  // def addFactor(node: GV, f: Factor): Unit = node.setPayload(node.getPayload.multiply(f))

  // def factor(node: GV): Factor = node.getPayload
  // def update(node: GV, f: Factor): Unit = node.setPayload(f)

}
