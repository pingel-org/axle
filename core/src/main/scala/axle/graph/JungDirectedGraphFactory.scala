package axle.graph

import collection.JavaConverters._
import collection._
import edu.uci.ics.jung.graph.DirectedSparseGraph

class JungDirectedGraph[VP, EP](
  vps: Seq[VP],
  ef: Seq[DirectedGraphVertex[VP]] => Seq[(DirectedGraphVertex[VP], DirectedGraphVertex[VP], EP)])
  extends DirectedGraph[VP, EP] {

  type G[VP, EP] = JungDirectedGraph[VP, EP]
  type V[VP] = JungDirectedGraphVertex[VP]
  type E[VP, EP] = JungDirectedGraphEdge[VP, EP]

  lazy val jungGraph = new DirectedSparseGraph[JungDirectedGraphVertex[VP], JungDirectedGraphEdge[VP, EP]]()

  // Note: Have to compensate for JUNG not preserving vertex order
  lazy val vertexSeq = vps.map(vp => new JungDirectedGraphVertex(vp))

  lazy val vertexSet = verticesSeq.toSet

  vertexSeq.map(v => jungGraph.addVertex(v)) // TODO check return value

  ef(vertexSeq).map({
    case (vi, vj, ep) => {
      jungGraph.addEdge(new JungDirectedGraphEdge(ep), vi, vj) // TODO check return value
    }
  })

  def storage(): DirectedSparseGraph[JungDirectedGraphVertex[VP], JungDirectedGraphEdge[VP, EP]] = jungGraph

  def size(): Int = jungGraph.getVertexCount()

  // Set[JungDirectedGraphEdge[VP, EP]]
  override def edges() = jungGraph.getEdges().asScala.toSet

  // Seq[JungDirectedGraphVertex[VP]]
  def verticesSeq() = vertexSeq

  // Set[JungDirectedGraphVertex[VP]]
  override def vertices() = vertexSet

  def findEdge(from: JungDirectedGraphVertex[VP], to: JungDirectedGraphVertex[VP]): Option[JungDirectedGraphEdge[VP, EP]] = Option(jungGraph.findEdge(from, to))

  // TODO: findVertex needs an index
  def findVertex(f: JungDirectedGraphVertex[VP] => Boolean): Option[JungDirectedGraphVertex[VP]] = vertexSeq.find(f(_))

  // JungDirectedGraph[VP, EP]
  def deleteEdge(e: JungDirectedGraphEdge[VP, EP]) = filterEdges(_ != e)

  // JungDirectedGraph[VP, EP]
  def deleteVertex(v: JungDirectedGraphVertex[VP]) = JungDirectedGraph(vertices().toSeq.filter(_ != v).map(_.payload), ef)

  // JungDirectedGraph[VP, EP]
  def filterEdges(f: ((DirectedGraphVertex[VP], DirectedGraphVertex[VP], EP)) => Boolean) = {
    val filter = (es: Seq[(DirectedGraphVertex[VP], DirectedGraphVertex[VP], EP)]) => es.filter(f(_))
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

  // JungDirectedGraph[VP, EP]
  def removeInputs(to: Set[DirectedGraphVertex[VP]]) = filterEdges(v => !to.contains(v._2))

  // JungDirectedGraph[VP, EP]
  def removeOutputs(from: Set[DirectedGraphVertex[VP]]) = filterEdges(v => !from.contains(v._1))

  // JungDirectedGraph[_, _]
  def moralGraph() = null // TODO !!!

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

  def vertexToVisualizationHtml(vp: VP): xml.Node = vp match {
    case x: axle.XmlAble => x.toXml
    case _ => xml.Text(vp.toString)
  }

  // JungDirectedGraph[NVP, NEP]
  def map[NVP, NEP](vpf: VP => NVP, epf: EP => NEP) = {

    val newVps = vps.map(vpf(_))

    val oldVs = null // TODO // Seq[JungDirectedGraphVertex[VP]]

    val newEf = (newVs: Seq[DirectedGraphVertex[NVP]]) =>
      ef(oldVs).map({
        case (vi, vj, ep) => (new JungDirectedGraphVertex(vpf(vi.payload)), new JungDirectedGraphVertex(vpf(vj.payload)), epf(ep))
      })

    JungDirectedGraph(newVps, newEf)
  }

}

class JungDirectedGraphEdge[VP, EP](ep: EP) extends DirectedGraphEdge[VP, EP] {

  type V[VP] = JungDirectedGraphVertex[VP]

  def source(): JungDirectedGraphVertex[VP] = null // TODO
  def dest(): JungDirectedGraphVertex[VP] = null // TODO
  def payload(): EP = ep
}

class JungDirectedGraphVertex[VP](vp: VP) extends DirectedGraphVertex[VP] {
  def payload(): VP = vp
}

trait JungDirectedGraphFactory { // extends DirectedGraphFactory {

  // type G[VP, EP] = JungDirectedGraph[VP, EP]
  // type V[VP] = JungDirectedGraphVertex[VP]

  // JungDirectedGraph[VP, EP]
  override def apply[VP, EP](vps: Seq[VP], ef: Seq[DirectedGraphVertex[VP]] => Seq[(DirectedGraphVertex[VP], DirectedGraphVertex[VP], EP)]) =
    new JungDirectedGraph(vps, ef)
}

object JungDirectedGraph extends JungDirectedGraphFactory
