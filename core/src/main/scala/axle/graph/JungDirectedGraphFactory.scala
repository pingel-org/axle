package axle.graph

import collection.JavaConverters._
import collection._

trait JungDirectedGraphFactory extends GenDirectedGraphFactory {

  def apply[A, B](): JungDirectedGraph[A, B] = new JungDirectedGraph[A, B]() {}

  def apply[A, B](vps: Seq[A],
    ef: Seq[JungDirectedGraphVertex[A]] => Seq[(JungDirectedGraphVertex[A], JungDirectedGraphVertex[A], B)]): JungDirectedGraph[A, B] =
    new JungDirectedGraphImpl(vps, ef)

}

object JungDirectedGraph extends JungDirectedGraphFactory

trait JungDirectedGraphVertex[VP] extends DirectedGraphVertex[VP]

trait JungDirectedGraphEdge[VP, EP] extends DirectedGraphEdge[VP, EP]

class JungDirectedGraphVertexImpl[VP](_payload: VP)
  extends JungDirectedGraphVertex[VP] {

  def payload(): VP = _payload
}

class JungDirectedGraphEdgeImpl[VP, EP](_source: JungDirectedGraphVertex[VP], _dest: JungDirectedGraphVertex[VP], _payload: EP)
  extends JungDirectedGraphEdge[VP, EP] {

  def source() = _source
  def dest() = _dest
  def payload(): EP = _payload
}

trait JungDirectedGraph[VP, EP] extends GenDirectedGraph[VP, EP]

class JungDirectedGraphImpl[VP, EP](vps: Seq[VP], ef: Seq[JungDirectedGraphVertex[VP]] => Seq[(JungDirectedGraphVertex[VP], JungDirectedGraphVertex[VP], EP)])
  extends JungDirectedGraph[VP, EP] {

  import edu.uci.ics.jung.graph.DirectedSparseGraph

  //  type V = JungDirectedGraphVertex[VP]
  //  type E = JungDirectedGraphEdge[VP, EP]
  type S = DirectedSparseGraph[JungDirectedGraphVertex[VP], EP]

  lazy val jungGraph = new DirectedSparseGraph[JungDirectedGraphVertex[VP], EP]()

  vps.map(vp => {
    val v = new JungDirectedGraphVertexImpl(vp)
    jungGraph.addVertex(v) // TODO check return value
  })

  ef(jungGraph.getVertices.asScala.toList).map({
    case (vi, vj, ep) => {
      // new JungDirectedGraphEdgeImpl(vi, v2, ep)
      jungGraph.addEdge(ep, vi, vj) // TODO check return value
    }
  })

  def storage(): S = jungGraph

  def size(): Int = jungGraph.getVertexCount()

  def edges(): immutable.Set[JungDirectedGraphEdge[VP, EP]] = jungGraph.getEdges().asScala.toSet

  def vertices(): Set[JungDirectedGraphVertex[VP]] = jungGraph.getVertices.asScala.toSet

  def findEdge(from: JungDirectedGraphVertex[VP], to: JungDirectedGraphVertex[VP]): Option[JungDirectedGraphEdge[VP, EP]] = Option(jungGraph.findEdge(from, to))

  // TODO: findVertex needs an index:
  def findVertex(f: VP => Boolean): Option[JungDirectedGraphVertex[VP]] = vertices().find(v => f(v.payload))

  def removeAllEdgesAndVertices(): JungDirectedGraph[VP, EP] = vertices().map(jungGraph.removeVertex(_))

  def deleteEdge(e: JungDirectedGraphEdge[VP, EP]): JungDirectedGraph[VP, EP] = jungGraph.removeEdge(e)

  def deleteVertex(v: JungDirectedGraphVertex[VP]): JungDirectedGraph[VP, EP] = jungGraph.removeVertex(v)

  def leaves(): Set[JungDirectedGraphVertex[VP]] = vertices().filter(isLeaf(_))

  def neighbors(v: JungDirectedGraphVertex[VP]): Set[JungDirectedGraphVertex[VP]] = jungGraph.getNeighbors(v).asScala.toSet

  def precedes(v1: JungDirectedGraphVertex[VP], v2: JungDirectedGraphVertex[VP]): Boolean = predecessors(v2).contains(v1)

  def predecessors(v: JungDirectedGraphVertex[VP]): Set[JungDirectedGraphVertex[VP]] = jungGraph.getPredecessors(v).asScala.toSet

  def isLeaf(v: JungDirectedGraphVertex[VP]): Boolean = jungGraph.getSuccessorCount(v) == 0

  def successors(v: JungDirectedGraphVertex[VP]): Set[JungDirectedGraphVertex[VP]] = jungGraph.getSuccessors(v).asScala.toSet

  def outputEdgesOf(v: JungDirectedGraphVertex[VP]): Set[JungDirectedGraphEdge[VP, EP]] = jungGraph.getOutEdges(v).asScala.toSet

  def descendantsIntersectsSet(v: JungDirectedGraphVertex[VP], s: Set[JungDirectedGraphVertex[VP]]): Boolean =
    s.contains(v) || s.exists(x => descendantsIntersectsSet(x, s))

  def removeInputs(vs: Set[JungDirectedGraphVertex[VP]]): JungDirectedGraph[VP, EP] =
    vs.map(v => jungGraph.getInEdges(v).asScala.map(inEdge => jungGraph.removeEdge(inEdge)))

  def removeOutputs(vs: Set[JungDirectedGraphVertex[VP]]): JungDirectedGraph[VP, EP] =
    vs.map(v => jungGraph.getOutEdges(v).asScala.map(outEdge => jungGraph.removeEdge(outEdge)))

  def moralGraph(): JungUndirectedGraph[_, _] = null // TODO !!!

  def isAcyclic() = true // TODO !!!

  def shortestPath(source: JungDirectedGraphVertex[VP], goal: JungDirectedGraphVertex[VP]): Option[List[JungDirectedGraphEdge[VP, EP]]] = {
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

  def map[NVP, NEP](vpf: VP => NVP, epf: EP => NEP): JungDirectedGraph[NVP, NEP] =
    JungDirectedGraph(vps.map(vpf(_)), epf.compose(ef))

}
