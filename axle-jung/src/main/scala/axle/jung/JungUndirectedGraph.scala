package axle.jung

import scala.collection.JavaConverters.collectionAsScalaIterableConverter

import axle.algebra.UndirectedGraph
import axle.enrichIndexedSeq
import edu.uci.ics.jung.graph.UndirectedSparseGraph
import spire.algebra.Eq
import spire.implicits.IntAlgebra
import spire.implicits.eqOps

object JungUndirectedGraph {

  implicit def uJung: UndirectedGraph[UndirectedSparseGraph] = new UndirectedGraph[UndirectedSparseGraph] {

    def make[V, E](vertices: Seq[V], ef: Seq[V] => Seq[(V, V, E)]): UndirectedSparseGraph[V, E] = {

      val jusg = new UndirectedSparseGraph[V, E]

      vertices foreach { jusg.addVertex } // TODO check return value

      ef(vertices) foreach {
        case (vi, vj, e) =>
          jusg.addEdge(e, vi, vj) // TODO check return value
      }

      jusg
    }

    def vertices[V, E](jusg: UndirectedSparseGraph[V, E]): Iterable[V] =
      jusg.getVertices.asScala

    def edges[V, E](jusg: UndirectedSparseGraph[V, E]): Iterable[E] =
      jusg.getEdges.asScala

    def size[V, E](jusg: UndirectedSparseGraph[V, E]): Int =
      jusg.getVertexCount

    // TODO findVertex needs an index
    def findVertex[V, E](jusg: UndirectedSparseGraph[V, E], f: V => Boolean): Option[V] =
      vertices(jusg).find(f)

    def filterEdges[V, E](jusg: UndirectedSparseGraph[V, E], f: E => Boolean): UndirectedSparseGraph[V, E] =
      make(
        vertices(jusg).toSeq,
        (es: Seq[V]) => edges(jusg).filter(f).toList.map({ case e => (e.vertices._1, e.vertices._2, e) }))

    //  def unlink(e: Edge[ES, E]): UndirectedSparseGraph[V, E] =
    //    filterEdges(t => {
    //      val v1 = e.storage._1
    //      val v2 = e.storage._2
    //      !((v1, v2, e.payload) === t || (v2, v1, e.payload) === t)
    //    })

    //  // UndirectedSparseGraph[V, E]
    //  def unlink(v1: Vertex[V], v2: Vertex[V]) =
    //    filterEdges(e => (e._1 === v1 && e._2 === v2) || (e._2 === v1 && e._1 === v2))

    def areNeighbors[V: Eq, E](jug: UndirectedSparseGraph[V, E], v1: V, v2: V): Boolean =
      edgesTouching(jug, v1).exists(edge => connects(jug, edge, v1, v2))

    def isClique[V: Eq, E](jug: UndirectedSparseGraph[V, E], vs: collection.GenTraversable[V]): Boolean =
      (for {
        vi <- vs
        vj <- vs
      } yield {
        (vi === vj) || areNeighbors(jug, vi, vj)
      }).forall(identity)

    def forceClique[V: Eq: Manifest, E](jug: UndirectedSparseGraph[V, E], among: Set[V], payload: (V, V) => E): UndirectedSparseGraph[V, E] = {

      val cliqued = (newVs: Seq[V]) => {

        val old2new: Map[V, V] = ??? // TODO _vertices.zip(newVs).toMap

        val newEdges = among.toVector.permutations(2)
          .map({ a => (a(0), a(1)) })
          .collect({
            case (vi: V, vj: V) if !areNeighbors(jug, vi, vj) =>
              val newVi = old2new(vi)
              val newVj = old2new(vj)
              (newVi, newVj, payload(newVi, newVj))
          })

        ??? // ef(newVs) ++ newEdges
      }

      make(vertices(jug).toList, cliqued)
    }

    def degree[V, E](jusg: UndirectedSparseGraph[V, E], v: V): Int =
      edgesTouching(jusg, v).size

    def edgesTouching[V, E](jusg: UndirectedSparseGraph[V, E], v: V): Set[E] =
      jusg.getIncidentEdges(v).asScala.toSet

    def neighbors[V, E](jusg: UndirectedSparseGraph[V, E], v: V): Set[V] =
      jusg.getNeighbors(v).asScala.toSet

    //  def delete(v: Vertex[V]): UndirectedSparseGraph[V, E] =
    //    UndirectedSparseGraph(vertices.toSeq.filter(_ != v).map(_.payload), ef)

    // a "leaf" is vertex with only one neighbor
    def firstLeafOtherThan[V: Eq, E](jug: UndirectedSparseGraph[V, E], r: V): Option[V] =
      vertices(jug).find(v => neighbors(jug, v).size === 1 && (!(v === r)))

    /**
     * "decompositions" page 3 (Definition 3, Section 9.3)
     * turn the neighbors of v into a clique
     */

    def eliminate[V, E](jug: UndirectedSparseGraph[V, E], v: V, payload: (V, V) => E): UndirectedSparseGraph[V, E] = {

      // TODO
      //    val vs = neighbors(v)
      //    makeFunctional.jungGraph.removeVertex(v)
      //    forceClique(vs, payload)
      ???
    }

    def other[V: Eq, E](jusg: UndirectedSparseGraph[V, E], edge: E, u: V): V = {
      val (v1, v2) = edge.vertices
      u match {
        case _ if (u === v1) => v2
        case _ if (u === v2) => v1
        case _               => throw new Exception("can't find 'other' of a vertex that isn't on the edge itself")
      }
    }

    def connects[V: Eq, E](jusg: UndirectedSparseGraph[V, E], edge: E, a1: V, a2: V): Boolean = {
      val (v1, v2) = edge.vertices
      (v1 === a1 && v2 === a2) || (v2 === a1 && v1 === a2)
    }

    def map[V, E, NV, NE](jusg: UndirectedSparseGraph[V, E], vpf: V => NV, epf: E => NE): UndirectedSparseGraph[NV, NE] =
      make(vertices(jusg).map(vpf).toList,
        (newVs: Seq[NV]) => ???)

  }
}
