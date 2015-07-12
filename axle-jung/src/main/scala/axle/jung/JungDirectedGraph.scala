package axle.jung

import scala.collection.JavaConverters.asScalaBufferConverter
import scala.collection.JavaConverters.collectionAsScalaIterableConverter

import axle.algebra.DirectedGraph
import axle.enrichIndexedSeq
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath
import edu.uci.ics.jung.graph.DirectedSparseGraph
import spire.algebra.Eq
import spire.implicits.IntAlgebra
import spire.implicits.eqOps

// Note: ES includes the vertices in order to provide uniqueness for jung
// This could also be accomplished by making Edge not a case class

object JungDirectedGraph {

  implicit def directedGraphJung: DirectedGraph[DirectedSparseGraph] = new DirectedGraph[DirectedSparseGraph] {

    def make[V, E](vertices: Seq[V], ef: Seq[V] => Seq[(V, V, E)]): DirectedSparseGraph[V, E] = {

      val jdsg = new DirectedSparseGraph[V, E]

      vertices foreach { jdsg.addVertex } // TODO check return value

      ef(vertices) foreach {
        case (vi, vj, e) =>
          jdsg.addEdge(e, vi, vj) // TODO check return value
      }

      jdsg
    }

    def vertices[V, E](jdsg: DirectedSparseGraph[V, E]): Iterable[V] =
      jdsg.getVertices.asScala

    def edges[V, E](jdsg: DirectedSparseGraph[V, E]): Iterable[E] =
      jdsg.getEdges.asScala

    def size[V, E](jdsg: DirectedSparseGraph[V, E]): Int =
      jdsg.getVertexCount

    // TODO findVertex needs an index
    def findVertex[V, E](jdsg: DirectedSparseGraph[V, E], f: V => Boolean): Option[V] =
      vertices(jdsg).find(f)

    def filterEdges[V, E](jdsg: DirectedSparseGraph[V, E], f: E => Boolean): DirectedSparseGraph[V, E] =
      make(
        vertices(jdsg).toSeq,
        (es: Seq[V]) => edges(jdsg).filter(f).toList.map({ case e => (e.from, e.to, e) }))

    def areNeighbors[V: Eq, E](jdg: DirectedSparseGraph[V, E], v1: V, v2: V): Boolean =
      edgesTouching(jdg, v1).exists(edge => connects(jdg, edge, v1, v2))

    def isClique[V: Eq, E](jdsg: DirectedSparseGraph[V, E], vs: collection.GenTraversable[V]): Boolean =
      (for {
        vi <- vs
        vj <- vs
      } yield {
        (vi === vj) || areNeighbors(jdsg, vi, vj)
      }).forall(identity)

    def forceClique[V: Eq: Manifest, E](jdsg: DirectedSparseGraph[V, E], among: Set[V], payload: (V, V) => E): DirectedSparseGraph[V, E] = {

      val cliqued = (newVs: Seq[V]) => {

        val old2new: Map[V, V] = ??? // TODO _vertices.zip(newVs).toMap

        val newEdges = among.toVector.permutations(2)
          .map({ a => (a(0), a(1)) })
          .collect({
            case (vi: V, vj: V) if !areNeighbors(jdsg, vi, vj) =>
              val newVi = old2new(vi)
              val newVj = old2new(vj)
              (newVi, newVj, payload(newVi, newVj))
          })

        ??? // ef(newVs) ++ newEdges
      }

      make(vertices(jdsg).toList, cliqued)
    }

    def degree[V, E](jdsg: DirectedSparseGraph[V, E], v: V): Int =
      edgesTouching(jdsg, v).size

    def edgesTouching[V, E](jdsg: DirectedSparseGraph[V, E], v: V): Set[E] =
      jdsg.getIncidentEdges(v).asScala.toSet

    def neighbors[V, E](jdsg: DirectedSparseGraph[V, E], v: V): Set[V] =
      jdsg.getNeighbors(v).asScala.toSet

    // a "leaf" is vertex with only one neighbor
    def firstLeafOtherThan[V: Eq, E](jdsg: DirectedSparseGraph[V, E], r: V): Option[V] =
      vertices(jdsg).find(v => neighbors(jdsg, v).size === 1 && (!(v === r)))

    /**
     * "decompositions" page 3 (Definition 3, Section 9.3)
     * turn the neighbors of v into a clique
     */

    def eliminate[V, E](jdsg: DirectedSparseGraph[V, E], v: V, payload: (V, V) => E): DirectedSparseGraph[V, E] = {

      // TODO
      //    val vs = neighbors(v)
      //    makeFunctional.jungGraph.removeVertex(v)
      //    forceClique(vs, payload)
      ???
    }

    def other[V: Eq, E](jdsg: DirectedSparseGraph[V, E], edge: E, u: V): V =
      u match {
        case _ if (u === edge.from) => edge.to
        case _ if (u === edge.to)   => edge.from
        case _                      => throw new Exception("can't find 'other' of a vertex that isn't on the edge itself")
      }

    def connects[V: Eq, E](jdsg: DirectedSparseGraph[V, E], edge: E, a1: V, a2: V): Boolean =
      (a1 === edge.from && a2 === edge.to) || (a2 === edge.from && a1 === edge.to)

    def map[V, E, NV, NE](jdsg: DirectedSparseGraph[V, E], vpf: V => NV, epf: E => NE): DirectedSparseGraph[NV, NE] =
      make(vertices(jdsg).map(vpf).toList,
        (newVs: Seq[NV]) => ???)

    def leaves[V: Eq, E](jdsg: DirectedSparseGraph[V, E]): Set[V] =
      vertices(jdsg).filter(v => isLeaf(jdsg, v)).toSet

    def precedes[V, E](jdsg: DirectedSparseGraph[V, E], v1: V, v2: V): Boolean =
      predecessors(jdsg, v2).contains(v1)

    def predecessors[V, E](jdsg: DirectedSparseGraph[V, E], v: V): Set[V] =
      jdsg.getPredecessors(v).asScala.toSet

    def isLeaf[V, E](jdsg: DirectedSparseGraph[V, E], v: V): Boolean =
      jdsg.getSuccessorCount(v) === 0

    def successors[V, E](jdsg: DirectedSparseGraph[V, E], v: V): Set[V] =
      jdsg.getSuccessors(v).asScala.toSet

    def outputEdgesOf[V, E](jdsg: DirectedSparseGraph[V, E], v: V): Set[V] =
      jdsg.getOutEdges(v).asScala.toSet

    def descendantsIntersectsSet[V, E](jdsg: DirectedSparseGraph[V, E], v: V, s: Set[V]): Boolean =
      s.contains(v) || s.exists(x => descendantsIntersectsSet(jdsg, x, s))

    def removeInputs[V, E](jdsg: DirectedSparseGraph[V, E], to: Set[V]): DirectedSparseGraph[V, E] =
      filterEdges(jdsg, edge => !to.contains(edge.to))

    def removeOutputs[V, E](jdsg: DirectedSparseGraph[V, E], from: Set[V]): DirectedSparseGraph[V, E] =
      filterEdges(jdsg, edge => !from.contains(edge.from))

    def moralGraph[V, E](jdsg: DirectedSparseGraph[V, E]): Boolean =
      ???

    def isAcyclic[V, E](jdsg: DirectedSparseGraph[V, E]): Boolean =
      ???

    def shortestPath[V: Eq, E](jdsg: DirectedSparseGraph[V, E], source: V, goal: V): Option[List[V]] =
      if (source === goal) {
        Some(Nil)
      } else {
        Option((new DijkstraShortestPath(jdsg)).getPath(source, goal)) flatMap { path =>
          if (path.size === 0)
            None
          else
            Some(path.asScala.toList)
        }
      }

  }
}
