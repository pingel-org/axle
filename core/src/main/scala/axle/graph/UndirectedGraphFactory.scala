package axle.graph

import collection._
import axle._

trait UndirectedGraphFactory extends GraphFactory {

  // def apply[A, B](): GenUndirectedGraph[A, B]

  def apply[A, B](vps: Seq[A], ef: Seq[UndirectedGraphVertex[A]] => Seq[(UndirectedGraphVertex[A], UndirectedGraphVertex[A], B)]): GenUndirectedGraph[A, B]

}

trait UndirectedGraphVertex[VP] extends GraphVertex[VP]

trait UndirectedGraphEdge[VP, EP] extends GraphEdge[VP, EP] {

  def vertices(): (UndirectedGraphVertex[VP], UndirectedGraphVertex[VP])

  def other(u: UndirectedGraphVertex[VP]): UndirectedGraphVertex[VP] = {
    val (v1, v2) = vertices()
    u match {
      case _ if u.equals(v1) => v2
      case _ if u.equals(v2) => v1
      case _ => throw new Exception("can't find 'other' of a vertex that isn't on the edge itself")
    }
  }

  def connects(a1: UndirectedGraphVertex[VP], a2: UndirectedGraphVertex[VP]) = {
    val (v1, v2) = vertices()
    (v1 == a1 && v2 == a2) || (v2 == a1 && v1 == a2)
  }
}

trait GenUndirectedGraph[VP, EP] extends GenGraph[VP, EP] {

  //  type V <: UndirectedGraphVertex[VP]
  //  type E <: UndirectedGraphEdge[VP, EP]

  //  def vertex(payload: VP): V
  //  def edge(v1: V, v2: V, payload: EP): E

  def unlink(e: UndirectedGraphEdge[VP, EP]): GenUndirectedGraph[VP, EP]
  def unlink(v1: UndirectedGraphVertex[VP], v2: UndirectedGraphVertex[VP]): GenUndirectedGraph[VP, EP]
  def areNeighbors(v1: UndirectedGraphVertex[VP], v2: UndirectedGraphVertex[VP]): Boolean

  def isClique(vs: GenTraversable[UndirectedGraphVertex[VP]]): Boolean = (for {
    vi <- vs
    vj <- vs
  } yield {
    (vi == vj) || areNeighbors(vi, vj)
  }).forall(b => b)

  def numEdgesToForceClique(vs: GenTraversable[UndirectedGraphVertex[VP]], payload: (UndirectedGraphVertex[VP], UndirectedGraphVertex[VP]) => EP) = (for {
    vi <- vs
    vj <- vs
  } yield {
    if (areNeighbors(vi, vj)) 1 else 0
  }).sum

  def forceClique(vs: Set[UndirectedGraphVertex[VP]], payload: (UndirectedGraphVertex[VP], UndirectedGraphVertex[VP]) => EP): GenUndirectedGraph[VP, EP]

  // assert: among is a subset of vertices
  def vertexWithFewestEdgesToEliminateAmong(among: Set[UndirectedGraphVertex[VP]], payload: (UndirectedGraphVertex[VP], UndirectedGraphVertex[VP]) => EP): UndirectedGraphVertex[VP] =
    among.map(v => (v, numEdgesToForceClique(neighbors(v), payload))).minBy(_._2)._1

  // assert: among is a subset of vertices
  def vertexWithFewestNeighborsAmong(among: Set[UndirectedGraphVertex[VP]]): UndirectedGraphVertex[VP] =
    among.map(v => (v, neighbors(v).size)).minBy(_._2)._1

  def degree(v: UndirectedGraphVertex[VP]): Int

  def edges(v: UndirectedGraphVertex[VP]): Set[UndirectedGraphEdge[VP, EP]]

  def neighbors(v: UndirectedGraphVertex[VP]): Set[UndirectedGraphVertex[VP]]

  def delete(v: UndirectedGraphVertex[VP]): GenUndirectedGraph[VP, EP]

  // a "leaf" is vertex with only one neighbor
  def firstLeafOtherThan(r: UndirectedGraphVertex[VP]): Option[UndirectedGraphVertex[VP]]

  def eliminate(v: UndirectedGraphVertex[VP], payload: (UndirectedGraphVertex[VP], UndirectedGraphVertex[VP]) => EP): GenUndirectedGraph[VP, EP]

  // def eliminate(vs: List[V], payload: (V, V) => EP): GenUndirectedGraph[VP, EP]
}
