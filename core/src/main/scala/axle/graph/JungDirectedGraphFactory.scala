package axle.graph

import collection.JavaConverters._
import collection._
import edu.uci.ics.jung.graph.DirectedSparseGraph

case class JungDirectedGraph[VP, EP](
  vps: Seq[VP],
  ef: Seq[Vertex[VP]] => Seq[(Vertex[VP], Vertex[VP], EP)])
  extends DirectedGraph[VP, EP] {

  type G[VP, EP] = JungDirectedGraph[VP, EP]
  type ES = EP

  val edgePayloadFunction = (es: EP) => es

  lazy val jungGraph = new DirectedSparseGraph[Vertex[VP], Edge[EP, EP]]()

  // Note: Have to compensate for JUNG not preserving vertex order
  // ...which defeats some of the purpose.  At least this is lazy:
  lazy val vertexSeq = vps.map(vp => Vertex(vp))

  lazy val vertexSet = verticesSeq.toSet

  vertexSeq.map(v => jungGraph.addVertex(v)) // TODO check return value

  ef(vertexSeq).map({
    case (vi, vj, ep) => {
      jungGraph.addEdge(Edge(ep, edgePayloadFunction), vi, vj) // TODO check return value
    }
  })

  def storage(): DirectedSparseGraph[Vertex[VP], Edge[EP, EP]] = jungGraph

  def size(): Int = jungGraph.getVertexCount()

  def source(edge: Edge[ES, EP]): Vertex[VP] = null // TODO

  def dest(edge: Edge[ES, EP]): Vertex[VP] = null // TODO

  def allEdges() = jungGraph.getEdges().asScala.toSet

  def verticesSeq() = vertexSeq

  def vertices() = vertexSet

  def findEdge(from: Vertex[VP], to: Vertex[VP]): Option[Edge[EP, EP]] = Option(jungGraph.findEdge(from, to))

  // TODO: findVertex needs an index
  def findVertex(f: Vertex[VP] => Boolean): Option[Vertex[VP]] = vertexSeq.find(f(_))

  def deleteEdge(e: Edge[EP, EP]) = filterEdges(_ != e)

  def deleteVertex(v: Vertex[VP]) = JungDirectedGraph(vertices().toSeq.filter(_ != v).map(_.payload), ef)

  def filterEdges(f: ((Vertex[VP], Vertex[VP], EP)) => Boolean) =
    JungDirectedGraph(vps, ((es: Seq[(Vertex[VP], Vertex[VP], EP)]) => es.filter(f(_))).compose(ef))

  def leaves(): Set[Vertex[VP]] = vertices().filter(isLeaf(_))

  def neighbors(v: Vertex[VP]): Set[Vertex[VP]] = jungGraph.getNeighbors(v).asScala.toSet

  def precedes(v1: Vertex[VP], v2: Vertex[VP]): Boolean = predecessors(v2).contains(v1)

  def predecessors(v: Vertex[VP]): Set[Vertex[VP]] = jungGraph.getPredecessors(v).asScala.toSet

  def isLeaf(v: Vertex[VP]): Boolean = jungGraph.getSuccessorCount(v) == 0

  def successors(v: Vertex[VP]): Set[Vertex[VP]] = jungGraph.getSuccessors(v).asScala.toSet

  def outputEdgesOf(v: Vertex[VP]): Set[Edge[EP, EP]] = jungGraph.getOutEdges(v).asScala.toSet

  def descendantsIntersectsSet(v: Vertex[VP], s: Set[Vertex[VP]]): Boolean =
    s.contains(v) || s.exists(x => descendantsIntersectsSet(x, s))

  def removeInputs(to: Set[Vertex[VP]]) = filterEdges(v => !to.contains(v._2))

  def removeOutputs(from: Set[Vertex[VP]]) = filterEdges(v => !from.contains(v._1))

  def moralGraph() = null // TODO !!!

  def isAcyclic() = true // TODO !!!

  def shortestPath(source: Vertex[VP], goal: Vertex[VP]): Option[List[Edge[EP, EP]]] = {
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
