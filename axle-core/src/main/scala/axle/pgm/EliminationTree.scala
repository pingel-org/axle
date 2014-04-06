package axle.pgm

import axle.stats._
import axle.graph._
import spire.algebra._
import spire.implicits._

object EliminationTreeModule extends EliminationTreeModule

trait EliminationTreeModule {

  import FactorModule._

  case class EliminationTree[T: Eq: Manifest, N: Field: Manifest](
    vps: Seq[Factor[T, N]],
    ef: Seq[Vertex[Factor[T, N]]] => Seq[(Vertex[Factor[T, N]], Vertex[Factor[T, N]], String)]) {

    lazy val graph = JungUndirectedGraph(vps, ef)

    def gatherVars(
      stop: Vertex[Factor[T, N]],
      node: Vertex[Factor[T, N]],
      accumulated: Set[RandomVariable[T, N]]): Set[RandomVariable[T, N]] =
      graph
        .neighbors(node)
        .filter(n => !(n === stop))
        .foldLeft(accumulated ++ node.payload.variables)((a, y) => gatherVars(node, y, a))

    def cluster(i: Vertex[Factor[T, N]]): Set[RandomVariable[T, N]] =
      graph.neighbors(i).flatMap(separate(i, _)) ++ i.payload.variables

    def separate(i: Vertex[Factor[T, N]], j: Vertex[Factor[T, N]]): Set[RandomVariable[T, N]] =
      gatherVars(j, i, Set.empty[RandomVariable[T, N]]).intersect(gatherVars(i, j, Set[RandomVariable[T, N]]()))

    // def constructEdge(v1: GV, v2: GV): GE = g += ((v1, v2), "")
    // def delete(node: GV): Unit = g.delete(node)

    def allVariables: Set[RandomVariable[T, N]] =
      graph.vertices.flatMap(_.payload.variables)

    // Note: previous version also handled case where 'node' wasn't in the graph
    // def addFactor(node: GV, f: Factor): Unit = node.setPayload(node.getPayload.multiply(f))

    // def factor(node: GV): Factor = node.getPayload
    // def update(node: GV, f: Factor): Unit = node.setPayload(f)

  }

}
