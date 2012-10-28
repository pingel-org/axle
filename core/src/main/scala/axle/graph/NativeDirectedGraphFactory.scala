package axle.graph

import collection._
import scalaz._
import Scalaz._

trait NativeDirectedGraph[VP, EP] extends GenDirectedGraph[VP, EP] {

  type V = NativeDirectedGraphVertex[VP]
  type E = NativeDirectedGraphEdge[EP]

  type S = (mutable.Set[V], mutable.Set[E], Map[V, mutable.Set[E]], Map[V, mutable.Set[E]])

  val _vertices = mutable.Set[V]()
  val _edges = mutable.Set[E]()
  val vertex2outedges = mutable.Map[V, mutable.Set[E]]().withDefaultValue(mutable.Set[E]())
  val vertex2inedges = mutable.Map[V, mutable.Set[E]]().withDefaultValue(mutable.Set[E]())

  def storage() = (_vertices, _edges, vertex2outedges, vertex2inedges)

  def size() = _vertices.size

  trait NativeDirectedGraphVertex[P] extends DirectedGraphVertex[P]

  trait NativeDirectedGraphEdge[P] extends DirectedGraphEdge[P]

  class NativeDirectedGraphVertexImpl[P](_payload: P) extends NativeDirectedGraphVertex[P] {

    self: V =>

    _vertices += this

    def payload(): P = _payload
  }

  class NativeDirectedGraphEdgeImpl[P](source: V, dest: V, _payload: P) extends NativeDirectedGraphEdge[P] {

    self: E =>

    _edges += this // assume that this edge isn't already in our list of edges

    vertex2outedges(source) += this
    vertex2inedges(dest) += this

    def source(): V = source
    def dest(): V = dest

    def payload(): P = _payload
  }

  def edges() = _edges.toSet // immutable copy

  def vertices() = _vertices.toSet // immutable copy

  def findEdge(from: V, to: V): Option[E] = vertex2outedges(from).find(_.dest == to)

  def edge(source: V, dest: V, payload: EP): (NativeDirectedGraph[VP, EP], E) = new NativeDirectedGraphEdgeImpl[EP](source, dest, payload)

  def ++(eps: Seq[(V, V, EP)]): (NativeDirectedGraph[VP, EP], Seq[E]) = todo

  def vertex(payload: VP): (NativeDirectedGraph[VP, EP], V) = new NativeDirectedGraphVertexImpl[VP](payload)

  def deleteEdge(e: E): NativeDirectedGraph[VP, EP] = {
    _edges -= e
    vertex2outedges.get(e.source()).map(_.remove(e))
    vertex2inedges.get(e.dest()).map(_.remove(e))
  }

  def deleteVertex(v: V): NativeDirectedGraph[VP, EP] = {
    vertex2outedges.get(v).map(outEdges =>
      outEdges.map(e => {
        _edges -= e
        vertex2inedges.get(e.dest()).map(_.remove(e))
      }))
    vertex2outedges -= v

    vertex2inedges.get(v).map(inEdges =>
      inEdges.map(e => {
        _edges -= e
        vertex2outedges.get(e.source()).map(_.remove(e))
      }))
    vertex2inedges -= v

    _vertices -= v
  }

  def leaves(): Set[V] = vertices().filter(isLeaf(_))

  def neighbors(v: V): Set[V] = predecessors(v) ++ successors(v)

  def precedes(v1: V, v2: V): Boolean = predecessors(v2).contains(v1)

  def predecessors(v: V): Set[V] = vertex2inedges(v).map(_.source())

  def isLeaf(v: V): Boolean = {
    val outEdges = vertex2outedges(v)
    outEdges == null || outEdges.size == 0
  }

  def successors(v: V): Set[V] = vertex2outedges(v).map(_.dest())

  def outputEdgesOf(v: V): immutable.Set[E] = vertex2outedges(v).toSet

  def descendantsIntersectsSet(v: V, s: Set[V]): Boolean =
    s.contains(v) || s.exists(x => descendantsIntersectsSet(x, s))

  def removeInputs(vs: Set[V]): NativeDirectedGraph[VP, EP] = vs.map(v => {
    vertex2inedges.get(v).map(incoming => {
      incoming.map(_edges -= _)
      vertex2inedges += v -> null
    })
  })

  def removeOutputs(vs: Set[V]): NativeDirectedGraph[VP, EP] = vs.map(v => {
    vertex2outedges.get(v).map(outgoing => {
      outgoing.map(_edges -= _)
      vertex2outedges += v -> null
    })
  })

  def removeSuccessor(v: V, successor: V): NativeDirectedGraph[VP, EP] = {
    vertex2outedges.get(v) map { outgoing =>
      outgoing.find(_.dest().equals(successor)) map { edgeToRemove =>
        outgoing.remove(edgeToRemove)
        _edges -= edgeToRemove
      }
    }
  }

  def removePredecessor(v: V, predecessor: V): NativeDirectedGraph[VP, EP] = {
    vertex2inedges.get(v) map { incoming =>
      incoming.find(_.source().equals(predecessor)).map(edgeToRemove => {
        incoming.remove(edgeToRemove)
        _edges -= edgeToRemove // we should really only do this if it's the last of the pair of calls. ick.
      })
    }
  }

  def isAcyclic() = true // TODO !!!

  /**
   * shortestPath
   *
   * TODO: This is just a quick, dirty, slow, and naive algorithm.
   */

  def _shortestPath(source: V, goal: V, visited: Set[V]): Option[List[E]] = if (source == goal) {
    Some(List())
  } else {
    outputEdgesOf(source).filter(edge => !visited.contains(edge.dest))
      .flatMap(edge => _shortestPath(edge.dest, goal, visited + source).map(sp => edge :: sp))
      .reduceOption((l1, l2) => (l1.length < l2.length) ? l1 | l2)
  }

  def shortestPath(source: V, goal: V): Option[List[E]] = _shortestPath(source, goal, Set())

}

object NativeDirectedGraph extends DirectedGraphFactory {

  def apply[A, B](): NativeDirectedGraph[A, B] = new NativeDirectedGraph[A, B]() {}

}
