package axle.graph

import collection.JavaConverters._
import collection._

object JungUndirectedGraphFactory extends JungUndirectedGraphFactory

trait JungUndirectedGraphFactory extends UndirectedGraphFactory {

  type G[VP, EP] = JungUndirectedGraph[VP, EP]

  def graph[A, B](): G[A, B] = new JungUndirectedGraph[A, B]() {}

  def graphFrom[OVP, OEP, NVP, NEP](other: UndirectedGraph[OVP, OEP])(convertVP: OVP => NVP, convertEP: OEP => NEP) = {

    val result = graph[NVP, NEP]()

    val ov2nv = mutable.Map[other.V, result.V]()

    other.vertices().map(oldV => {
      val oldVP = oldV.payload()
      val newVP = convertVP(oldVP)
      val newV = result += newVP
      ov2nv += oldV -> newV
    })

    other.edges().map(oldE => {
      val (otherv1, otherv2) = oldE.vertices()
      val nv1 = ov2nv(otherv1)
      val nv2 = ov2nv(otherv2)
      result += ((nv1, nv2), convertEP(oldE.payload))
    })

    result
  }

  trait JungUndirectedGraph[VP, EP] extends UndirectedGraph[VP, EP] {

    import edu.uci.ics.jung.graph.UndirectedSparseGraph

    type V = JungUndirectedGraphVertex[VP]
    type E = JungUndirectedGraphEdge[EP]

    type S = UndirectedSparseGraph[V, E]

    trait JungUndirectedGraphVertex[P] extends UndirectedGraphVertex[P]

    trait JungUndirectedGraphEdge[P] extends UndirectedGraphEdge[P]

    class JungUndirectedGraphVertexImpl(var _payload: VP)
      extends JungUndirectedGraphVertex[VP] {

      val ok = jungGraph.addVertex(this)
      // TODO check 'ok'

      def payload(): VP = _payload
      def setPayload(p: VP) = _payload = p
    }

    class JungUndirectedGraphEdgeImpl(v1: V, v2: V, var _payload: EP)
      extends JungUndirectedGraphEdge[EP] {

      val ok = jungGraph.addEdge(this, v1, v2)
      // TODO check 'ok'

      def vertices(): (V, V) = (v1, v2)

      def payload(): EP = _payload
      def setPayload(p: EP) = _payload = p
    }

    val jungGraph = new UndirectedSparseGraph[V, E]()

    def storage() = jungGraph

    def vertices(): immutable.Set[V] = jungGraph.getVertices.asScala.toSet

    def edges(): immutable.Set[E] = jungGraph.getEdges.asScala.toSet

    def size(): Int = jungGraph.getVertexCount()

    def vertex(payload: VP): JungUndirectedGraphVertex[VP] = new JungUndirectedGraphVertexImpl(payload)

    // TODO: findVertex needs an index:
    def findVertex(payload: VP): Option[V] = vertices().find(_.payload == payload)

    def edge(v1: V, v2: V, payload: EP): JungUndirectedGraphEdge[EP] = new JungUndirectedGraphEdgeImpl(v1, v2, payload)

    def unlink(e: E): Unit = jungGraph.removeEdge(e)

    def unlink(v1: V, v2: V): Unit = edges(v1).filter(_.other(v1).equals(v2)).map(unlink(_))

    def areNeighbors(v1: V, v2: V) = edges(v1).exists(_.connects(v1, v2))

    def degree(v: V) = edges(v).size

    def edges(v: V): Set[E] = jungGraph.getIncidentEdges(v).asScala.toSet

    def neighbors(v: V): Set[V] = jungGraph.getNeighbors(v).asScala.toSet

    def delete(v: V): Unit = jungGraph.removeVertex(v)

    // a "leaf" is vertex with only one neighbor
    def firstLeafOtherThan(r: V) = vertices().find(v => neighbors(v).size == 1 && !v.equals(r))

    def eliminate(v: V, payload: (V, V) => EP) = {
      // "decompositions" page 3 (Definition 3, Section 9.3)
      // turn the neighbors of v into a clique
      val vs = neighbors(v)
      jungGraph.removeVertex(v)
      forceClique(vs.asInstanceOf[Set[V]], payload)
    }

    // TODO there is probably a more efficient way to do this:
    def eliminate(vs: immutable.List[V], payload: (V, V) => EP): Unit = vs.map(eliminate(_, payload))

  }

}
