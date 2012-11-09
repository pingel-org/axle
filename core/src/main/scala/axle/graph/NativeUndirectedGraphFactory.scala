package axle.graph

import collection._
import axle._

trait NativeUndirectedGraphFactory extends UndirectedGraphFactory {

  def apply[VP, EP](): NativeUndirectedGraph[VP, EP] =
    new NativeUndirectedGraph(List[VP](), vs => List[(NativeUndirectedGraphVertex[VP], NativeUndirectedGraphVertex[VP], EP)]())

  def apply[VP, EP](vps: Seq[VP], ef: (Seq[NativeUndirectedGraphVertex[VP]]) => Seq[(NativeUndirectedGraphVertex[VP], NativeUndirectedGraphVertex[VP], EP)]): NativeUndirectedGraph[VP, EP] =
    new NativeUndirectedGraph(vps, ef)

}

object NativeUndirectedGraph extends NativeUndirectedGraphFactory

class NativeUndirectedGraphVertex[VP](_payload: VP)
  extends UndirectedGraphVertex[VP] {

  def payload(): VP = _payload
}

class NativeUndirectedGraphEdge[VP, EP](
  v1: NativeUndirectedGraphVertex[VP],
  v2: NativeUndirectedGraphVertex[VP],
  _payload: EP)
  extends UndirectedGraphEdge[VP, EP] {

  def vertices(): (NativeUndirectedGraphVertex[VP], NativeUndirectedGraphVertex[VP]) = (v1, v2)

  def payload(): EP = _payload

  def other(u: NativeUndirectedGraphVertex[VP]): NativeUndirectedGraphVertex[VP] = super.other(u).asInstanceOf[NativeUndirectedGraphVertex[VP]] // TODO cast

}

class NativeUndirectedGraph[VP, EP](
  vps: Seq[VP],
  ef: (Seq[NativeUndirectedGraphVertex[VP]]) => Seq[(NativeUndirectedGraphVertex[VP], NativeUndirectedGraphVertex[VP], EP)])
  extends GenUndirectedGraph[VP, EP] {

  // type V = NativeUndirectedGraphVertex[VP]
  // type E = NativeUndirectedGraphEdge[VP, EP]

  type S = (Seq[NativeUndirectedGraphVertex[VP]], Seq[NativeUndirectedGraphEdge[VP, EP]], Map[NativeUndirectedGraphVertex[VP], Set[NativeUndirectedGraphEdge[VP, EP]]])

  val _vertices: Seq[NativeUndirectedGraphVertex[VP]] = vps.map(new NativeUndirectedGraphVertex(_))

  val _edges: Seq[NativeUndirectedGraphEdge[VP, EP]] = ef(_vertices).map({
    case (vi, vj, ep) => new NativeUndirectedGraphEdge(vi, vj, ep)
  })

  lazy val vertexSet = _vertices.toSet
  lazy val edgeSet = _edges.toSet

  lazy val vertex2edges: Map[NativeUndirectedGraphVertex[VP], Set[NativeUndirectedGraphEdge[VP, EP]]] =
    immutable.Map[NativeUndirectedGraphVertex[VP], Set[NativeUndirectedGraphEdge[VP, EP]]]().withDefaultValue(Set[NativeUndirectedGraphEdge[VP, EP]]())

  def storage(): S = (_vertices, _edges, vertex2edges)
  def vertices(): Set[NativeUndirectedGraphVertex[VP]] = vertexSet
  def edges(): Set[NativeUndirectedGraphEdge[VP, EP]] = edgeSet

  def size(): Int = _vertices.size

  def unlink(e: NativeUndirectedGraphEdge[VP, EP]): NativeUndirectedGraph[VP, EP] = filterEdge(xxx)

  def unlink(v1: NativeUndirectedGraphVertex[VP], v2: NativeUndirectedGraphVertex[VP]): NativeUndirectedGraph[VP, EP] = 4

  def areNeighbors(v1: NativeUndirectedGraphVertex[VP], v2: NativeUndirectedGraphVertex[VP]): Boolean = edges(v1).exists(_.connects(v1, v2))

  def forceClique(vs: GenTraversable[NativeUndirectedGraphVertex[VP]], payload: (NativeUndirectedGraphVertex[VP], NativeUndirectedGraphVertex[VP]) => EP): NativeUndirectedGraph[VP, EP] =
    NativeUndirectedGraph(vps, bar)

  // vs.doubles().filter({ case (vi, vj) => !areNeighbors(vi, vj) }).map({ case (vi, vj) => edge(vi, vj, payload(vi, vj)) })

  override def isClique(vs: GenTraversable[NativeUndirectedGraphVertex[VP]]): Boolean = (for {
    vi <- vs
    vj <- vs
  } yield {
    (vi == vj) || areNeighbors(vi, vj)
  }).forall(b => b) // TODO use 'doubles' and ∀

  def degree(v: NativeUndirectedGraphVertex[VP]): Int = vertex2edges.get(v).map(_.size).getOrElse(0)

  def edges(v: NativeUndirectedGraphVertex[VP]): Set[NativeUndirectedGraphEdge[VP, EP]] = vertex2edges(v)

  def neighbors(v: NativeUndirectedGraphVertex[VP]): Set[NativeUndirectedGraphVertex[VP]] =
    vertex2edges(v).map(_.other(v))

  def delete(v: NativeUndirectedGraphVertex[VP]): NativeUndirectedGraph[VP, EP] =
    NativeUndirectedGraph(vps.filter(_ != v), ef)

  // a "leaf" is vertex with only one neighbor
  def firstLeafOtherThan(r: NativeUndirectedGraphVertex[VP]): Option[NativeUndirectedGraphVertex[VP]] = vertices.find({ v => neighbors(v).size == 1 && !v.equals(r) })

  def eliminate(v: NativeUndirectedGraphVertex[VP], payload: (NativeUndirectedGraphVertex[VP], NativeUndirectedGraphVertex[VP]) => EP): NativeUndirectedGraph[VP, EP] = {
    // "decompositions" page 3 (Definition 3, Section 9.3)
    // turn the neighbors of v into a clique
    null // TODO: remove v and all edges it touches, then force clique of all of v's neighbors
  }

  def eliminate(vs: List[NativeUndirectedGraphVertex[VP]], payload: (NativeUndirectedGraphVertex[VP], NativeUndirectedGraphVertex[VP]) => EP): NativeUndirectedGraph[VP, EP] =
    vs.map(eliminate(_, payload))

  /**
   * dijkstra
   *
   * Modelled after psuedocode on Wikipedia:
   *
   *   http://en.wikipedia.org/wiki/Dijkstra's_algorithm
   */
  /*
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
  */

}
