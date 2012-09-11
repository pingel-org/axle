package axle.graph

import collection._
import axle._

object NativeUndirectedGraphFactory extends NativeUndirectedGraphFactory

trait NativeUndirectedGraphFactory extends UndirectedGraphFactory {

  type G[VP, EP] = NativeUndirectedGraph[VP, EP]

  def graph[A, B](): G[A, B] = new NativeUndirectedGraph[A, B]() {}

  trait NativeUndirectedGraph[VP, EP] extends UndirectedGraph[VP, EP] {

    type V = NativeUndirectedGraphVertex[VP]
    type E = NativeUndirectedGraphEdge[EP]

    type S = (mutable.Set[V], mutable.Set[E], Map[V, mutable.Set[E]])

    val _vertices = mutable.Set[V]()
    val _edges = mutable.Set[E]()
    val vertex2edges = mutable.Map[V, mutable.Set[E]]()

    def storage() = (_vertices, _edges, vertex2edges)

    def vertices() = _vertices.toSet

    def edges() = _edges.toSet

    def size() = _vertices.size

    trait NativeUndirectedGraphVertex[P] extends UndirectedGraphVertex[P] {
      def setPayload(p: P): Unit = {} // TODO payload = p // type erasure problem
    }

    trait NativeUndirectedGraphEdge[P] extends UndirectedGraphEdge[P] {
      def setPayload(p: P): Unit = {} // TODO payload = p // type erasure problem
    }

    class NativeUndirectedGraphVertexImpl[P](var _payload: P) extends NativeUndirectedGraphVertex[P] {
      self: V =>
      _vertices += this
      def payload(): P = _payload
    }

    class NativeUndirectedGraphEdgeImpl[P](v1: V, v2: V, var _payload: P) extends NativeUndirectedGraphEdge[P] {

      self: E =>

      // assume that this edge isn't already in our list of edges
      _edges += this
      edges(v1) += this
      edges(v2) += this

      def vertices(): (V, V) = (v1, v2)
      def payload(): P = _payload
    }

    def vertex(payload: VP): NativeUndirectedGraphVertex[VP] = new NativeUndirectedGraphVertexImpl[VP](payload)

    def edge(v1: V, v2: V, payload: EP): NativeUndirectedGraphEdge[EP] = new NativeUndirectedGraphEdgeImpl[EP](v1, v2, payload)

    def copyTo(other: UndirectedGraph[VP, EP]) = {
      // TODO
    }

    def unlink(e: E): Unit = {
      val dble = e.vertices()
      edges(dble._1) -= e
      edges(dble._2) -= e
      _edges -= e
    }

    def unlink(v1: V, v2: V): Unit = edges(v1).filter(_.other(v1).equals(v2)).map(unlink(_))

    def areNeighbors(v1: V, v2: V) = edges(v1).exists(_.connects(v1, v2))

    override def isClique(vs: Set[V]): Boolean =
      vs.doubles.∀({ case (a, b) => ((a == b) || areNeighbors(a, b)) })

    def degree(v: V) = edges(v).size

    def edges(v: V) = {
      if (!vertex2edges.contains(v)) {
        vertex2edges += v -> mutable.Set[E]()
      }
      vertex2edges(v)
    }

    def neighbors(v: V): Set[V] = edges(v).map(_.other(v)).toSet

    def delete(v: V) = {
      val es = edges(v)
      _vertices -= v
      vertex2edges.remove(v)
      for (e <- es) {
        _edges -= e
        vertex2edges.get(e.other(v)) map { otherEdges => otherEdges.remove(e) }
      }
    }

    // a "leaf" is vertex with only one neighbor
    def firstLeafOtherThan(r: V) = vertices.find({ v => neighbors(v).size == 1 && !v.equals(r) })

    def eliminate(v: V, payload: (V, V) => EP) = {
      // "decompositions" page 3 (Definition 3, Section 9.3)
      // turn the neighbors of v into a clique

      val es = edges(v)
      val vs = neighbors(v)

      _vertices -= v
      vertex2edges.remove(v)
      _edges --= es

      forceClique(vs.asInstanceOf[Set[V]], payload)
    }

    // TODO there is probably a more efficient way to do this:
    def eliminate(vs: immutable.List[V], payload: (V, V) => EP): Unit = vs.map(eliminate(_, payload))

  }

}