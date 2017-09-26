package axle.pgm

import axle.algebra.UndirectedGraph
import axle.stats.Variable
import axle.stats.Factor
import cats.kernel.Eq
import spire.algebra.Field
import cats.implicits._
import axle.syntax.undirectedgraph._

class EliminationTreeEdge

case class EliminationTree[T: Eq: Manifest, N: Field: Manifest, UG](
    vps: Seq[Factor[T, N]],
    ef: Seq[(Factor[T, N], Factor[T, N])])(
        implicit ug: UndirectedGraph[UG, Factor[T, N], EliminationTreeEdge]) {

  lazy val graph = ug.make(vps, ef.map({ case (v1, v2) => (v1, v2, new EliminationTreeEdge) }))

  def gatherVars(
    stop: Factor[T, N],
    node: Factor[T, N],
    accumulated: Set[Variable[T]]): Set[Variable[T]] =
    graph
      .neighbors(node)
      .filter(n => !(n === stop))
      .foldLeft(accumulated ++ node.variables)((a, y) => gatherVars(node, y, a))

  def cluster(i: Factor[T, N]): Set[Variable[T]] =
    graph.neighbors(i).flatMap(separate(i, _)).toSet ++ i.variables

  def separate(i: Factor[T, N], j: Factor[T, N]): Set[Variable[T]] =
    gatherVars(j, i, Set.empty[Variable[T]]).intersect(gatherVars(i, j, Set[Variable[T]]()))

  // def constructEdge(v1: GV, v2: GV): GE = g += ((v1, v2), "")
  // def delete(node: GV): Unit = g.delete(node)

  def allVariables: Set[Variable[T]] =
    graph.vertices.flatMap(_.variables).toSet

  // Note: previous version also handled case where 'node' wasn't in the graph
  // def addFactor(node: GV, f: Factor): Unit = node.setPayload(node.getPayload.multiply(f))

  // def factor(node: GV): Factor = node.getPayload
  // def update(node: GV, f: Factor): Unit = node.setPayload(f)

}
