package axle.graph

import collection.JavaConverters._
import collection._

object JungDirectedGraphFactory extends JungDirectedGraphFactory {

}

trait JungDirectedGraphFactory extends DirectedGraphFactory {

  type G[VP, EP] = JungDirectedGraph[VP, EP]

  def graph[A, B](): G[A, B] = new JungDirectedGraph[A, B]() {}

  def graphFrom[OVP, OEP, NVP, NEP](other: DirectedGraph[OVP, OEP])(
    convertVP: OVP => NVP, convertEP: OEP => NEP) = {

    val result = graph[NVP, NEP]()

    val ov2nv = mutable.Map[other.V, result.V]()

    other.vertices().map(ov => {
      val nv = result += convertVP(ov.payload)
      ov2nv += ov -> nv
    })

    other.edges().map(oe => {
      val nSource = ov2nv(oe.source)
      val nDest = ov2nv(oe.dest)
      result += (nSource -> nDest, convertEP(oe.payload))
    })

    result
  }

  trait JungDirectedGraph[VP, EP] extends DirectedGraph[VP, EP] {

    import collection._
    import edu.uci.ics.jung.graph.DirectedSparseGraph

    type V = JungDirectedGraphVertex[VP]
    type E = JungDirectedGraphEdge[EP]

    type S = DirectedSparseGraph[V, E]

    trait JungDirectedGraphVertex[P] extends DirectedGraphVertex[P]

    trait JungDirectedGraphEdge[P] extends DirectedGraphEdge[P]

    class JungDirectedGraphVertexImpl(_payload: VP) extends JungDirectedGraphVertex[VP] {

      val ok = jungGraph.addVertex(this)
      // TODO check 'ok'

      def payload(): VP = _payload
    }

    class JungDirectedGraphEdgeImpl(_source: V, _dest: V, _payload: EP) extends JungDirectedGraphEdge[EP] {

      val ok = jungGraph.addEdge(this, source, dest)
      // TODO check 'ok'

      def source() = _source
      def dest() = _dest
      def payload(): EP = _payload
    }

    val jungGraph = new DirectedSparseGraph[V, E]()

    def storage() = jungGraph

    def size(): Int = jungGraph.getVertexCount()

    def edges(): immutable.Set[E] = jungGraph.getEdges().asScala.toSet

    def vertices(): immutable.Set[V] = jungGraph.getVertices.asScala.toSet

    def findEdge(from: V, to: V): Option[E] = Option(jungGraph.findEdge(from, to))

    def edge(source: V, dest: V, payload: EP): E = new JungDirectedGraphEdgeImpl(source, dest, payload)

    def vertex(payload: VP): V = new JungDirectedGraphVertexImpl(payload)

    // TODO: findVertex needs an index:
    def findVertex(test: VP => Boolean): Option[V] = vertices().find(v => test(v.payload))

    def removeAllEdgesAndVertices(): Unit = vertices().map(jungGraph.removeVertex(_))

    def deleteEdge(e: E): Unit = jungGraph.removeEdge(e)

    def deleteVertex(v: V): Unit = jungGraph.removeVertex(v)

    def leaves(): Set[V] = vertices().filter(isLeaf(_))

    def neighbors(v: V): Set[V] = jungGraph.getNeighbors(v).asScala.toSet

    def precedes(v1: V, v2: V): Boolean = predecessors(v2).contains(v1)

    def predecessors(v: V): Set[V] = jungGraph.getPredecessors(v).asScala.toSet

    def isLeaf(v: V): Boolean = jungGraph.getSuccessorCount(v) == 0

    def successors(v: V): Set[V] = jungGraph.getSuccessors(v).asScala.toSet

    def outputEdgesOf(v: V): Set[E] = jungGraph.getOutEdges(v).asScala.toSet

    def descendantsIntersectsSet(v: V, s: Set[V]): Boolean =
      s.contains(v) || s.exists(x => descendantsIntersectsSet(x, s))

    def collectDescendants(v: V, result: mutable.Set[V]): Unit = {
      // inefficient
      if (!result.contains(v)) {
        result += v
        successors(v).map(collectDescendants(_, result))
      }
    }

    def collectAncestors(v: V, result: mutable.Set[V]): Unit = {
      // inefficient
      if (!result.contains(v)) {
        result += v
        predecessors(v).map(collectAncestors(_, result))
      }
    }

    def collectAncestors(vs: Set[V], result: mutable.Set[V]): Unit = vs.map(collectAncestors(_, result))

    def removeInputs(vs: Set[V]): Unit =
      vs.map(v => jungGraph.getInEdges(v).asScala.map(inEdge => jungGraph.removeEdge(inEdge)))

    def removeOutputs(vs: Set[V]): Unit =
      vs.map(v => jungGraph.getOutEdges(v).asScala.map(outEdge => jungGraph.removeEdge(outEdge)))

    //TODO remove this method
    def removeSuccessor(v: V, successor: V): Unit = findEdge(v, successor).map(deleteEdge(_))

    //TODO remove this method
    def removePredecessor(v: V, predecessor: V): Unit = findEdge(predecessor, v).map(deleteEdge(_))

    def moralGraph(): JungUndirectedGraphFactory.UndirectedGraph[_, _] = null // TODO !!!

    def isAcyclic() = true // TODO !!!

    def shortestPath(source: V, goal: V): Option[immutable.List[E]] = {
      if (source == goal) {
        Some(Nil)
      } else {
        import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath
        val dsp = new DijkstraShortestPath(jungGraph)
        val path = dsp.getPath(source, goal)
        path match {
          case null => None
          case _ => path.size match {
            case 0 => None
            case _ => Some(path.asScala.toList)
          }
        }
      }
    }

    def vertexToVisualizationHtml(vp: VP): xml.Node = <span>{vp.toString}</span>

  }

}
