package axle.pgm

import axle.algebra.UndirectedGraph
import axle.stats.Distribution
import axle.stats.Factor
import spire.algebra.Eq
import spire.algebra.Field
import spire.implicits.StringOrder
import spire.implicits.eqOps
import axle.syntax.undirectedgraph._

case class EliminationTree[T: Eq: Manifest, N: Field: Manifest, UG[_, _]: UndirectedGraph](
  vps: Seq[Factor[T, N]],
  ef: Seq[Factor[T, N]] => Seq[(Factor[T, N], Factor[T, N], String)]) {

  lazy val graph = undirectedGraph(vps, ef)

  def gatherVars(
    stop: Factor[T, N],
    node: Factor[T, N],
    accumulated: Set[Distribution[T, N]]): Set[Distribution[T, N]] =
    graph
      .neighbors(node)
      .filter(n => !(n === stop))
      .foldLeft(accumulated ++ node.variables)((a, y) => gatherVars(node, y, a))

  def cluster(i: Factor[T, N]): Set[Distribution[T, N]] =
    graph.neighbors(i).flatMap(separate(i, _)) ++ i.variables

  def separate(i: Factor[T, N], j: Factor[T, N]): Set[Distribution[T, N]] =
    gatherVars(j, i, Set.empty[Distribution[T, N]]).intersect(gatherVars(i, j, Set[Distribution[T, N]]()))

  // def constructEdge(v1: GV, v2: GV): GE = g += ((v1, v2), "")
  // def delete(node: GV): Unit = g.delete(node)

  def allVariables: Set[Distribution[T, N]] =
    graph.vertices.flatMap(_.variables).toSet

  // Note: previous version also handled case where 'node' wasn't in the graph
  // def addFactor(node: GV, f: Factor): Unit = node.setPayload(node.getPayload.multiply(f))

  // def factor(node: GV): Factor = node.getPayload
  // def update(node: GV, f: Factor): Unit = node.setPayload(f)

}
