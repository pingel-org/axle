package axle.jung

import scala.collection.JavaConverters.asScalaBufferConverter
import scala.collection.JavaConverters.collectionAsScalaIterableConverter

import axle.algebra.DirectedEdge
import axle.algebra.DirectedGraph
import axle.algebra.Vertex
import axle.enrichIndexedSeq
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath
import edu.uci.ics.jung.graph.DirectedSparseGraph
import spire.algebra.Eq
import spire.implicits.IntAlgebra
import spire.implicits.eqOps

case class JungDirectedGraph[VP, EP](
  jdsg: DirectedSparseGraph[Vertex[VP], JungDirectedGraphEdge[VP, EP]])

case class JungDirectedGraphEdge[VP, EP](
  from: Vertex[VP],
  to: Vertex[VP],
  payload: EP) extends DirectedEdge[VP, EP]

// Note: ES includes the vertices in order to provide uniqueness for jung
// This could also be accomplished by making Edge not a case class

object JungDirectedGraph {

  implicit def directedGraphJung: DirectedGraph[JungDirectedGraph] = new DirectedGraph[JungDirectedGraph] {

    def make[VP, EP](vps: Seq[VP], ef: Seq[Vertex[VP]] => Seq[(Vertex[VP], Vertex[VP], EP)]): JungDirectedGraph[VP, EP] = {

      val jdsg = new DirectedSparseGraph[Vertex[VP], JungDirectedGraphEdge[VP, EP]]

      val vertices = vps map { Vertex(_) }

      vertices foreach { jdsg.addVertex } // TODO check return value

      ef(vertices) foreach {
        case (vi, vj, ep) =>
          jdsg.addEdge(JungDirectedGraphEdge(vi, vj, ep), vi, vj) // TODO check return value
      }

      JungDirectedGraph(jdsg)
    }

    def vertices[VP, EP](jdg: JungDirectedGraph[VP, EP]): Iterable[Vertex[VP]] =
      jdg.jdsg.getVertices.asScala

    def edges[VP, EP](jdg: JungDirectedGraph[VP, EP]): Iterable[DirectedEdge[VP, EP]] =
      jdg.jdsg.getEdges.asScala

    def size[VP, EP](jdg: JungDirectedGraph[VP, EP]): Int =
      jdg.jdsg.getVertexCount

    // TODO findVertex needs an index
    def findVertex[VP, EP](jdg: JungDirectedGraph[VP, EP], f: Vertex[VP] => Boolean): Option[Vertex[VP]] =
      vertices(jdg).find(f)

    def filterEdges[VP, EP](jdg: JungDirectedGraph[VP, EP], f: DirectedEdge[VP, EP] => Boolean): JungDirectedGraph[VP, EP] =
      make(
        vertices(jdg).map(_.payload).toSeq,
        (es: Seq[Vertex[VP]]) => edges(jdg).filter(f).toList.map({ case e => (e.from, e.to, e.payload) }))

    def areNeighbors[VP: Eq, EP](jdg: JungDirectedGraph[VP, EP], v1: Vertex[VP], v2: Vertex[VP]): Boolean =
      edgesTouching(jdg, v1).exists(edge => connects(jdg, edge, v1, v2))

    def isClique[VP: Eq, EP](jdg: JungDirectedGraph[VP, EP], vs: collection.GenTraversable[Vertex[VP]]): Boolean =
      (for {
        vi <- vs
        vj <- vs
      } yield {
        (vi === vj) || areNeighbors(jdg, vi, vj)
      }).forall(identity)

    def forceClique[VP: Eq: Manifest, EP](jdg: JungDirectedGraph[VP, EP], among: Set[Vertex[VP]], payload: (Vertex[VP], Vertex[VP]) => EP): JungDirectedGraph[VP, EP] = {

      val cliqued = (newVs: Seq[Vertex[VP]]) => {

        val old2new: Map[Vertex[VP], Vertex[VP]] = ??? // TODO _vertices.zip(newVs).toMap

        val newEdges = among.toVector.permutations(2)
          .map({ a => (a(0), a(1)) })
          .collect({
            case (vi: Vertex[VP], vj: Vertex[VP]) if !areNeighbors(jdg, vi, vj) =>
              val newVi = old2new(vi)
              val newVj = old2new(vj)
              (newVi, newVj, payload(newVi, newVj))
          })

        ??? // ef(newVs) ++ newEdges
      }

      make(vertices(jdg).map(_.payload).toList, cliqued)
    }

    def degree[VP, EP](jdg: JungDirectedGraph[VP, EP], v: Vertex[VP]): Int =
      edgesTouching(jdg, v).size

    def edgesTouching[VP, EP](jdg: JungDirectedGraph[VP, EP], v: Vertex[VP]): Set[DirectedEdge[VP, EP]] =
      jdg.jdsg.getIncidentEdges(v).asScala.toSet

    def neighbors[VP, EP](jdg: JungDirectedGraph[VP, EP], v: Vertex[VP]): Set[Vertex[VP]] =
      jdg.jdsg.getNeighbors(v).asScala.toSet

    // a "leaf" is vertex with only one neighbor
    def firstLeafOtherThan[VP: Eq, EP](jdg: JungDirectedGraph[VP, EP], r: Vertex[VP]): Option[Vertex[VP]] =
      vertices(jdg).find(v => neighbors(jdg, v).size === 1 && (!(v === r)))

    /**
     * "decompositions" page 3 (Definition 3, Section 9.3)
     * turn the neighbors of v into a clique
     */

    def eliminate[VP, EP](jdg: JungDirectedGraph[VP, EP], v: Vertex[VP], payload: (Vertex[VP], Vertex[VP]) => EP): JungDirectedGraph[VP, EP] = {

      // TODO
      //    val vs = neighbors(v)
      //    makeFunctional.jungGraph.removeVertex(v)
      //    forceClique(vs, payload)
      ???
    }

    def other[VP: Eq, EP](jdg: JungDirectedGraph[VP, EP], edge: DirectedEdge[VP, EP], u: Vertex[VP]): Vertex[VP] = {
      u match {
        case _ if (u === edge.from) => edge.to
        case _ if (u === edge.to)   => edge.from
        case _                      => throw new Exception("can't find 'other' of a vertex that isn't on the edge itself")
      }
    }

    def connects[VP: Eq, EP](jdg: JungDirectedGraph[VP, EP], edge: DirectedEdge[VP, EP], a1: Vertex[VP], a2: Vertex[VP]): Boolean =
      (a1 === edge.from && a2 === edge.to) || (a2 === edge.from && a1 === edge.to)

    def map[VP, EP, NVP, NEP](jdg: JungDirectedGraph[VP, EP], vpf: VP => NVP, epf: EP => NEP): JungDirectedGraph[NVP, NEP] =
      make(vertices(jdg).map(v => vpf(v.payload)).toList,
        (newVs: Seq[Vertex[NVP]]) => ???)

    def leaves[VP: Eq, EP](jdg: JungDirectedGraph[VP, EP]): Set[Vertex[VP]] =
      vertices(jdg).filter(v => isLeaf(jdg, v)).toSet

    def precedes[VP, EP](jdg: JungDirectedGraph[VP, EP], v1: Vertex[VP], v2: Vertex[VP]): Boolean =
      predecessors(jdg, v2).contains(v1)

    def predecessors[VP, EP](jdg: JungDirectedGraph[VP, EP], v: Vertex[VP]): Set[Vertex[VP]] =
      jdg.jdsg.getPredecessors(v).asScala.toSet

    def isLeaf[VP, EP](jdg: JungDirectedGraph[VP, EP], v: Vertex[VP]): Boolean =
      jdg.jdsg.getSuccessorCount(v) === 0

    def successors[VP, EP](jdg: JungDirectedGraph[VP, EP], v: Vertex[VP]): Set[Vertex[VP]] =
      jdg.jdsg.getSuccessors(v).asScala.toSet

    def outputEdgesOf[VP, EP](jdg: JungDirectedGraph[VP, EP], v: Vertex[VP]): Set[DirectedEdge[VP, EP]] =
      jdg.jdsg.getOutEdges(v).asScala.toSet

    def descendantsIntersectsSet[VP, EP](jdg: JungDirectedGraph[VP, EP], v: Vertex[VP], s: Set[Vertex[VP]]): Boolean =
      s.contains(v) || s.exists(x => descendantsIntersectsSet(jdg, x, s))

    def removeInputs[VP, EP](jdg: JungDirectedGraph[VP, EP], to: Set[Vertex[VP]]): JungDirectedGraph[VP, EP] =
      filterEdges(jdg, edge => !to.contains(edge.to))

    def removeOutputs[VP, EP](jdg: JungDirectedGraph[VP, EP], from: Set[Vertex[VP]]): JungDirectedGraph[VP, EP] =
      filterEdges(jdg, edge => !from.contains(edge.from))

    def moralGraph[VP, EP](jdg: JungDirectedGraph[VP, EP]): Boolean =
      ???

    def isAcyclic[VP, EP](jdg: JungDirectedGraph[VP, EP]): Boolean =
      ???

    def shortestPath[VP: Eq, EP](jdg: JungDirectedGraph[VP, EP], source: Vertex[VP], goal: Vertex[VP]): Option[List[DirectedEdge[VP, EP]]] =
      if (source === goal) {
        Some(Nil)
      } else {
        Option((new DijkstraShortestPath(jdg.jdsg)).getPath(source, goal)) flatMap { path =>
          if (path.size === 0)
            None
          else
            Some(path.asScala.toList)
        }
      }

  }
}
