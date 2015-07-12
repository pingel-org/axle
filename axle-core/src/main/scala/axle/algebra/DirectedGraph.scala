package axle.algebra

import spire.algebra.Eq
import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for DirectedGraph[${DG}]")
trait DirectedGraph[DG[_, _]] {

  def make[V, E](Vs: Seq[V], ef: Seq[V] => Seq[(V, V, E)]): DG[V, E]

  def vertices[V, E](jdg: DG[V, E]): Iterable[V]

  def edges[V, E](jdg: DG[V, E]): Iterable[E]

  def size[V, E](jdg: DG[V, E]): Int

  // TODO findVertex needs an index
  def findVertex[V, E](jdg: DG[V, E], f: V => Boolean): Option[V]

  def filterEdges[V, E](jdg: DG[V, E], f: E => Boolean): DG[V, E]

  def areNeighbors[V: Eq, E](jdg: DG[V, E], v1: V, v2: V): Boolean

  def isClique[V: Eq, E](jdg: DG[V, E], vs: collection.GenTraversable[V]): Boolean

  def forceClique[V: Eq: Manifest, E](jdg: DG[V, E], among: Set[V], payload: (V, V) => E): DG[V, E]

  def degree[V, E](jdg: DG[V, E], v: V): Int

  def edgesTouching[V, E](jdg: DG[V, E], v: V): Set[E]

  def neighbors[V, E](jdg: DG[V, E], v: V): Set[V]

  // a "leaf" is vertex with only one neighbor
  def firstLeafOtherThan[V: Eq, E](jdg: DG[V, E], r: V): Option[V]

  def eliminate[V, E](jdg: DG[V, E], v: V, payload: (V, V) => E): DG[V, E]

  def other[V: Eq, E](jdg: DG[V, E], edge: E, u: V): V

  def connects[V: Eq, E](jdg: DG[V, E], edge: E, a1: V, a2: V): Boolean

  def map[V, E, NV, NE](jdg: DG[V, E], Vf: V => NV, epf: E => NE): DG[NV, NE]

  def leaves[V: Eq, E](jdg: DG[V, E]): Set[V]

  def precedes[V, E](jdg: DG[V, E], v1: V, v2: V): Boolean

  def predecessors[V, E](jdg: DG[V, E], v: V): Set[V]

  def isLeaf[V, E](jdg: DG[V, E], v: V): Boolean

  def successors[V, E](jdg: DG[V, E], v: V): Set[V]

  def outputEdgesOf[V, E](jdg: DG[V, E], v: V): Set[E]

  def descendantsIntersectsSet[V, E](jdg: DG[V, E], v: V, s: Set[V]): Boolean

  def removeInputs[V, E](jdg: DG[V, E], to: Set[V]): DG[V, E]

  def removeOutputs[V, E](jdg: DG[V, E], from: Set[V]): DG[V, E]

  def moralGraph[V, E](jdg: DG[V, E]): Boolean

  def isAcyclic[V, E](jdg: DG[V, E]): Boolean

  def shortestPath[V: Eq, E](jdg: DG[V, E], source: V, goal: V): Option[List[E]]

  // inefficient
  def _descendants[V, E](jdg: DG[V, E], v: V, accumulator: Set[V]): Set[V] =
    if (!accumulator.contains(v)) {
      successors(jdg, v).foldLeft(accumulator + v)((a, v) => _descendants(jdg, v, a))
    } else {
      accumulator
    }

  def descendants[V, E](jdg: DG[V, E], v: V): Set[V] =
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

  @inline final def apply[DG[_, _]: DirectedGraph]: DirectedGraph[DG] = implicitly[DirectedGraph[DG]]

}
