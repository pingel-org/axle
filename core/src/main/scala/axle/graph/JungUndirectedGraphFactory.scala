package axle.graph

import collection.JavaConverters._
import collection._

trait JungUndirectedGraphFactory extends UndirectedGraphFactory {

  def apply[A, B](): JungUndirectedGraph[A, B]

  def apply[VP, EP](vps: Seq[VP], ef: Seq[JungUndirectedGraphVertex[VP]] => Seq[(JungUndirectedGraphVertex[VP], JungUndirectedGraphVertex[VP], EP)]): JungUndirectedGraph[VP, EP] =
    new JungUndirectedGraphImpl[VP, EP](vps, ef)
}

object JungUndirectedGraph extends JungUndirectedGraphFactory

trait JungUndirectedGraphVertex[P] extends UndirectedGraphVertex[P]

trait JungUndirectedGraphEdge[P] extends UndirectedGraphEdge[P]

class JungUndirectedGraphVertexImpl[VP](_payload: VP)
  extends JungUndirectedGraphVertex[VP] {

  def payload(): VP = _payload
}

class JungUndirectedGraphEdgeImpl[VP, EP](v1: V, v2: V, _payload: EP)
  extends JungUndirectedGraphEdge[EP] {

  def vertices(): (V, V) = (v1, v2)

  def payload(): EP = _payload
}

trait JungUndirectedGraph[VP, EP] extends GenUndirectedGraph[VP, EP]

class JungUndirectedGraphImpl[VP, EP](vps: Seq[VP], ef: Seq[JungUndirectedGraphVertex[VP]] => Seq[(JungUndirectedGraphVertex[VP], JungUndirectedGraphVertex[VP], EP)])
  extends JungUndirectedGraph[VP, EP] {

  import edu.uci.ics.jung.graph.UndirectedSparseGraph

  type V = JungUndirectedGraphVertex[VP]
  type E = JungUndirectedGraphEdge[EP]
  type S = UndirectedSparseGraph[V, EP]

  lazy val jungGraph = new UndirectedSparseGraph[V, EP]()

  vps.map(vp => {
    val v = new JungUndirectedGraphVertexImpl(vp)
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
  def edges(): Set[E] = jungGraph.getEdges.asScala.toSet.map(je => new JungUndirectedGraphEdgeImpl(je.vi, je.vj, je.payload))
  def size(): Int = jungGraph.getVertexCount()

  def vertex(payload: VP): (JungUndirectedGraph[VP, EP], JungUndirectedGraphVertex[VP]) = 4

  // TODO: findVertex needs an index:
  def findVertex(payload: VP): Option[V] = vertices().find(_.payload == payload)

  def edge(v1: V, v2: V, payload: EP): (JungUndirectedGraph[VP, EP], JungUndirectedGraphEdge[EP]) = 4

  def unlink(e: E): JungUndirectedGraph[VP, EP] = {
    jungGraph.removeEdge(e)
  }

  def unlink(v1: V, v2: V): JungUndirectedGraph[VP, EP] = {
    edges(v1).filter(_.other(v1).equals(v2)).map(unlink(_))
  }

  def areNeighbors(v1: V, v2: V): Boolean = edges(v1).exists(_.connects(v1, v2))

  def forceClique(vs: Set[V], payload: (V, V) => EP): JungUndirectedGraph[VP, EP] =
    vs.doubles().filter({ case (vi, vj) => !areNeighbors(vi, vj) })
      .map({ case (vi, vj) => edge(vi, vj, payload(vi, vj)) })

  def degree(v: V): Int = edges(v).size

  def edges(v: V): Set[E] = {
    jungGraph.getIncidentEdges(v).asScala.toSet
  }

  def neighbors(v: V): Set[V] = {
    jungGraph.getNeighbors(v).asScala.toSet
  }

  def delete(v: V): JungUndirectedGraph[VP, EP] = {
    jungGraph.removeVertex(v)
  }

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

  // TODO there is probably a more efficient way to do this:
  def eliminate(vs: immutable.List[V], payload: (V, V) => EP): JungUndirectedGraph[VP, EP] = {
    vs.map(eliminate(_, payload))
  }

  def mapVertices[NVP](f: VP => NVP): JungUndirectedGraph[NVP, EP] = 4

  def mapEdges[NEP](f: EP => NEP): JungUndirectedGraph[VP, NEP] = 4

}
