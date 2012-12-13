package axle.graph

import collection.JavaConverters._
import collection._

case class JungDirectedGraph[VP, EP](vps: Seq[VP], ef: Seq[JungDirectedGraphVertex[VP]] => Seq[(JungDirectedGraphVertex[VP], JungDirectedGraphVertex[VP], EP)])
  extends DirectedGraph[VP, EP] {

  type G[VP, EP] = JungDirectedGraph[VP, EP]
  type V[VP] = JungDirectedGraphVertex[VP]
  type E[VP, EP] = JungDirectedGraphEdge[VP, EP]
  // type S = DirectedSparseGraph[JungDirectedGraphVertex[VP], JungDirectedGraphEdge[VP, EP]]

  import edu.uci.ics.jung.graph.DirectedSparseGraph

  lazy val jungGraph = new DirectedSparseGraph[JungDirectedGraphVertex[VP], JungDirectedGraphEdge[VP, EP]]()

  // Note: Have to compensate for JUNG not preserving vertex order
  lazy val vertexSeq = vps.map(vp => new JungDirectedGraphVertex(vp))

  lazy val vertexSet = verticesSeq.toSet

  vertexSeq.map(v => jungGraph.addVertex(v)) // TODO check return value

  ef(vertexSeq).map({
    case (vi, vj, ep) => {
      val edge: JungDirectedGraphEdge[VP, EP] = new JungDirectedGraphEdge[VP, EP](ep)
      jungGraph.addEdge(edge, vi, vj) // TODO check return value
    }
  })

  def storage(): DirectedSparseGraph[JungDirectedGraphVertex[VP], JungDirectedGraphEdge[VP, EP]] = jungGraph

  def size(): Int = jungGraph.getVertexCount()

  override def edges(): Set[JungDirectedGraphEdge[VP, EP]] = jungGraph.getEdges().asScala.toSet

  def verticesSeq(): Seq[JungDirectedGraphVertex[VP]] = vertexSeq

  override def vertices(): Set[JungDirectedGraphVertex[VP]] = vertexSet // 

  def findEdge(from: JungDirectedGraphVertex[VP], to: JungDirectedGraphVertex[VP]): Option[JungDirectedGraphEdge[VP, EP]] = Option(jungGraph.findEdge(from, to))

  // TODO: findVertex needs an index
  def findVertex(f: JungDirectedGraphVertex[VP] => Boolean): Option[JungDirectedGraphVertex[VP]] = vertexSeq.find(f(_))

  def deleteEdge(e: JungDirectedGraphEdge[VP, EP]): JungDirectedGraph[VP, EP] = filterEdges(_ != e)

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

  def moralGraph(): JungDirectedGraph[_, _] = null // TODO !!!

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

class JungDirectedGraphEdge[VP, EP](ep: EP) extends DirectedGraphEdge[VP, EP] {

  type V[VP] = JungDirectedGraphVertex[VP]
  
  def source(): JungDirectedGraphVertex[VP] = null // TODO
  def dest(): JungDirectedGraphVertex[VP] = null // TODO
  def payload(): EP = ep
}

class JungDirectedGraphVertex[VP](vp: VP) extends DirectedGraphVertex[VP] {
  def payload(): VP = vp
}

trait JungDirectedGraphFactory extends DirectedGraphFactory {

  type G[VP, EP] = JungDirectedGraph[VP, EP]
  type V[VP] = JungDirectedGraphVertex[VP]
  
  override def apply[VP, EP](vps: Seq[VP], ef: Seq[JungDirectedGraphVertex[VP]] => Seq[(JungDirectedGraphVertex[VP], JungDirectedGraphVertex[VP], EP)]): JungDirectedGraph[VP, EP] =
    new JungDirectedGraph(vps, ef)
}

object JungDirectedGraph extends JungDirectedGraphFactory
