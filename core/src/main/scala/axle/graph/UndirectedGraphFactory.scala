package axle.graph

import collection._
import axle._

trait UndirectedGraphFactory {

  type G[VP, EP] <: UndirectedGraph[VP, EP]
  type V[VP] <: UndirectedGraphVertex[VP]
  type E[VP, EP] <: UndirectedGraphEdge[VP, EP]

  def apply[VP, EP](vps: Seq[VP], ef: Seq[V[VP]] => Seq[(V[VP], V[VP], EP)])

  trait UndirectedGraph[VP, EP] {

    def vertices(): Set[V[VP]]
    def edges(): Set[E[VP, EP]]

    def unlink(e: E[VP, EP]): G[VP, EP]
    def unlink(v1: V[VP], v2: V[VP]): G[VP, EP]
    def areNeighbors(v1: V[VP], v2: V[VP]): Boolean

    def isClique(vs: GenTraversable[V[VP]]): Boolean = (for {
      vi <- vs
      vj <- vs
    } yield {
      (vi == vj) || areNeighbors(vi, vj)
    }).forall(b => b)

    def numEdgesToForceClique(vs: GenTraversable[V[VP]], payload: (V[VP], V[VP]) => EP) = (for {
      vi <- vs
      vj <- vs
    } yield {
      if (areNeighbors(vi, vj)) 1 else 0
    }).sum

    def forceClique(vs: Set[V[VP]], payload: (V[VP], V[VP]) => EP): G[VP, EP]

    // assert: among is a subset of vertices
    def vertexWithFewestEdgesToEliminateAmong(among: Set[V[VP]], payload: (V[VP], V[VP]) => EP): V[VP] =
      among.map(v => (v, numEdgesToForceClique(neighbors(v), payload))).minBy(_._2)._1

    // assert: among is a subset of vertices
    def vertexWithFewestNeighborsAmong(among: Set[V[VP]]): V[VP] =
      among.map(v => (v, neighbors(v).size)).minBy(_._2)._1

    def degree(v: V[VP]): Int

    def edges(v: V[VP]): Set[E[VP, EP]]

    def neighbors(v: V[VP]): Set[V[VP]]

    def delete(v: V[VP]): G[VP, EP]

    // a "leaf" is vertex with only one neighbor
    def firstLeafOtherThan(r: V[VP]): Option[V[VP]]

    def eliminate(v: V[VP], payload: (V[VP], V[VP]) => EP): G[VP, EP]

    // def eliminate(vs: List[V], payload: (V, V) => EP): GenUndirectedGraph[VP, EP]

  }

  trait UndirectedGraphEdge[VP, EP] {

    def vertices(): (V[VP], V[VP])

    def other(u: V[VP]): V[VP] = {
      val (v1, v2) = vertices()
      u match {
        case _ if u.equals(v1) => v2
        case _ if u.equals(v2) => v1
        case _ => throw new Exception("can't find 'other' of a vertex that isn't on the edge itself")
      }
    }

    def connects(a1: V[VP], a2: V[VP]) = {
      val (v1, v2) = vertices()
      (v1 == a1 && v2 == a2) || (v2 == a1 && v1 == a2)
    }

  }

  trait UndirectedGraphVertex[VP]
}

