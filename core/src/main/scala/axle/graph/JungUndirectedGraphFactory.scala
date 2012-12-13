package axle.graph

import collection.JavaConverters._
import collection._
import axle._

case class JungUndirectedGraph[VP, EP](vps: Seq[VP], ef: Seq[JungUndirectedGraphVertex[VP]] => Seq[(JungUndirectedGraphVertex[VP], JungUndirectedGraphVertex[VP], EP)])
  extends UndirectedGraph[VP, EP] {

  type G[VP, EP] = JungUndirectedGraph[VP, EP]
  type V[VP] = JungUndirectedGraphVertex[VP]
  type E[VP, EP] = JungUndirectedGraphEdge[VP, EP]

  import edu.uci.ics.jung.graph.UndirectedSparseGraph

  lazy val jungGraph = new UndirectedSparseGraph[JungUndirectedGraphVertex[VP], JungUndirectedGraphEdge[VP, EP]]()

  lazy val vertexSeq = vps.map(vp => new JungUndirectedGraphVertex(vp))

  vertexSeq.map(v => jungGraph.addVertex(v)) // TODO check return value

  lazy val vertexSet = vertexSeq.toSet

  ef(vertexSeq).map({
    case (vi, vj, ep) => {
      val edge = new JungUndirectedGraphEdge[VP, EP](ep)
      jungGraph.addEdge(edge, vi, vj) // TODO check return value
    }
  })

  def storage(): UndirectedSparseGraph[JungUndirectedGraphVertex[VP], JungUndirectedGraphEdge[VP, EP]] = jungGraph

  def vertices(): Set[JungUndirectedGraphVertex[VP]] = vertexSet

  def edges(): Set[JungUndirectedGraphEdge[VP, EP]] = jungGraph.getEdges().asScala.toSet

  def size(): Int = jungGraph.getVertexCount()

  // TODO findVertex needs an index
  def findVertex(f: JungUndirectedGraphVertex[VP] => Boolean): Option[JungUndirectedGraphVertex[VP]] = vertexSeq.find(f(_))

  def filterEdges(f: ((JungUndirectedGraphVertex[VP], JungUndirectedGraphVertex[VP], EP)) => Boolean): JungUndirectedGraph[VP, EP] = {
    val filter = (es: Seq[(JungUndirectedGraphVertex[VP], JungUndirectedGraphVertex[VP], EP)]) => es.filter(f(_))
    JungUndirectedGraph(vps, filter.compose(ef))
  }

  def unlink(e: JungUndirectedGraphEdge[VP, EP]): JungUndirectedGraph[VP, EP] = filterEdges(_ != e)

  def unlink(v1: JungUndirectedGraphVertex[VP], v2: JungUndirectedGraphVertex[VP]): JungUndirectedGraph[VP, EP] =
    filterEdges(e => (e._1 == v1 && e._2 == v2) || (e._2 == v1 && e._1 == v2))

  def areNeighbors(v1: JungUndirectedGraphVertex[VP], v2: JungUndirectedGraphVertex[VP]): Boolean = edges(v1).exists(_.connects(v1, v2))

  def forceClique(among: Set[JungUndirectedGraphVertex[VP]], payload: (JungUndirectedGraphVertex[VP], JungUndirectedGraphVertex[VP]) => EP): JungUndirectedGraph[VP, EP] = {

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

  def eliminate(v: JungUndirectedGraphVertex[VP], payload: (JungUndirectedGraphVertex[VP], JungUndirectedGraphVertex[VP]) => EP): JungUndirectedGraph[VP, EP] = {

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

class JungUndirectedGraphEdge[VP, EP](ep: EP) extends UndirectedGraphEdge[VP, EP] {
  
  type V[VP] = JungUndirectedGraphVertex[VP]
  
  def vertices(): (JungUndirectedGraphVertex[VP], JungUndirectedGraphVertex[VP]) = null // TODO (v1, v2)
  def payload(): EP = ep
}

class JungUndirectedGraphVertex[VP](vp: VP) extends UndirectedGraphVertex[VP] {
  def payload(): VP = vp
}

trait JungUndirectedGraphFactory extends UndirectedGraphFactory {

  type G[VP, EP] = JungUndirectedGraph[VP, EP]
  type V[VP] = JungUndirectedGraphVertex[VP]
  // type S = UndirectedSparseGraph[V, EP]

  override def apply[VP, EP](vps: Seq[VP], ef: Seq[V[VP]] => Seq[(V[VP], V[VP], EP)]): G[VP, EP] = new JungUndirectedGraph(vps, ef)

}

object JungUndirectedGraph extends JungUndirectedGraphFactory
