package axle.algebra

import spire.algebra.Eq
import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for UndirectedGraph[${UG}]")
trait UndirectedGraph[UG[_, _]] {

  def make[V, E](vps: Seq[V], ef: Seq[V] => Seq[(V, V, E)]): UG[V, E]

  def vertices[V, E](jug: UG[V, E]): Iterable[V]

  def edges[V, E](jug: UG[V, E]): Iterable[E]

  def vertices[V, E](jusg: UG[V, E], e: E): (V, V)

  def size[V, E](jug: UG[V, E]): Int

  // TODO findVertex needs an index
  def findVertex[V, E](jug: UG[V, E], f: V => Boolean): Option[V]

  def filterEdges[V, E](jug: UG[V, E], f: E => Boolean): UG[V, E]

  //  def unlink(e: Edge[ES, E]): UG[V, E]

  //  // UG[V, E]
  //  def unlink(v1: Vertex[V], v2: Vertex[V])

  def areNeighbors[V: Eq, E](jug: UG[V, E], v1: V, v2: V): Boolean

  def isClique[V: Eq, E](jug: UG[V, E], vs: collection.GenTraversable[V]): Boolean

  def forceClique[V: Eq: Manifest, E](jug: UG[V, E], among: Set[V], payload: (V, V) => E): UG[V, E]

  def degree[V, E](jug: UG[V, E], v: V): Int

  def edgesTouching[V, E](jug: UG[V, E], v: V): Set[E]

  def neighbors[V, E](jug: UG[V, E], v: V): Set[V]

  //  def delete(v: Vertex[V]): UG[V, E]

  // a "leaf" is vertex with only one neighbor
  def firstLeafOtherThan[V: Eq, E](jug: UG[V, E], r: V): Option[V]

  /**
   * "decompositions" page 3 (Definition 3, Section 9.3)
   * turn the neighbors of v into a clique
   */

  def eliminate[V, E](jug: UG[V, E], v: V, payload: (V, V) => E): UG[V, E]

  def other[V: Eq, E](jug: UG[V, E], edge: E, u: V): V

  def connects[V: Eq, E](jug: UG[V, E], edge: E, a1: V, a2: V): Boolean

  def map[V, E, NV, NE](jug: UG[V, E], vpf: V => NV, epf: E => NE): UG[NV, NE]

  //  def numEdgesToForceClique(vs: collection.GenTraversable[Vertex[V]], payload: (Vertex[V], Vertex[V]) => E): Int =
  //    (for {
  //      vi <- vs
  //      vj <- vs
  //    } yield { if (areNeighbors(vi, vj)) 1 else 0 }).sum

  //
  //  // assert: among is a subset of vertices
  //  def vertexWithFewestEdgesToEliminateAmong(among: Set[Vertex[V]], payload: (Vertex[V], Vertex[V]) => E): Vertex[V] =
  //    among.map(v => (v, numEdgesToForceClique(neighbors(v), payload))).minBy(_._2)._1
  //
  //  // assert: among is a subset of vertices
  //  def vertexWithFewestNeighborsAmong(among: Set[Vertex[V]]): Vertex[V] =
  //    among.map(v => (v, neighbors(v).size)).minBy(_._2)._1

}

object UndirectedGraph {

  @inline final def apply[UG[_, _]: UndirectedGraph]: UndirectedGraph[UG] = implicitly[UndirectedGraph[UG]]

}
