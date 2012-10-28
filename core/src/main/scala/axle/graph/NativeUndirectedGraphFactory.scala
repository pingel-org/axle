package axle.graph

import collection._
import axle._

trait NativeUndirectedGraph[VP, EP] extends GenUndirectedGraph[VP, EP] {

  type V = NativeUndirectedGraphVertex[VP]
  type E = NativeUndirectedGraphEdge[EP]

  type S = (mutable.Set[V], mutable.Set[E], Map[V, mutable.Set[E]])

  val _vertices = mutable.Set[V]()
  val _edges = mutable.Set[E]()
  val vertex2edges = mutable.Map[V, mutable.Set[E]]().withDefaultValue(mutable.Set[E]())

  def storage() = (_vertices, _edges, vertex2edges)

  def vertices() = _vertices.toSet

  def edges() = _edges.toSet

  def size() = _vertices.size

  trait NativeUndirectedGraphVertex[P] extends UndirectedGraphVertex[P]

  trait NativeUndirectedGraphEdge[P] extends UndirectedGraphEdge[P]

  class NativeUndirectedGraphVertexImpl[P](_payload: P) extends NativeUndirectedGraphVertex[P] {
    self: V =>
    _vertices += this
    def payload(): P = _payload
  }

  class NativeUndirectedGraphEdgeImpl[P](v1: V, v2: V, _payload: P) extends NativeUndirectedGraphEdge[P] {

    self: E =>

    // assume that this edge isn't already in our list of edges
    _edges += this
    vertex2edges(v1) += this
    vertex2edges(v2) += this

    def vertices(): (V, V) = (v1, v2)
    def payload(): P = _payload
  }

  def vertex(payload: VP): (NativeUndirectedGraph[VP, EP], NativeUndirectedGraphVertex[VP]) = new NativeUndirectedGraphVertexImpl[VP](payload)

  def edge(v1: V, v2: V, payload: EP): (NativeUndirectedGraph[VP, EP], NativeUndirectedGraphEdge[EP]) = new NativeUndirectedGraphEdgeImpl[EP](v1, v2, payload)

  def ++(eps: Seq[(V, V, EP)]): (NativeUndirectedGraph[VP, EP], Seq[E]) = todo

  def unlink(e: E): NativeUndirectedGraph[VP, EP] = {
    val dble = e.vertices()
    vertex2edges(dble._1) -= e
    vertex2edges(dble._2) -= e
    _edges -= e
  }

  def unlink(v1: V, v2: V): NativeUndirectedGraph[VP, EP] = edges(v1).filter(_.other(v1).equals(v2)).map(unlink(_))

  def areNeighbors(v1: V, v2: V): Boolean = edges(v1).exists(_.connects(v1, v2))

  def forceClique(vs: Set[V], payload: (V, V) => EP): NativeUndirectedGraph[VP, EP] =
    vs.doubles().filter({ case (vi, vj) => !areNeighbors(vi, vj) })
      .map({ case (vi, vj) => edge(vi, vj, payload(vi, vj)) })

  override def isClique(vs: Set[V]): Boolean =
    vs.doubles.∀({ case (a, b) => ((a == b) || areNeighbors(a, b)) })

  def degree(v: V): Int = edges(v).size

  def edges(v: V): Set[E] = vertex2edges(v)

  def neighbors(v: V): Set[V] = edges(v).map(_.other(v)).toSet

  def delete(v: V): NativeUndirectedGraph[VP, EP] = {
    val es = edges(v)
    _vertices -= v
    vertex2edges.remove(v)
    for (e <- es) {
      _edges -= e
      vertex2edges.get(e.other(v)) map { otherEdges => otherEdges.remove(e) }
    }
  }

  // a "leaf" is vertex with only one neighbor
  def firstLeafOtherThan(r: V): Option[V] = vertices.find({ v => neighbors(v).size == 1 && !v.equals(r) })

  def eliminate(v: V, payload: (V, V) => EP): NativeUndirectedGraph[VP, EP] = {
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
  def eliminate(vs: immutable.List[V], payload: (V, V) => EP): NativeUndirectedGraph[VP, EP] = vs.map(eliminate(_, payload))

  /**
   * dijkstra
   *
   * Modelled after psuedocode on Wikipedia:
   *
   *   http://en.wikipedia.org/wiki/Dijkstra's_algorithm
   */

  def dijkstra(source: V, target: V): Map[V, Int] = {

    def edgeCost(v1: V, v2: V): Int = 1 // TODO: generalize

    val undefined = -1
    val dist = mutable.Map[V, Int]()
    val previous = mutable.Map[V, V]()
    for (v <- vertices) {
      dist(v) = Int.MaxValue // Unknown distance function from source to v
    }

    dist(source) = 0 // Distance from source to source
    val Q = mutable.Set[V]() ++ vertices // All nodes in the graph are unoptimized - thus are in Q
    var broken = false
    while (Q.size > 0 && !broken) {
      val u = Q.minBy(dist(_)) // Start node in first case
      Q -= u
      if (u == target) {
        var S = List[V]()
        var u = target
        while (previous.contains(u)) {
          S = u :: S
          u = previous(u)
        }
      }
      if (dist(u) == Int.MaxValue) {
        broken = true // all remaining vertices are inaccessible from source
      } else {
        for (v <- neighbors(u)) { // where v has not yet been removed from Q
          val alt = dist(u) + edgeCost(u, v)
          if (alt < dist(v)) { // Relax (u,v,a)
            dist(v) = alt
            previous(v) = u
            // TODO decrease - key v in Q // Reorder v in the Queue
          }
        }
      }
    }
    dist
  }

}

object NativeUndirectedGraph extends UndirectedGraphFactory {

  def graph[A, B](): NativeUndirectedGraph[A, B] = new NativeUndirectedGraph[A, B]() {}

}