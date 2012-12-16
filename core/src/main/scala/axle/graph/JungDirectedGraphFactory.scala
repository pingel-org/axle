package axle.graph

import collection.JavaConverters._
import collection._
import edu.uci.ics.jung.graph.DirectedSparseGraph

class JungDirectedGraph[VP, EP](
  vps: Seq[VP],
  ef: Seq[Vertex[VP]] => Seq[(Vertex[VP], Vertex[VP], EP)])
  extends DirectedGraph[VP, EP] {

  type G[VP, EP] = JungDirectedGraph[VP, EP]

  lazy val jungGraph = new DirectedSparseGraph[Vertex[VP], Edge[EP]]()

  // Note: Have to compensate for JUNG not preserving vertex order
  // ...which defeats some of the purpose.  At least this is lazy:
  lazy val vertexSeq = vps.map(vp => Vertex(vp))

  lazy val vertexSet = verticesSeq.toSet

  vertexSeq.map(v => jungGraph.addVertex(v)) // TODO check return value

  ef(vertexSeq).map({
    case (vi, vj, ep) => {
      jungGraph.addEdge(Edge(ep), vi, vj) // TODO check return value
    }
  })

  def storage(): DirectedSparseGraph[Vertex[VP], Edge[EP]] = jungGraph

  def size(): Int = jungGraph.getVertexCount()

  // Set[JungEdge[VP, EP]]
  override def edges() = jungGraph.getEdges().asScala.toSet

  // Seq[JungVertex[VP]]
  def verticesSeq() = vertexSeq

  // Set[JungVertex[VP]]
  override def vertices() = vertexSet

  def findEdge(from: Vertex[VP], to: Vertex[VP]): Option[Edge[EP]] = Option(jungGraph.findEdge(from, to))

  // TODO: findVertex needs an index
  def findVertex(f: Vertex[VP] => Boolean): Option[Vertex[VP]] = vertexSeq.find(f(_))

  // JungDirectedGraph[VP, EP]
  def deleteEdge(e: Edge[EP]) = filterEdges(_ != e)

  // JungDirectedGraph[VP, EP]
  def deleteVertex(v: Vertex[VP]) = JungDirectedGraph(vertices().toSeq.filter(_ != v).map(_.payload), ef)

  // JungDirectedGraph[VP, EP]
  def filterEdges(f: ((Vertex[VP], Vertex[VP], EP)) => Boolean) = {
    val filter = (es: Seq[(Vertex[VP], Vertex[VP], EP)]) => es.filter(f(_))
    JungDirectedGraph(vps, filter.compose(ef))
  }

  def leaves(): Set[Vertex[VP]] = vertices().filter(isLeaf(_))

  def neighbors(v: Vertex[VP]): Set[Vertex[VP]] = jungGraph.getNeighbors(v).asScala.toSet

  def precedes(v1: Vertex[VP], v2: Vertex[VP]): Boolean = predecessors(v2).contains(v1)

  def predecessors(v: Vertex[VP]): Set[Vertex[VP]] = jungGraph.getPredecessors(v).asScala.toSet

  def isLeaf(v: Vertex[VP]): Boolean = jungGraph.getSuccessorCount(v) == 0

  def successors(v: Vertex[VP]): Set[Vertex[VP]] = jungGraph.getSuccessors(v).asScala.toSet

  def outputEdgesOf(v: Vertex[VP]): Set[Edge[EP]] = jungGraph.getOutEdges(v).asScala.toSet

  def descendantsIntersectsSet(v: Vertex[VP], s: Set[Vertex[VP]]): Boolean =
    s.contains(v) || s.exists(x => descendantsIntersectsSet(x, s))

  // JungDirectedGraph[VP, EP]
  def removeInputs(to: Set[Vertex[VP]]) = filterEdges(v => !to.contains(v._2))

  // JungDirectedGraph[VP, EP]
  def removeOutputs(from: Set[Vertex[VP]]) = filterEdges(v => !from.contains(v._1))

  // JungDirectedGraph[_, _]
  def moralGraph() = null // TODO !!!

  def isAcyclic() = true // TODO !!!

  def shortestPath(source: Vertex[VP], goal: Vertex[VP]): Option[List[Edge[EP]]] = {
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

    val oldVs = null // TODO // Seq[JungVertex[VP]]

    val newEf = (newVs: Seq[Vertex[NVP]]) =>
      ef(oldVs).map({
        case (vi, vj, ep) => (Vertex(vpf(vi.payload)), Vertex(vpf(vj.payload)), epf(ep))
      })

    JungDirectedGraph(newVps, newEf)
  }

}

trait JungDirectedGraphFactory {

  override def apply[VP, EP](vps: Seq[VP], ef: Seq[Vertex[VP]] => Seq[(Vertex[VP], Vertex[VP], EP)]) =
    new JungDirectedGraph(vps, ef)
}

object JungDirectedGraph extends JungDirectedGraphFactory
