package axle.graph

import collection.JavaConverters._
import collection._

trait JungDirectedGraph[VP, EP] extends GenDirectedGraph[VP, EP] {

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

  def edge(source: V, dest: V, payload: EP): (JungDirectedGraph[VP, EP], E) = {
    // val result = new DirectedSparseGraph[V, E]()
    new JungDirectedGraphEdgeImpl(source, dest, payload)
  }

  def ++(eps: Seq[(V, V, EP)]): (JungDirectedGraph[VP, EP], Seq[E]) = todo

  def vertex(payload: VP): (JungDirectedGraph[VP, EP], V) = {

    new JungDirectedGraphVertexImpl(payload)
  }

  // TODO: findVertex needs an index:
  def findVertex(test: VP => Boolean): Option[V] = vertices().find(v => test(v.payload))

  def removeAllEdgesAndVertices(): JungDirectedGraph[VP, EP] = vertices().map(jungGraph.removeVertex(_))

  def deleteEdge(e: E): JungDirectedGraph[VP, EP] = jungGraph.removeEdge(e)

  def deleteVertex(v: V): JungDirectedGraph[VP, EP] = jungGraph.removeVertex(v)

  def leaves(): Set[V] = vertices().filter(isLeaf(_))

  def neighbors(v: V): Set[V] = jungGraph.getNeighbors(v).asScala.toSet

  def precedes(v1: V, v2: V): Boolean = predecessors(v2).contains(v1)

  def predecessors(v: V): Set[V] = jungGraph.getPredecessors(v).asScala.toSet

  def isLeaf(v: V): Boolean = jungGraph.getSuccessorCount(v) == 0

  def successors(v: V): Set[V] = jungGraph.getSuccessors(v).asScala.toSet

  def outputEdgesOf(v: V): Set[E] = jungGraph.getOutEdges(v).asScala.toSet

  def descendantsIntersectsSet(v: V, s: Set[V]): Boolean =
    s.contains(v) || s.exists(x => descendantsIntersectsSet(x, s))

  def removeInputs(vs: Set[V]): JungDirectedGraph[VP, EP] = {
    vs.map(v => jungGraph.getInEdges(v).asScala.map(inEdge => jungGraph.removeEdge(inEdge)))
  }

  def removeOutputs(vs: Set[V]): JungDirectedGraph[VP, EP] = {
    vs.map(v => jungGraph.getOutEdges(v).asScala.map(outEdge => jungGraph.removeEdge(outEdge)))
  }

  def removeSuccessor(v: V, successor: V): JungDirectedGraph[VP, EP] = {
    findEdge(v, successor).map(deleteEdge(_))
  }

  def removePredecessor(v: V, predecessor: V): JungDirectedGraph[VP, EP] = {
    findEdge(predecessor, v).map(deleteEdge(_))
  }

  def moralGraph(): JungUndirectedGraph[_, _] = null // TODO !!!

  def isAcyclic() = true // TODO !!!

  def shortestPath(source: V, goal: V): Option[immutable.List[E]] = {
    if (source == goal) {
      Some(Nil)
    } else {
      import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath
      val path = (new DijkstraShortestPath(jungGraph)).getPath(source, goal)
      if (path == null) {
        None
      } else {
        path.size match {
          case 0 => None
          case _ => Some(path.asScala.toList)
        }
      }
    }
  }

  def vertexToVisualizationHtml(vp: VP): xml.Node = xml.Text(vp.toString)

}

object JungDirectedGraph extends DirectedGraphFactory {

  def apply[A, B](): JungDirectedGraph[A, B] = new JungDirectedGraph[A, B]() {}

  def apply[OVP, OEP, NVP, NEP](other: GenDirectedGraph[OVP, OEP])(
    convertVP: OVP => NVP, convertEP: OEP => NEP): JungDirectedGraph[NVP, NEP] = {

    val result = JungDirectedGraph[NVP, NEP]()

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
}
