package axle.graph

import collection.JavaConverters._
import collection._
import axle._

trait JungUndirectedGraphFactory extends UndirectedGraphFactory {

  def apply[VP, EP](vps: Seq[VP], ef: Seq[JungUndirectedGraphVertex[VP]] => Seq[(JungUndirectedGraphVertex[VP], JungUndirectedGraphVertex[VP], EP)]): JungUndirectedGraph[VP, EP] =
    new JungUndirectedGraph[VP, EP](vps, ef)
}

object JungUndirectedGraph extends JungUndirectedGraphFactory

class JungUndirectedGraphVertex[VP](_payload: VP)
  extends UndirectedGraphVertex[VP] {

  def payload(): VP = _payload
}

class JungUndirectedGraphEdge[VP, EP](_payload: EP)
  extends UndirectedGraphEdge[VP, EP] {

  def vertices(): (JungUndirectedGraphVertex[VP], JungUndirectedGraphVertex[VP]) // (v1, v2)

  def payload(): EP = _payload
}

case class JungUndirectedGraph[VP, EP](vps: Seq[VP], ef: Seq[JungUndirectedGraphVertex[VP]] => Seq[(JungUndirectedGraphVertex[VP], JungUndirectedGraphVertex[VP], EP)])
  extends GenUndirectedGraph[VP, EP] {

  import edu.uci.ics.jung.graph.UndirectedSparseGraph

  //  type V = JungUndirectedGraphVertex[VP]
  //  type E = JungUndirectedGraphEdge[VP, EP]
  //  type S = UndirectedSparseGraph[V, EP]

  lazy val jungGraph = new UndirectedSparseGraph[JungUndirectedGraphVertex[VP], JungUndirectedGraphEdge[VP, EP]]()

  vps.map(vp => {
    jungGraph.addVertex(new JungUndirectedGraphVertex(vp)) // TODO check return value
  })

  ef(jungGraph.getVertices.asScala.toList).map({
    case (vi, vj, ep) => {
      val edge = new JungUndirectedGraphEdge[VP, EP](ep) {
        def vertices(): (JungUndirectedGraphVertex[VP], JungUndirectedGraphVertex[VP]) = (vi, vj)
      }
      jungGraph.addEdge(edge, vi, vj) // TODO check return value
    }
  })

  def storage(): UndirectedSparseGraph[JungUndirectedGraphVertex[VP], JungUndirectedGraphEdge[VP, EP]] = jungGraph

  def vertices(): Set[JungUndirectedGraphVertex[VP]] = jungGraph.getVertices.asScala.toSet

  def edges(): Set[JungUndirectedGraphEdge[VP, EP]] = jungGraph.getEdges().asScala.toSet

  def size(): Int = jungGraph.getVertexCount()

  def findVertex(payload: VP): Option[JungUndirectedGraphVertex[VP]] = vertices().find(_.payload == payload) // TODO an index would speed this up

  def filterEdges(f: ((JungUndirectedGraphVertex[VP], JungUndirectedGraphVertex[VP], EP)) => Boolean): JungUndirectedGraph[VP, EP] = {
    val filter = (es: Seq[(JungUndirectedGraphVertex[VP], JungUndirectedGraphVertex[VP], EP)]) => es.filter(f(_))
    JungUndirectedGraph(vps, filter.compose(ef))
  }

  def unlink(e: JungUndirectedGraphEdge[VP, EP]): JungUndirectedGraph[VP, EP] = filterEdges(_ != e)

  def unlink(v1: JungUndirectedGraphVertex[VP], v2: JungUndirectedGraphVertex[VP]): JungUndirectedGraph[VP, EP] =
    filterEdges(e => (e._1 == v1 && e._2 == v2) || (e._2 == v1 && e._1 == v2))

  def areNeighbors(v1: JungUndirectedGraphVertex[VP], v2: JungUndirectedGraphVertex[VP]): Boolean = edges(v1).exists(_.connects(v1, v2))

  def forceClique(
    among: Set[JungUndirectedGraphVertex[VP]],
    payload: (JungUndirectedGraphVertex[VP], JungUndirectedGraphVertex[VP]) => EP): JungUndirectedGraph[VP, EP] = {

    val cliqued = (newVs: Seq[JungUndirectedGraphVertex[VP]]) => {

      val old2new: Map[JungUndirectedGraphVertex[VP], JungUndirectedGraphVertex[VP]] = null // TODO _vertices.zip(newVs).toMap

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

  def degree(v: JungUndirectedGraphVertex[VP]): Int = edges(v).size

  def edges(v: JungUndirectedGraphVertex[VP]): Set[JungUndirectedGraphEdge[VP, EP]] =
    jungGraph.getIncidentEdges(v).asScala.toSet

  def neighbors(v: JungUndirectedGraphVertex[VP]): Set[JungUndirectedGraphVertex[VP]] =
    jungGraph.getNeighbors(v).asScala.toSet

  def delete(v: JungUndirectedGraphVertex[VP]): JungUndirectedGraph[VP, EP] =
    JungUndirectedGraph(vertices().toSeq.filter(_ != v).map(_.payload), ef)

  // a "leaf" is vertex with only one neighbor
  def firstLeafOtherThan(r: JungUndirectedGraphVertex[VP]): Option[JungUndirectedGraphVertex[VP]] = {
    vertices().find(v => neighbors(v).size == 1 && !v.equals(r))
  }

  /**
   * "decompositions" page 3 (Definition 3, Section 9.3)
   * turn the neighbors of v into a clique
   */

  def eliminate(
    v: JungUndirectedGraphVertex[VP],
    payload: (JungUndirectedGraphVertex[VP], JungUndirectedGraphVertex[VP]) => EP): JungUndirectedGraph[VP, EP] = {

    // TODO
    //    val vs = neighbors(v)
    //    makeFunctional.jungGraph.removeVertex(v)
    //    forceClique(vs, payload)
    null
  }

  def map[NVP, NEP](vpf: VP => NVP, epf: EP => NEP): JungUndirectedGraph[NVP, NEP] = {

    val newVps = vps.map(vpf(_))

    val oldVs = null // TODO

    val newEf = (newVs: Seq[JungUndirectedGraphVertex[NVP]]) =>
      ef(oldVs).map({
        case (vi, vj, ep) => (new JungUndirectedGraphVertex(vpf(vi.payload)), new JungUndirectedGraphVertex(vpf(vj.payload)), epf(ep))
      })

    JungUndirectedGraph(newVps, newEf)
  }

}
