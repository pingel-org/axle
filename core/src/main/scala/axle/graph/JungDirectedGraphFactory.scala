package axle.graph

import collection.JavaConverters._
import collection._

trait JungDirectedGraphFactory extends GenDirectedGraphFactory {

  // def apply[A, B](): JungDirectedGraph[A, B] = new JungDirectedGraph[A, B]() {}

  def apply[A, B](vps: Seq[A],
    ef: Seq[JungDirectedGraphVertex[A]] => Seq[(JungDirectedGraphVertex[A], JungDirectedGraphVertex[A], B)]): JungDirectedGraph[A, B] =
    new JungDirectedGraph(vps, ef)

}

object JungDirectedGraph extends JungDirectedGraphFactory

class JungDirectedGraphVertex[VP](_payload: VP)
  extends DirectedGraphVertex[VP] {

  def payload(): VP = _payload
}

class JungDirectedGraphEdge[VP, EP](_payload: EP)
  extends DirectedGraphEdge[VP, EP] {

  def source(): JungDirectedGraphVertex[VP]

  def dest(): JungDirectedGraphVertex[VP]

  def payload(): EP = _payload
}

case class JungDirectedGraph[VP, EP](
  vps: Seq[VP],
  ef: Seq[JungDirectedGraphVertex[VP]] => Seq[(JungDirectedGraphVertex[VP], JungDirectedGraphVertex[VP], EP)])
  extends GenDirectedGraph[VP, EP] {

  import edu.uci.ics.jung.graph.DirectedSparseGraph

  // type V = JungDirectedGraphVertex[VP]
  // type E = JungDirectedGraphEdge[VP, EP]
  // type S = DirectedSparseGraph[JungDirectedGraphVertex[VP], JungDirectedGraphEdge[VP, EP]]

  lazy val jungGraph = new DirectedSparseGraph[JungDirectedGraphVertex[VP], JungDirectedGraphEdge[VP, EP]]()

  vps.map(vp => {
    jungGraph.addVertex(new JungDirectedGraphVertex(vp)) // TODO check return value
  })

  ef(jungGraph.getVertices.asScala.toList).map({
    case (vi, vj, ep) => {
      val edge: JungDirectedGraphEdge[VP, EP] = new JungDirectedGraphEdge[VP, EP](ep) {
        def source(): JungDirectedGraphVertex[VP] = vi
        def dest(): JungDirectedGraphVertex[VP] = vj
      }
      jungGraph.addEdge(edge, vi, vj) // TODO check return value
    }
  })

  def storage(): DirectedSparseGraph[JungDirectedGraphVertex[VP], JungDirectedGraphEdge[VP, EP]] = jungGraph

  def size(): Int = jungGraph.getVertexCount()

  def edges(): immutable.Set[JungDirectedGraphEdge[VP, EP]] = jungGraph.getEdges().asScala.toSet

  def verticesSeq(): Seq[JungDirectedGraphVertex[VP]] = jungGraph.getVertices.asScala.toSeq

  def vertices(): Set[JungDirectedGraphVertex[VP]] = verticesSeq.toSet

  def findEdge(from: JungDirectedGraphVertex[VP], to: JungDirectedGraphVertex[VP]): Option[JungDirectedGraphEdge[VP, EP]] = Option(jungGraph.findEdge(from, to))

  // TODO: findVertex needs an index
  // TOOD: findVertex f should probably take JungDirectedGraphVertex[VP]
  // def findVertex(f: VP => Boolean): Option[JungDirectedGraphVertex[VP]] = vertices().find(v => f(v.payload))
  def findVertex(f: JungDirectedGraphVertex[VP] => Boolean): Option[JungDirectedGraphVertex[VP]] =
    vertices().find(f(_))

  def deleteEdge(e: E): JungDirectedGraph[VP, EP] = filterEdges(_ != e)

  def deleteVertex(v: JungDirectedGraphVertex[VP]): JungDirectedGraph[VP, EP] =
    JungDirectedGraph(vertices().toSeq.filter(_ != v).map(_.payload), ef)

  def filterEdges(f: ((JungDirectedGraphVertex[VP], JungDirectedGraphVertex[VP], EP)) => Boolean): JungDirectedGraph[VP, EP] = {
    val filter = (es: Seq[(JungDirectedGraphVertex[VP], JungDirectedGraphVertex[VP], EP)]) => es.filter(f(_))
    JungDirectedGraph(vps, filter.compose(ef))
  }

  def leaves(): Set[JungDirectedGraphVertex[VP]] = vertices().filter(isLeaf(_))

  def neighbors(v: JungDirectedGraphVertex[VP]): Set[JungDirectedGraphVertex[VP]] = jungGraph.getNeighbors(v).asScala.toSet

  def precedes(v1: JungDirectedGraphVertex[VP], v2: JungDirectedGraphVertex[VP]): Boolean = predecessors(v2).contains(v1)

  def predecessors(v: JungDirectedGraphVertex[VP]): Set[JungDirectedGraphVertex[VP]] = jungGraph.getPredecessors(v).asScala.toSet

  def isLeaf(v: JungDirectedGraphVertex[VP]): Boolean = jungGraph.getSuccessorCount(v) == 0

  def successors(v: JungDirectedGraphVertex[VP]): Set[JungDirectedGraphVertex[VP]] = jungGraph.getSuccessors(v).asScala.toSet

  def outputEdgesOf(v: JungDirectedGraphVertex[VP]): Set[JungDirectedGraphEdge[VP, EP]] = jungGraph.getOutEdges(v).asScala.toSet

  def descendantsIntersectsSet(v: JungDirectedGraphVertex[VP], s: Set[JungDirectedGraphVertex[VP]]): Boolean =
    s.contains(v) || s.exists(x => descendantsIntersectsSet(x, s))

  def removeInputs(to: Set[JungDirectedGraphVertex[VP]]): JungDirectedGraph[VP, EP] = filterEdges(v => !to.contains(v._2))

  def removeOutputs(from: Set[JungDirectedGraphVertex[VP]]): JungDirectedGraph[VP, EP] = filterEdges(v => !from.contains(v._1))

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

  def map[NVP, NEP](vpf: VP => NVP, epf: EP => NEP): JungDirectedGraph[NVP, NEP] = {

    val newVps = vps.map(vpf(_))

    val oldVs = null // TODO // Seq[JungDirectedGraphVertex[VP]]

    val newEf = (newVs: Seq[JungDirectedGraphVertex[NVP]]) =>
      ef(oldVs).map({
        case (vi, vj, ep) => (new JungDirectedGraphVertex(vpf(vi.payload)), new JungDirectedGraphVertex(vpf(vj.payload)), epf(ep))
      })

    JungDirectedGraph(newVps, newEf)
  }

}
