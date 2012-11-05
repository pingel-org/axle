package axle.graph

import collection._
import axle._

trait NativeUndirectedGraphFactory extends UndirectedGraphFactory {

  def apply[VP, EP](): NativeUndirectedGraph[VP, EP] =
    new NativeUndirectedGraphImpl[VP, EP](List[VP](), vs => List[(NativeUndirectedGraphVertex[VP], NativeUndirectedGraphVertex[VP], EP)]())

  def apply[VP, EP](vps: Seq[VP], ef: (Seq[NativeUndirectedGraphVertex[VP]]) => Seq[(NativeUndirectedGraphVertex[VP], NativeUndirectedGraphVertex[VP], EP)]): NativeUndirectedGraph[VP, EP] =
    new NativeUndirectedGraphImpl[VP, EP](vps, ef)

}

object NativeUndirectedGraph extends NativeUndirectedGraphFactory

trait NativeUndirectedGraphVertex[VP] extends UndirectedGraphVertex[VP]

trait NativeUndirectedGraphEdge[VP, EP] extends UndirectedGraphEdge[VP, EP]

class NativeUndirectedGraphVertexImpl[VP](_payload: VP)
  extends NativeUndirectedGraphVertex[VP] {

  def payload(): VP = _payload
}

class NativeUndirectedGraphEdgeImpl[VP, EP](v1: NativeUndirectedGraphVertex[VP], v2: NativeUndirectedGraphVertex[VP], _payload: EP)
  extends NativeUndirectedGraphEdge[VP, EP] {

  def vertices(): (NativeUndirectedGraphVertex[VP], NativeUndirectedGraphVertex[VP]) = (v1, v2)
  def payload(): EP = _payload
}

trait NativeUndirectedGraph[VP, EP] extends GenUndirectedGraph[VP, EP]

class NativeUndirectedGraphImpl[VP, EP](vps: Seq[VP], ef: (Seq[NativeUndirectedGraphVertex[VP]]) => Seq[(NativeUndirectedGraphVertex[VP], NativeUndirectedGraphVertex[VP], EP)])
  extends NativeUndirectedGraph[VP, EP] {

  type V = NativeUndirectedGraphVertex[VP]
  type E = NativeUndirectedGraphEdge[VP, EP]
  type S = (Set[V], Set[E], Map[V, Set[E]])

  val _vertices: Set[V] = 4
  val _edges: Set[E] = 4
  val vertex2edges: Map[V, Set[E]] = Map[V, Set[E]]().withDefaultValue(Set[E]())

  def storage(): S = (_vertices, _edges, vertex2edges)
  def vertices(): Set[V] = _vertices.toSet
  def edges(): Set[E] = _edges.toSet

  def size(): Int = _vertices.size

  def vertex(payload: VP): (NativeUndirectedGraph[VP, EP], NativeUndirectedGraphVertex[VP]) =
    new NativeUndirectedGraphVertexImpl[VP](payload)

  def edge(v1: V, v2: V, payload: EP): (NativeUndirectedGraph[VP, EP], NativeUndirectedGraphEdge[VP, EP]) =
    new NativeUndirectedGraphEdgeImpl[EP](v1, v2, payload)

  def unlink(e: E): NativeUndirectedGraph[VP, EP] = 4

  def unlink(v1: V, v2: V): NativeUndirectedGraph[VP, EP] = 4

  def areNeighbors(v1: V, v2: V): Boolean = edges(v1).exists(_.connects(v1, v2))

  def forceClique(vs: Set[V], payload: (V, V) => EP): NativeUndirectedGraph[VP, EP] =
    vs.doubles().filter({ case (vi, vj) => !areNeighbors(vi, vj) })
      .map({ case (vi, vj) => edge(vi, vj, payload(vi, vj)) })

  override def isClique(vs: Set[V]): Boolean =
    vs.doubles.∀({ case (vi, vj) => ((vi == vj) || areNeighbors(vi, vj)) })

  def degree(v: V): Int = edges(v).size

  def edges(v: V): Set[E] = vertex2edges(v)

  def neighbors(v: V): Set[V] = edges(v).map(_.other(v)).toSet

  def delete(v: V): NativeUndirectedGraph[VP, EP] = 4

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
