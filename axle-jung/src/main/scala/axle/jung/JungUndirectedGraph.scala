package axle.jung

import scala.collection.JavaConverters.collectionAsScalaIterableConverter

import axle.algebra.UndirectedEdge
import axle.algebra.UndirectedGraph
import axle.algebra.Vertex
import axle.enrichIndexedSeq
import edu.uci.ics.jung.graph.UndirectedSparseGraph
import spire.algebra.Eq
import spire.implicits.IntAlgebra
import spire.implicits.eqOps

case class JungUndirectedGraph[VP, EP](
  jusg: UndirectedSparseGraph[Vertex[VP], JungUndirectedGraphEdge[VP, EP]])

case class JungUndirectedGraphEdge[VP, EP](
  v1: Vertex[VP],
  v2: Vertex[VP],
  payload: EP) extends UndirectedEdge[VP, EP] {

  def vertices = (v1, v2)
}

object JungUndirectedGraph {

  implicit def uJung: UndirectedGraph[JungUndirectedGraph] = new UndirectedGraph[JungUndirectedGraph] {

    def make[VP, EP](vps: Seq[VP], ef: Seq[Vertex[VP]] => Seq[(Vertex[VP], Vertex[VP], EP)]): JungUndirectedGraph[VP, EP] = {

      val jusg = new UndirectedSparseGraph[Vertex[VP], JungUndirectedGraphEdge[VP, EP]]

      val vertices = vps map { Vertex(_) }

      vertices foreach { jusg.addVertex } // TODO check return value

      ef(vertices) foreach {
        case (vi, vj, ep) =>
          jusg.addEdge(JungUndirectedGraphEdge(vi, vj, ep), vi, vj) // TODO check return value
      }

      JungUndirectedGraph(jusg)
    }

    def vertices[VP, EP](jug: JungUndirectedGraph[VP, EP]): Iterable[Vertex[VP]] =
      jug.jusg.getVertices.asScala

    def edges[VP, EP](jug: JungUndirectedGraph[VP, EP]): Iterable[UndirectedEdge[VP, EP]] =
      jug.jusg.getEdges.asScala

    def size[VP, EP](jug: JungUndirectedGraph[VP, EP]): Int =
      jug.jusg.getVertexCount

    // TODO findVertex needs an index
    def findVertex[VP, EP](jug: JungUndirectedGraph[VP, EP], f: Vertex[VP] => Boolean): Option[Vertex[VP]] =
      vertices(jug).find(f)

    def filterEdges[VP, EP](jug: JungUndirectedGraph[VP, EP], f: UndirectedEdge[VP, EP] => Boolean): JungUndirectedGraph[VP, EP] =
      make(
        vertices(jug).map(_.payload).toSeq,
        (es: Seq[Vertex[VP]]) => edges(jug).filter(f).toList.map({ case e => (e.vertices._1, e.vertices._2, e.payload) }))

    //  def unlink(e: Edge[ES, EP]): JungUndirectedGraph[VP, EP] =
    //    filterEdges(t => {
    //      val v1 = e.storage._1
    //      val v2 = e.storage._2
    //      !((v1, v2, e.payload) === t || (v2, v1, e.payload) === t)
    //    })

    //  // JungUndirectedGraph[VP, EP]
    //  def unlink(v1: Vertex[VP], v2: Vertex[VP]) =
    //    filterEdges(e => (e._1 === v1 && e._2 === v2) || (e._2 === v1 && e._1 === v2))

    def areNeighbors[VP: Eq, EP](jug: JungUndirectedGraph[VP, EP], v1: Vertex[VP], v2: Vertex[VP]): Boolean =
      edgesTouching(jug, v1).exists(edge => connects(jug, edge, v1, v2))

    def isClique[VP: Eq, EP](jug: JungUndirectedGraph[VP, EP], vs: collection.GenTraversable[Vertex[VP]]): Boolean =
      (for {
        vi <- vs
        vj <- vs
      } yield {
        (vi === vj) || areNeighbors(jug, vi, vj)
      }).forall(identity)

    def forceClique[VP: Eq: Manifest, EP](jug: JungUndirectedGraph[VP, EP], among: Set[Vertex[VP]], payload: (Vertex[VP], Vertex[VP]) => EP): JungUndirectedGraph[VP, EP] = {

      val cliqued = (newVs: Seq[Vertex[VP]]) => {

        val old2new: Map[Vertex[VP], Vertex[VP]] = ??? // TODO _vertices.zip(newVs).toMap

        val newEdges = among.toVector.permutations(2)
          .map({ a => (a(0), a(1)) })
          .collect({
            case (vi: Vertex[VP], vj: Vertex[VP]) if !areNeighbors(jug, vi, vj) =>
              val newVi = old2new(vi)
              val newVj = old2new(vj)
              (newVi, newVj, payload(newVi, newVj))
          })

        ??? // ef(newVs) ++ newEdges
      }

      make(vertices(jug).map(_.payload).toList, cliqued)
    }

    def degree[VP, EP](jug: JungUndirectedGraph[VP, EP], v: Vertex[VP]): Int =
      edgesTouching(jug, v).size

    def edgesTouching[VP, EP](jug: JungUndirectedGraph[VP, EP], v: Vertex[VP]): Set[UndirectedEdge[VP, EP]] =
      jug.jusg.getIncidentEdges(v).asScala.toSet

    def neighbors[VP, EP](jug: JungUndirectedGraph[VP, EP], v: Vertex[VP]): Set[Vertex[VP]] =
      jug.jusg.getNeighbors(v).asScala.toSet

    //  def delete(v: Vertex[VP]): JungUndirectedGraph[VP, EP] =
    //    JungUndirectedGraph(vertices.toSeq.filter(_ != v).map(_.payload), ef)

    // a "leaf" is vertex with only one neighbor
    def firstLeafOtherThan[VP: Eq, EP](jug: JungUndirectedGraph[VP, EP], r: Vertex[VP]): Option[Vertex[VP]] =
      vertices(jug).find(v => neighbors(jug, v).size === 1 && (!(v === r)))

    /**
     * "decompositions" page 3 (Definition 3, Section 9.3)
     * turn the neighbors of v into a clique
     */

    def eliminate[VP, EP](jug: JungUndirectedGraph[VP, EP], v: Vertex[VP], payload: (Vertex[VP], Vertex[VP]) => EP): JungUndirectedGraph[VP, EP] = {

      // TODO
      //    val vs = neighbors(v)
      //    makeFunctional.jungGraph.removeVertex(v)
      //    forceClique(vs, payload)
      ???
    }

    def other[VP: Eq, EP](jug: JungUndirectedGraph[VP, EP], edge: UndirectedEdge[VP, EP], u: Vertex[VP]): Vertex[VP] = {
      val (v1, v2) = edge.vertices
      u match {
        case _ if (u === v1) => v2
        case _ if (u === v2) => v1
        case _               => throw new Exception("can't find 'other' of a vertex that isn't on the edge itself")
      }
    }

    def connects[VP: Eq, EP](jug: JungUndirectedGraph[VP, EP], edge: UndirectedEdge[VP, EP], a1: Vertex[VP], a2: Vertex[VP]): Boolean = {
      val (v1, v2) = edge.vertices
      (v1 === a1 && v2 === a2) || (v2 === a1 && v1 === a2)
    }

    def map[VP, EP, NVP, NEP](jug: JungUndirectedGraph[VP, EP], vpf: VP => NVP, epf: EP => NEP): JungUndirectedGraph[NVP, NEP] =
      make(vertices(jug).map(v => vpf(v.payload)).toList,
        (newVs: Seq[Vertex[NVP]]) => ???)

  }
}
