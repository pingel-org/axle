package axle.algebra

import spire.algebra.Eq
import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for DirectedGraph[${DG}, ${V}, ${E}]")
trait DirectedGraph[DG, V, E] {

  def make(Vs: Seq[V], ef: Seq[(V, V, E)]): DG

  def vertices(jdg: DG): Iterable[V]

  def edges(jdg: DG): Iterable[E]

  def source(jdg: DG, e: E): V

  def destination(jdg: DG, e: E): V

  def findVertex(jdg: DG, f: V => Boolean): Option[V]

  def filterEdges(jdg: DG, f: E => Boolean): DG

  def areNeighbors(jdg: DG, v1: V, v2: V): Boolean

  def isClique(jdg: DG, vs: collection.GenTraversable[V])(implicit eqV: Eq[V]): Boolean

  // TODO? Manifest[V]?
  def forceClique(jdg: DG, among: Set[V], payload: (V, V) => E)(implicit eqV: Eq[V]): DG

  def degree(jdg: DG, v: V): Int

  def edgesTouching(jdg: DG, v: V): Set[E]

  def neighbors(jdg: DG, v: V): Set[V]

  // a "leaf" is vertex with only one neighbor
  def firstLeafOtherThan[V, E](jdg: DG, r: V): Option[V]

  def eliminate(jdg: DG, v: V, payload: (V, V) => E): DG

  def other(jdg: DG, edge: E, u: V)(implicit eqV: Eq[V]): V

  def connects(jdg: DG, edge: E, a1: V, a2: V)(implicit eqV: Eq[V]): Boolean

  def leaves(jdg: DG): Set[V]

  def precedes(jdg: DG, v1: V, v2: V): Boolean

  def predecessors(jdg: DG, v: V): Set[V]

  def isLeaf(jdg: DG, v: V): Boolean

  def successors(jdg: DG, v: V): Set[V]

  def outputEdgesOf(jdg: DG, v: V): Set[E]

  def descendantsIntersectsSet(jdg: DG, v: V, s: Set[V]): Boolean

  def removeInputs(jdg: DG, to: Set[V]): DG

  def removeOutputs(jdg: DG, from: Set[V]): DG

  def moralGraph(jdg: DG): Boolean

  def isAcyclic(jdg: DG): Boolean

  def shortestPath(jdg: DG, source: V, goal: V)(implicit eqV: Eq[V]): Option[List[E]]

  // inefficient
  def _descendants(jdg: DG, v: V, accumulator: Set[V]): Set[V] =
    if (!accumulator.contains(v)) {
      successors(jdg, v).foldLeft(accumulator + v)((a, v) => _descendants(jdg, v, a))
    } else {
      accumulator
    }

  def descendants(jdg: DG, v: V): Set[V] =
    _descendants(jdg, v, Set[V]())

  //  // inefficient
  //  def _ancestors(v: Vertex[V], accumulator: Set[Vertex[V]]): Set[Vertex[V]] =
  //    if (!accumulator.contains(v)) {
  //      predecessors(v).foldLeft(accumulator + v)((a, v) => _ancestors(v, a))
  //    } else {
  //      accumulator
  //    }

  //  def ancestors(v: Vertex[V]): Set[Vertex[V]] = _ancestors(v, Set[Vertex[V]]())

  //  def ancestors(vs: Set[Vertex[V]]): Set[Vertex[V]] =
  //    vs.foldLeft(Set[Vertex[V]]())((a, v) => _ancestors(v, a))

}

object DirectedGraph {

  @inline final def apply[DG, V, E](implicit dg: DirectedGraph[DG, V, E]): DirectedGraph[DG, V, E] = dg

}
