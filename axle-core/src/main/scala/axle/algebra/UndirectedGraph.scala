package axle.algebra

import spire.algebra.Eq
import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for UndirectedGraph[${UG}, ${V}, ${E}]")
trait UndirectedGraph[UG, V, E] {

  def make(vps: Seq[V], ef: Seq[(V, V, E)]): UG

  def vertices(jug: UG): Iterable[V]

  def edges(jug: UG): Iterable[E]

  def vertices(jusg: UG, e: E): (V, V)

  // TODO findVertex needs an index

  def findVertex(jug: UG, f: V => Boolean): Option[V]

  def filterEdges(jug: UG, f: E => Boolean): UG

  //  def unlink(e: Edge[ES, E]): UG

  //  // UG
  //  def unlink(v1: Vertex[V], v2: Vertex[V])

  def areNeighbors(jug: UG, v1: V, v2: V)(implicit eqV: Eq[V]): Boolean

  def isClique(jug: UG, vs: collection.GenTraversable[V])(implicit eqV: Eq[V]): Boolean

  def forceClique(jug: UG, among: Set[V], payload: (V, V) => E)(implicit eqV: Eq[V], mv: Manifest[V]): UG

  def degree(jug: UG, v: V): Int

  def edgesTouching(jug: UG, v: V): Set[E]

  def neighbors(jug: UG, v: V): Set[V]

  //  def delete(v: Vertex[V]): UG

  // a "leaf" is vertex with only one neighbor
  def firstLeafOtherThan(jug: UG, r: V)(implicit eqV: Eq[V]): Option[V]

  /**
   * "decompositions" page 3 (Definition 3, Section 9.3)
   * turn the neighbors of v into a clique
   */

  def eliminate(jug: UG, v: V, payload: (V, V) => E): UG

  def other(jug: UG, edge: E, u: V)(implicit eqV: Eq[V]): V

  def connects(jug: UG, edge: E, a1: V, a2: V)(implicit eqV: Eq[V]): Boolean

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

  final def apply[UG, V, E](implicit ug: UndirectedGraph[UG, V, E]): UndirectedGraph[UG, V, E] = ug

}
