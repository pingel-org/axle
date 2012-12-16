package axle.graph

import collection._
import axle._

class NativeUndirectedGraph[VP, EP](vps: Seq[VP], ef: Seq[Vertex[VP]] => Seq[(Vertex[VP], Vertex[VP], EP)])
  extends UndirectedGraph[VP, EP] {

  type G[VP, EP] = NativeUndirectedGraph[VP, EP]

  val _vertices = vps.map(Vertex(_))

  val _edges = ef(_vertices).map({
    case (vi, vj, ep) => Edge(vi, vj, ep)
  })

  lazy val vertexSet = _vertices.toSet
  lazy val edgeSet = _edges.toSet

  lazy val vertex2edges: Map[Vertex[VP], Set[Edge[EP]]] =
    _edges
      .flatMap(e => {
        val (vi, vj): (Vertex[VP], Vertex[VP]) = e.vertices()
        Vector((vi, e), (vj, e))
      }).groupBy(_._1).map(kv => (kv._1, kv._2.map(_._2).toSet))
      .withDefaultValue(Set())

  def storage() = (_vertices, _edges, vertex2edges)

  def vertices() = vertexSet

  def allEdges() = edgeSet

  def size(): Int = _vertices.size

  def findEdge(vi: Vertex[VP], vj: Vertex[VP]): Option[Edge[EP]] =
    _edges.find(e => (e.vertices == (vi, vj)) || (e.vertices == (vj, vi))) // Note: no matching on payload

  // TODO findVertex needs an index
  def findVertex(f: Vertex[VP] => Boolean): Option[Vertex[VP]] = _vertices.find(f(_))

  def filterEdges(f: ((Vertex[VP], Vertex[VP], EP)) => Boolean): NativeUndirectedGraph[VP, EP] = {
    val filter = (es: Seq[(Vertex[VP], Vertex[VP], EP)]) => es.filter(f(_))
    NativeUndirectedGraph(vps, filter.compose(ef))
  }

  def unlink(e: Edge[EP]): NativeUndirectedGraph[VP, EP] = {
    val filter = (es: Seq[(Vertex[VP], Vertex[VP], EP)]) => es.zip(_edges).filter({
      case ((vi, vj, ep), oldEdge) => oldEdge != e
    }).map(_._1)
    NativeUndirectedGraph(vps, filter.compose(ef))
  }

  def unlink(vi: Vertex[VP], vj: Vertex[VP]): NativeUndirectedGraph[VP, EP] = findEdge(vi, vj).map(unlink(_)).getOrElse(this)

  def areNeighbors(vi: Vertex[VP], vj: Vertex[VP]): Boolean = edges(vi).exists(_.connects(vi, vj))

  def forceClique(among: Set[Vertex[VP]], payload: (Vertex[VP], Vertex[VP]) => EP): NativeUndirectedGraph[VP, EP] = {

    val cliqued: Seq[Vertex[VP]] => Seq[(Vertex[VP], Vertex[VP], EP)] = (newVs: Seq[Vertex[VP]]) => {

      val old2new = _vertices.zip(newVs).toMap

      val newEdges = among.toIndexedSeq.permutations(2)
        .map({ case vi :: vj :: Nil => (vi, vj) })
        .filter({ case (vi, vj) => !areNeighbors(vi, vj) })
        .map({
          case (vi, vj) => {
            val newVi = old2new(vi)
            val newVj = old2new(vj)
            (newVi, newVj, payload(newVi, newVj))
          }
        })

      ef(newVs) ++ newEdges
    }

    NativeUndirectedGraph(vps, cliqued(_)) // TODO: phrase in terms of mapEdges?
  }

  def isClique(vs: IndexedSeq[Vertex[VP]]): Boolean =
    vs.permutations(2).∀({ case vi :: vj :: Nil => areNeighbors(vi, vj) })

  def degree(v: Vertex[VP]): Int = vertex2edges.get(v).map(_.size).getOrElse(0)

  def edges(v: Vertex[VP]): Set[Edge[EP]] = vertex2edges.get(v).getOrElse(Set())

  def neighbors(v: Vertex[VP]): Set[Vertex[VP]] =
    vertex2edges.get(v).map(edges => edges.map(edge => edge.other(v)))
      .getOrElse(Set[Vertex[VP]]())

  def delete(v: Vertex[VP]): NativeUndirectedGraph[VP, EP] = NativeUndirectedGraph(vps.filter(_ != v), ef)

  // a "leaf" is vertex with only one neighbor
  def firstLeafOtherThan(r: Vertex[VP]): Option[Vertex[VP]] = vertices.find({ v => neighbors(v).size == 1 && !v.equals(r) })

  def eliminate(v: Vertex[VP], payload: (Vertex[VP], Vertex[VP]) => EP): NativeUndirectedGraph[VP, EP] = {
    // "decompositions" page 3 (Definition 3, Section 9.3)
    // turn the neighbors of v into a clique
    null // TODO: remove v and all edges it touches, then force clique of all of v's neighbors
  }

  /**
   * dijkstra
   *
   * Modelled after psuedocode on Wikipedia:
   *
   *   http://en.wikipedia.org/wiki/Dijkstra's_algorithm
   */
  //  def dijkstra(source: V, target: V): Map[V, Int] = {
  //
  //    def edgeCost(v1: V, v2: V): Int = 1 // TODO: generalize
  //
  //    val undefined = -1
  //    val dist = mutable.Map[V, Int]()
  //    val previous = mutable.Map[V, V]()
  //    for (v <- vertices) {
  //      dist(v) = Int.MaxValue // Unknown distance function from source to v
  //    }
  //
  //    dist(source) = 0 // Distance from source to source
  //    val Q = mutable.Set[V]() ++ vertices // All nodes in the graph are unoptimized - thus are in Q
  //    var broken = false
  //    while (Q.size > 0 && !broken) {
  //      val u = Q.minBy(dist(_)) // Start node in first case
  //      Q -= u
  //      if (u == target) {
  //        var S = List[V]()
  //        var u = target
  //        while (previous.contains(u)) {
  //          S = u :: S
  //          u = previous(u)
  //        }
  //      }
  //      if (dist(u) == Int.MaxValue) {
  //        broken = true // all remaining vertices are inaccessible from source
  //      } else {
  //        for (v <- neighbors(u)) { // where v has not yet been removed from Q
  //          val alt = dist(u) + edgeCost(u, v)
  //          if (alt < dist(v)) { // Relax (u,v,a)
  //            dist(v) = alt
  //            previous(v) = u
  //            // TODO decrease - key v in Q // Reorder v in the Queue
  //          }
  //        }
  //      }
  //    }
  //    dist
  //  }

}

//class Edge[VP, EP](v1: Vertex[VP], v2: Vertex[VP], ep: EP)
//  extends UndirectedGraphEdge[VP, EP] {
//
//  type V[VP] = Vertex[VP]
//  
//  def vertices(): (Vertex[VP], Vertex[VP]) = (v1, v2)
//
//  def payload(): EP = ep
//
//  override def other(u: Vertex[VP]): Vertex[VP] = super.other(u).asInstanceOf[Vertex[VP]]
//
//}

object NativeUndirectedGraph {

  // type S = (Seq[Vertex[VP]], Seq[Edge[VP, EP]], Map[Vertex[VP], Set[Edge[VP, EP]]])

  def apply[VP, EP](vps: Seq[VP], ef: Seq[Vertex[VP]] => Seq[(Vertex[VP], Vertex[VP], EP)]) = new NativeUndirectedGraph(vps, ef)
}

