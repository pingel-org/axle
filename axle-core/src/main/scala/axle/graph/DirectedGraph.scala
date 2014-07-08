package axle.graph

import spire.algebra.Eq

trait DirectedGraph[VP, EP] {

  type G[VP, EP] <: DirectedGraph[VP, EP]
  type ES

  def vertexPayloads: Seq[VP]
  def edgeFunction: Seq[Vertex[VP]] => Seq[(Vertex[VP], Vertex[VP], EP)]

  def vertices: Set[Vertex[VP]]
  def allEdges: Set[Edge[ES, EP]]

  def size: Int

  def source(edge: Edge[ES, EP]): Vertex[VP]
  def dest(edge: Edge[ES, EP]): Vertex[VP]

  //def deleteVertex(v: Vertex[VP]): G[VP, EP]
  def findVertex(f: Vertex[VP] => Boolean): Option[Vertex[VP]]
  def findEdge(from: Vertex[VP], to: Vertex[VP]): Option[Edge[ES, EP]]
  def leaves: Set[Vertex[VP]]
  def neighbors(v: Vertex[VP]): Set[Vertex[VP]]
  def precedes(v1: Vertex[VP], v2: Vertex[VP]): Boolean
  def predecessors(v: Vertex[VP]): Set[Vertex[VP]]
  def isLeaf(v: Vertex[VP]): Boolean
  def successors(v: Vertex[VP]): Set[Vertex[VP]]
  def outputEdgesOf(v: Vertex[VP]): Set[Edge[ES, EP]]
  def descendantsIntersectsSet(v: Vertex[VP], s: Set[Vertex[VP]]): Boolean

  // inefficient
  def _descendants(v: Vertex[VP], accumulator: Set[Vertex[VP]]): Set[Vertex[VP]] =
    if (!accumulator.contains(v)) {
      successors(v).foldLeft(accumulator + v)((a, v) => _descendants(v, a))
    } else {
      accumulator
    }

  def descendants(v: Vertex[VP]): Set[Vertex[VP]] = _descendants(v, Set[Vertex[VP]]())

  // inefficient
  def _ancestors(v: Vertex[VP], accumulator: Set[Vertex[VP]]): Set[Vertex[VP]] =
    if (!accumulator.contains(v)) {
      predecessors(v).foldLeft(accumulator + v)((a, v) => _ancestors(v, a))
    } else {
      accumulator
    }

  def ancestors(v: Vertex[VP]): Set[Vertex[VP]] = _ancestors(v, Set[Vertex[VP]]())

  def ancestors(vs: Set[Vertex[VP]]): Set[Vertex[VP]] =
    vs.foldLeft(Set[Vertex[VP]]())((a, v) => _ancestors(v, a))

  def isAcyclic: Boolean

  def shortestPath(source: Vertex[VP], goal: Vertex[VP]): Option[List[Edge[ES, EP]]]
  // def moralGraph(): UndirectedGraph[_, _] = ???

  // def deleteEdge(e: Edge[VP, EP]): DirectedGraph[VP, EP]

  // def deleteVertex(v: Vertex[VP]): DirectedGraph[VP, EP]

  //  def removeInputs(vs: Set[V]): GenDirectedGraph[VP, EP]
  //  def removeOutputs(vs: Set[V]): GenDirectedGraph[VP, EP]

  def map[NVP: Manifest: Eq, NEP: Eq](vpf: VP => NVP, epf: EP => NEP): G[NVP, NEP]

}
