package axle.stats

import collection._
import axle.stats._
import axle.graph._

trait EliminationTreeModule extends FactorModule {
  
  case class EliminationTree(
    vps: Seq[Factor],
    ef: Seq[Vertex[Factor]] => Seq[(Vertex[Factor], Vertex[Factor], String)]) {

    lazy val graph = JungUndirectedGraph(vps, ef)

    def gatherVars(
      stop: Vertex[Factor],
      node: Vertex[Factor],
      accumulated: Set[RandomVariable[_]]): Set[RandomVariable[_]] =
      graph
        .neighbors(node)
        .filter(!_.equals(stop))
        .foldLeft(accumulated ++ node.payload.variables)((a, y) => gatherVars(node, y, a))

    def cluster(i: Vertex[Factor]): Set[RandomVariable[_]] =
      graph.neighbors(i).flatMap(separate(i, _)) ++ i.payload.variables

    def separate(i: Vertex[Factor], j: Vertex[Factor]): Set[RandomVariable[_]] =
      gatherVars(j, i, Set[RandomVariable[_]]()).intersect(gatherVars(i, j, Set[RandomVariable[_]]()))

    // def constructEdge(v1: GV, v2: GV): GE = g += ((v1, v2), "")
    // def delete(node: GV): Unit = g.delete(node)

    def allVariables(): Set[RandomVariable[_]] = graph.vertices.flatMap(_.payload.variables)

    // Note: previous version also handled case where 'node' wasn't in the graph
    // def addFactor(node: GV, f: Factor): Unit = node.setPayload(node.getPayload.multiply(f))

    // def factor(node: GV): Factor = node.getPayload
    // def update(node: GV, f: Factor): Unit = node.setPayload(f)

  }

}