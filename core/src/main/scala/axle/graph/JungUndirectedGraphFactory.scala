package axle.graph

import collection.JavaConverters._
import collection._
import axle._

trait JungUndirectedGraphFactory extends UndirectedGraphFactory {

  import edu.uci.ics.jung.graph.UndirectedSparseGraph

  type G[VP, EP] = JungUndirectedGraph[VP, EP]
  type V[VP] = JungUndirectedGraphVertex[VP]
  type E[VP, EP] = JungUndirectedGraphEdge[VP, EP]
  // type S = UndirectedSparseGraph[V, EP]

  override def apply[VP, EP](vps: Seq[VP], ef: Seq[V[VP]] => Seq[(V[VP], V[VP], EP)]): G[VP, EP] = new JungUndirectedGraph(vps, ef)

  case class JungUndirectedGraph[VP, EP](vps: Seq[VP], ef: Seq[V[VP]] => Seq[(V[VP], V[VP], EP)])
    extends UndirectedGraph[VP, EP] {

    lazy val jungGraph = new UndirectedSparseGraph[V[VP], E[VP, EP]]()

    vps.map(vp => {
      jungGraph.addVertex(new JungUndirectedGraphVertex(vp)) // TODO check return value
    })

    ef(jungGraph.getVertices.asScala.toList).map({
      case (vi, vj, ep) => {
        val edge = new JungUndirectedGraphEdge[VP, EP](ep)
        // def vertices(): (V[VP], V[VP]) = (vi, vj)
        jungGraph.addEdge(edge, vi, vj) // TODO check return value
      }
    })

    def storage(): UndirectedSparseGraph[V[VP], E[VP, EP]] = jungGraph

    def vertices(): Set[V[VP]] = jungGraph.getVertices.asScala.toSet

    def edges(): Set[E[VP, EP]] = jungGraph.getEdges().asScala.toSet

    def size(): Int = jungGraph.getVertexCount()

    def findVertex(payload: VP): Option[V[VP]] = vertices().find(_.payload == payload) // TODO an index would speed this up

    def filterEdges(f: ((V[VP], V[VP], EP)) => Boolean): G[VP, EP] = {
      val filter = (es: Seq[(V[VP], V[VP], EP)]) => es.filter(f(_))
      JungUndirectedGraph(vps, filter.compose(ef))
    }

    def unlink(e: E[VP, EP]): G[VP, EP] = filterEdges(_ != e)

    def unlink(v1: V[VP], v2: V[VP]): G[VP, EP] =
      filterEdges(e => (e._1 == v1 && e._2 == v2) || (e._2 == v1 && e._1 == v2))

    def areNeighbors(v1: V[VP], v2: V[VP]): Boolean = edges(v1).exists(_.connects(v1, v2))

    def forceClique(among: Set[V[VP]], payload: (V[VP], V[VP]) => EP): G[VP, EP] = {

      val cliqued = (newVs: Seq[V[VP]]) => {

        val old2new: Map[V[VP], V[VP]] = null // TODO _vertices.zip(newVs).toMap

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

      JungUndirectedGraph(vps, cliqued(_))
    }

    def degree(v: V[VP]): Int = edges(v).size

    def edges(v: V[VP]): Set[E[VP, EP]] =
      jungGraph.getIncidentEdges(v).asScala.toSet

    def neighbors(v: V[VP]): Set[V[VP]] =
      jungGraph.getNeighbors(v).asScala.toSet

    def delete(v: V[VP]): G[VP, EP] =
      JungUndirectedGraph(vertices().toSeq.filter(_ != v).map(_.payload), ef)

    // a "leaf" is vertex with only one neighbor
    def firstLeafOtherThan(r: V[VP]): Option[V[VP]] = {
      vertices().find(v => neighbors(v).size == 1 && !v.equals(r))
    }

    /**
     * "decompositions" page 3 (Definition 3, Section 9.3)
     * turn the neighbors of v into a clique
     */

    def eliminate(v: V[VP], payload: (V[VP], V[VP]) => EP): G[VP, EP] = {

      // TODO
      //    val vs = neighbors(v)
      //    makeFunctional.jungGraph.removeVertex(v)
      //    forceClique(vs, payload)
      null
    }

    def map[NVP, NEP](vpf: VP => NVP, epf: EP => NEP): G[NVP, NEP] = {

      val newVps = vps.map(vpf(_))

      val oldVs = null // TODO

      val newEf = (newVs: Seq[V[NVP]]) =>
        ef(oldVs).map({
          case (vi, vj, ep) => (new JungUndirectedGraphVertex(vpf(vi.payload)), new JungUndirectedGraphVertex(vpf(vj.payload)), epf(ep))
        })

      JungUndirectedGraph(newVps, newEf)
    }

  }

  class JungUndirectedGraphEdge[VP, EP](ep: EP) extends UndirectedGraphEdge[VP, EP] {
    def vertices(): (V[VP], V[VP]) = null // TODO (v1, v2)
    def payload(): EP = ep
  }

  class JungUndirectedGraphVertex[VP](vp: VP) extends UndirectedGraphVertex[VP] {
    def payload(): VP = vp
  }

}

object JungUndirectedGraph extends JungUndirectedGraphFactory
