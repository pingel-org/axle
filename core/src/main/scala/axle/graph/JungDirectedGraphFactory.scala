package axle.graph

import collection.JavaConverters._
import collection._

object JungDirectedGraphFactory extends JungDirectedGraphFactory {

  //    def draw(): Unit = {
  //      import axle.visualize._
  //      val vis = new JungDirectedGraphVisualization()
  //      vis.draw[VP, EP](this)
  //    }

}

trait JungDirectedGraphFactory extends DirectedGraphFactory {

  type G[VP, EP] = JungDirectedGraph[VP, EP]

  def graph[A, B](): G[A, B] = new JungDirectedGraph[A, B]() {}

  def graphFrom[OVP, OEP, NVP, NEP](other: DirectedGraph[OVP, OEP])(
    convertVP: OVP => NVP, convertEP: OEP => NEP) = {

    val result = graph[NVP, NEP]()

    val ov2nv = mutable.Map[other.V, result.V]()

    other.getVertices().map(ov => {
      val nv = result += convertVP(ov.getPayload)
      ov2nv += ov -> nv
    })

    other.getEdges().map(oe => {
      val nSource = ov2nv(oe.getSource)
      val nDest = ov2nv(oe.getDest)
      result += (nSource -> nDest, convertEP(oe.getPayload))
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

    class JungDirectedGraphVertexImpl(var payload: VP) extends JungDirectedGraphVertex[VP] {

      val ok = jungGraph.addVertex(this)
      // TODO check 'ok'

      def getPayload(): VP = payload

      def setPayload(p: VP) = payload = p
    }

    class JungDirectedGraphEdgeImpl(source: V, dest: V, var payload: EP) extends JungDirectedGraphEdge[EP] {

      val ok = jungGraph.addEdge(this, source, dest)
      // TODO check 'ok'

      def getSource() = source
      def getDest() = dest
      def getPayload(): EP = payload
      def setPayload(p: EP) = payload = p
    }

    val jungGraph = new DirectedSparseGraph[V, E]()

    // TODO: make enVertex implicit

    //    def enEdge(payload: EP): JungDirectedGraphEdge[EP] = {
    //      val endpoints = jungGraph.getEndpoints(payload)
    //      edge(endpoints.getFirst, endpoints.getSecond, payload, false)
    //    }

    def getStorage() = jungGraph

    def size(): Int = jungGraph.getVertexCount()

    def getEdges(): immutable.Set[E] = jungGraph.getEdges().asScala.toSet

    def getVertices(): immutable.Set[V] = jungGraph.getVertices.asScala.toSet

    def getEdge(from: V, to: V): Option[E] = {
      val result = jungGraph.findEdge(from, to)
      result match {
        case null => None
        case _ => Some(result)
      }
    }

    def edge(source: V, dest: V, payload: EP): E = new JungDirectedGraphEdgeImpl(source, dest, payload)

    def vertex(payload: VP): V = new JungDirectedGraphVertexImpl(payload)

    // TODO: findVertex needs an index:
    def findVertex(payload: VP): Option[V] = getVertices().find(_.getPayload == payload)

    def removeAllEdgesAndVertices(): Unit = getVertices().map(jungGraph.removeVertex(_))

    def deleteEdge(e: E): Unit = jungGraph.removeEdge(e)

    def deleteVertex(v: V): Unit = jungGraph.removeVertex(v)

    def getLeaves(): Set[V] = getVertices().filter(isLeaf(_))

    def getNeighbors(v: V): Set[V] = jungGraph.getNeighbors(v).asScala.toSet

    def precedes(v1: V, v2: V): Boolean = getPredecessors(v2).contains(v1)

    def getPredecessors(v: V): Set[V] = jungGraph.getPredecessors(v).asScala.toSet

    def isLeaf(v: V): Boolean = jungGraph.getSuccessorCount(v) == 0

    def getSuccessors(v: V): Set[V] = jungGraph.getSuccessors(v).asScala.toSet

    def outputEdgesOf(v: V): Set[E] = jungGraph.getOutEdges(v).asScala.toSet

    def descendantsIntersectsSet(v: V, s: Set[V]): Boolean =
      s.contains(v) || s.exists(x => descendantsIntersectsSet(x, s))

    def collectDescendants(v: V, result: mutable.Set[V]): Unit = {
      // inefficient
      if (!result.contains(v)) {
        result.add(v)
        getSuccessors(v).map(collectDescendants(_, result))
      }
    }

    def collectAncestors(v: V, result: mutable.Set[V]): Unit = {
      // inefficient
      if (!result.contains(v)) {
        result.add(v)
        getPredecessors(v).map(collectAncestors(_, result))
      }
    }

    def collectAncestors(vs: Set[V], result: mutable.Set[V]): Unit = vs.map(collectAncestors(_, result))

    def removeInputs(vs: Set[V]): Unit =
      vs.map(v => jungGraph.getInEdges(v).asScala.map(inEdge => jungGraph.removeEdge(inEdge)))

    def removeOutputs(vs: Set[V]): Unit =
      vs.map(v => jungGraph.getOutEdges(v).asScala.map(outEdge => jungGraph.removeEdge(outEdge)))

    //TODO remove this method
    def removeSuccessor(v: V, successor: V): Unit = getEdge(v, successor).map(e => deleteEdge(e))

    //TODO remove this method
    def removePredecessor(v: V, predecessor: V): Unit = getEdge(predecessor, v).map(e => deleteEdge(e))

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

  }

}
