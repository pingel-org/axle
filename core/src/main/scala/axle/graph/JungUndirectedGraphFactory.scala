package axle.graph

import collection.JavaConverters._
import collection._

trait JungUndirectedGraphFactory extends UndirectedGraphFactory {

  def apply[A, B](): JungUndirectedGraph[A, B]

  def apply[VP, EP](vps: Seq[VP], ef: Seq[JungUndirectedGraphVertex[VP]] => Seq[(JungUndirectedGraphVertex[VP], JungUndirectedGraphVertex[VP], EP)]): JungUndirectedGraph[VP, EP] =
    new JungUndirectedGraph[VP, EP](vps, ef)
}

object JungUndirectedGraph extends JungUndirectedGraphFactory

class JungUndirectedGraphVertex[VP](_payload: VP)
  extends UndirectedGraphVertex[VP] {

  def payload(): VP = _payload
}

class JungUndirectedGraphEdge[VP, EP](v1: JungUndirectedGraphVertex[VP], v2: JungUndirectedGraphVertex[VP], _payload: EP)
  extends UndirectedGraphEdge[VP, EP] {

  def vertices() = (v1, v2)

  def payload(): EP = _payload
}

class JungUndirectedGraph[VP, EP](vps: Seq[VP], ef: Seq[JungUndirectedGraphVertex[VP]] => Seq[(JungUndirectedGraphVertex[VP], JungUndirectedGraphVertex[VP], EP)])
  extends GenUndirectedGraph[VP, EP] {

  import edu.uci.ics.jung.graph.UndirectedSparseGraph

  type V = JungUndirectedGraphVertex[VP]
  type E = JungUndirectedGraphEdge[VP, EP]
  type S = UndirectedSparseGraph[V, EP]

  lazy val jungGraph = new UndirectedSparseGraph[V, EP]()

  vps.map(vp => {
    val v = new JungUndirectedGraphVertex(vp)
    jungGraph.addVertex(v) // TODO check return value
  })

  ef(jungGraph.getVertices.asScala.toList).map({
    case (vi, vj, ep) => {
      // new JungUndirectedGraphEdgeImpl(vi, v2, payload)
      jungGraph.addEdge(ep, vi, vj) // TODO check return value
    }
  })

  def storage(): S = jungGraph

  def vertices(): Set[V] = jungGraph.getVertices.asScala.toSet
  def edges(): Set[E] = jungGraph.getEdges.asScala.map(je => new JungUndirectedGraphEdge(je.vi, je.vj, je.payload)).toSet
  def size(): Int = jungGraph.getVertexCount()

  def findVertex(payload: VP): Option[V] = vertices().find(_.payload == payload) // TODO an index would speed this up

  def filterEdges(f: ((JungUndirectedGraphVertex[VP], JungUndirectedGraphVertex[VP], EP)) => Boolean): JungUndirectedGraph[VP, EP] = {
    val filter = (es: Seq[(JungUndirectedGraphVertex[VP], JungUndirectedGraphVertex[VP], EP)]) => es.filter(f(_))
    JungUndirectedGraph(vps, filter.compose(ef))
  }

  def unlink(e: E): JungUndirectedGraph[VP, EP] = filterEdges(_ != e)

  def unlink(v1: V, v2: V): JungUndirectedGraph[VP, EP] = filterEdges(e => (e._1 == v1 && e._2 == v2) || (e._2 == v1 && e._1 == v2))

  def areNeighbors(v1: V, v2: V): Boolean = edges(v1).exists(_.connects(v1, v2))

  def forceClique(vs: Set[V], payload: (V, V) => EP): JungUndirectedGraph[VP, EP] =
    vs.doubles().filter({ case (vi, vj) => !areNeighbors(vi, vj) }).map({ case (vi, vj) => edge(vi, vj, payload(vi, vj)) })

  def degree(v: V): Int = edges(v).size

  def edges(v: V): Set[E] = {
    val xxx = jungGraph.getIncidentEdges(v).asScala.toSet
    xxx
  }

  def neighbors(v: V): Set[V] = {
    jungGraph.getNeighbors(v).asScala.toSet
  }

  def delete(v: V): JungUndirectedGraph[VP, EP] = JungUndirectedGraph(foo, ef)

  // a "leaf" is vertex with only one neighbor
  def firstLeafOtherThan(r: V): Option[V] = {
    vertices().find(v => neighbors(v).size == 1 && !v.equals(r))
  }

  def eliminate(v: V, payload: (V, V) => EP): JungUndirectedGraph[VP, EP] = {
    // "decompositions" page 3 (Definition 3, Section 9.3)
    // turn the neighbors of v into a clique
    val vs = neighbors(v)
    jungGraph.removeVertex(v)
    forceClique(vs.asInstanceOf[Set[V]], payload)
  }

  def map[NVP, NEP](vpf: VP => NVP, epf: EP => NEP): JungUndirectedGraph[NVP, NEP] =
    JungUndirectedGraph(vps.map(vpf(_)), epf.compose(ef))

}
