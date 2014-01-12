package axle.graph

import axle._
import spire.algebra._
import spire.implicits._

case class NativeUndirectedGraph[VP: Manifest: Eq, EP: Eq](
  vps: Seq[VP],
  ef: Seq[Vertex[VP]] => Seq[(Vertex[VP], Vertex[VP], EP)])
  extends UndirectedGraph[VP, EP] {

  type G[VP, EP] = NativeUndirectedGraph[VP, EP]
  type ES = (Vertex[VP], Vertex[VP], EP)

  val edgePayloadFunction = (es: ES) => es._3

  val _vertices = vps.map(Vertex(_))

  val _edges = ef(_vertices) map {
    case (vi, vj, ep) => Edge((vi, vj, ep), edgePayloadFunction)
  }

  lazy val vertexSet = _vertices.toSet
  lazy val edgeSet = _edges.toSet

  lazy val vertex2edges: Map[Vertex[VP], Set[Edge[ES, EP]]] =
    _edges
      .flatMap(e => {
        val (vi, vj) = vertices(e)
        Vector((vi, e), (vj, e))
      })
      .groupBy(_._1)
      .map(kv => (kv._1, kv._2.map(_._2).toSet))
      .withDefaultValue(Set())

  def storage: (Seq[Vertex[VP]], Seq[Edge[ES, EP]], Map[Vertex[VP], Set[Edge[ES, EP]]]) =
    (_vertices, _edges, vertex2edges)

  def vertexPayloads: Seq[VP] = vps

  def edgeFunction: Seq[Vertex[VP]] => Seq[ES] = ef

  def vertices: Set[Vertex[VP]] = vertexSet

  def allEdges: Set[Edge[ES, EP]] = edgeSet

  def size: Int = _vertices.size

  def vertices(edge: Edge[ES, EP]): (Vertex[VP], Vertex[VP]) = (edge.storage._1, edge.storage._2)

  def findEdge(vi: Vertex[VP], vj: Vertex[VP]): Option[Edge[ES, EP]] =
    _edges.find(e => (vertices(e) === (vi, vj)) || (vertices(e) === (vj, vi))) // Note: no matching on payload

  // TODO findVertex needs an index
  def findVertex(f: Vertex[VP] => Boolean): Option[Vertex[VP]] = _vertices.find(f)

  def filterEdges(f: ES => Boolean): NativeUndirectedGraph[VP, EP] =
    NativeUndirectedGraph(vps, ((es: Seq[ES]) => es.filter(f)).compose(ef))

  def unlink(e: Edge[ES, EP]): NativeUndirectedGraph[VP, EP] = {
    val filter = (es: Seq[(Vertex[VP], Vertex[VP], EP)]) => es.zip(_edges).filter({
      case ((vi, vj, ep), oldEdge) => oldEdge != e
    }).map(_._1)
    NativeUndirectedGraph(vps, filter.compose(ef))
  }

  def unlink(vi: Vertex[VP], vj: Vertex[VP]): NativeUndirectedGraph[VP, EP] = findEdge(vi, vj).map(unlink).getOrElse(this)

  def areNeighbors(vi: Vertex[VP], vj: Vertex[VP]): Boolean = edgesTouching(vi).exists(connects(_, vi, vj))

  def isClique(vs: collection.GenTraversable[Vertex[VP]]): Boolean =
    (for {
      vi <- vs
      vj <- vs
    } yield {
      (vi === vj) || areNeighbors(vi, vj)
    }).forall(identity)

  def forceClique(among: Set[Vertex[VP]], payload: (Vertex[VP], Vertex[VP]) => EP): NativeUndirectedGraph[VP, EP] = {

    val cliqued: Seq[Vertex[VP]] => Seq[(Vertex[VP], Vertex[VP], EP)] = (newVs: Seq[Vertex[VP]]) => {

      val old2new = _vertices.zip(newVs).toMap

      val newEdges = among.toIndexedSeq.permutations(2).toList
        .map({ a => (a(0), a(1)) })
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

    NativeUndirectedGraph(vps, cliqued) // TODO: phrase in terms of mapEdges?
  }

  def isClique(vs: IndexedSeq[Vertex[VP]]): Boolean =
    vs.permutations(2).∀({ a => areNeighbors(a(0), a(1)) })

  def degree(v: Vertex[VP]): Int = vertex2edges.get(v).map(_.size).getOrElse(0)

  def edgesTouching(v: Vertex[VP]): Set[Edge[ES, EP]] = vertex2edges.get(v).getOrElse(Set())

  def neighbors(v: Vertex[VP]): Set[Vertex[VP]] =
    vertex2edges.get(v).map(edges => edges.map(edge => other(edge, v)))
      .getOrElse(Set[Vertex[VP]]())

  def delete(v: Vertex[VP]): NativeUndirectedGraph[VP, EP] = NativeUndirectedGraph(vps.filter(_ != v), ef)

  // a "leaf" is vertex with only one neighbor
  def firstLeafOtherThan(r: Vertex[VP]): Option[Vertex[VP]] = vertices.find({ v => neighbors(v).size === 1 && (!(v === r)) })

  def eliminate(v: Vertex[VP], payload: (Vertex[VP], Vertex[VP]) => EP): NativeUndirectedGraph[VP, EP] = {
    // "decompositions" page 3 (Definition 3, Section 9.3)
    // turn the neighbors of v into a clique
    ??? // TODO: remove v and all edges it touches, then force clique of all of v's neighbors
  }

  def other(edge: Edge[ES, EP], u: Vertex[VP]): Vertex[VP] = {
    val (v1, v2) = vertices(edge)
    u match {
      case _ if (u === v1) => v2
      case _ if (u === v2) => v1
      case _ => throw new Exception("can't find 'other' of a vertex that isn't on the edge itself")
    }
  }
  
  def connects(edge: Edge[ES, EP], a1: Vertex[VP], a2: Vertex[VP]): Boolean = {
    val (v1, v2) = vertices(edge)
    (v1 === a1 && v2 === a2) || (v2 === a1 && v1 === a2)
  }

  def map[NVP: Manifest: Eq, NEP: Eq](vpf: VP => NVP, epf: EP => NEP): NativeUndirectedGraph[NVP, NEP] =
    NativeUndirectedGraph(vps.map(vpf),
      (newVs: Seq[Vertex[NVP]]) =>
        ef(_vertices).map({
          case (vi, vj, ep) => (Vertex(vpf(vi.payload)), Vertex(vpf(vj.payload)), epf(ep))
        }))

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
  //    val dist = Map[V, Int]()
  //    val previous = Map[V, V]()
  //    vertices foreach { v =>
  //      dist(v) = Int.MaxValue // Unknown distance function from source to v
  //    }
  //
  //    dist(source) = 0 // Distance from source to source
  //    val Q = Set[V]() ++ vertices // All nodes in the graph are unoptimized - thus are in Q
  //    val broken = false
  //    while (Q.size > 0 && !broken) {
  //      val u = Q.minBy(dist(_)) // Start node in first case
  //      Q -= u
  //      if (u === target) {
  //        val S = List[V]()
  //        val u = target
  //        while (previous.contains(u)) {
  //          S = u :: S
  //          u = previous(u)
  //        }
  //      }
  //      if (dist(u) === Int.MaxValue) {
  //        broken = true // all remaining vertices are inaccessible from source
  //      } else {
  //        neighbors(u) foreach { v => // where v has not yet been removed from Q
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
