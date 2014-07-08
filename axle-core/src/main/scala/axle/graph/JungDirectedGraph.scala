package axle.graph

import scala.collection.JavaConverters.asScalaBufferConverter
import scala.collection.JavaConverters.collectionAsScalaIterableConverter

import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath
import edu.uci.ics.jung.graph.DirectedSparseGraph
import spire.algebra.Eq
import spire.implicits.IntAlgebra
import spire.implicits.eqOps

case class JungDirectedGraph[VP: Eq, EP: Eq](
  vps: Seq[VP],
  ef: Seq[Vertex[VP]] => Seq[(Vertex[VP], Vertex[VP], EP)])
  extends DirectedGraph[VP, EP] {

  type G[VP, EP] = JungDirectedGraph[VP, EP]
  type ES = (Vertex[VP], Vertex[VP], EP)

  // Note: ES includes the vertices in order to provide uniqueness for jung
  // This could also be accomplished by making Edge not a case class

  val edgePayloadFunction = (es: ES) => es._3

  lazy val jungGraph = new DirectedSparseGraph[Vertex[VP], Edge[ES, EP]]()

  // Note: Have to compensate for JUNG not preserving vertex order
  // ...which defeats some of the purpose.  At least this is lazy:
  lazy val vertexSeq = vps.map(Vertex(_))

  lazy val vertexSet = verticesSeq.toSet

  vertexSeq foreach { jungGraph.addVertex(_) } // TODO check return value

  ef(vertexSeq) foreach {
    case (vi, vj, ep) =>
      jungGraph.addEdge(Edge((vi, vj, ep), edgePayloadFunction), vi, vj) // TODO check return value
  }

  def storage: DirectedSparseGraph[Vertex[VP], Edge[ES, EP]] = jungGraph

  def size: Int = jungGraph.getVertexCount

  def source(edge: Edge[ES, EP]): Vertex[VP] = edge.storage._1

  def dest(edge: Edge[ES, EP]): Vertex[VP] = edge.storage._2

  def vertexPayloads: Seq[VP] = vps

  def edgeFunction: Seq[Vertex[VP]] => Seq[(Vertex[VP], Vertex[VP], EP)] = ef

  // TODO: Clean up allEdges type
  def allEdges: Set[Edge[(Vertex[VP], Vertex[VP], EP), EP]] = jungGraph.getEdges.asScala.toSet

  def verticesSeq: Seq[Vertex[VP]] = vertexSeq

  def vertices: Set[Vertex[VP]] = vertexSet

  def findEdge(from: Vertex[VP], to: Vertex[VP]): Option[Edge[ES, EP]] = Option(jungGraph.findEdge(from, to))

  // TODO: findVertex needs an index
  def findVertex(f: Vertex[VP] => Boolean): Option[Vertex[VP]] = vertexSeq.find(f)

  //  def deleteEdge(e: Edge[ES, EP]) = filterEdges(t => !((source(e), dest(e), e.payload) === t))
  //
  //  def deleteVertex(v: Vertex[VP]) = JungDirectedGraph(vertices().toSeq.filter(_ != v).map(_.payload), ef)

  def filterEdges(f: ((Vertex[VP], Vertex[VP], EP)) => Boolean): JungDirectedGraph[VP, EP] =
    JungDirectedGraph(vps, ((es: Seq[(Vertex[VP], Vertex[VP], EP)]) => es.filter(f)).compose(ef))

  def leaves: Set[Vertex[VP]] = vertices.filter(isLeaf)

  def neighbors(v: Vertex[VP]): Set[Vertex[VP]] = jungGraph.getNeighbors(v).asScala.toSet

  def precedes(v1: Vertex[VP], v2: Vertex[VP]): Boolean = predecessors(v2).contains(v1)

  def predecessors(v: Vertex[VP]): Set[Vertex[VP]] = jungGraph.getPredecessors(v).asScala.toSet

  def isLeaf(v: Vertex[VP]): Boolean = jungGraph.getSuccessorCount(v) === 0

  def successors(v: Vertex[VP]): Set[Vertex[VP]] = jungGraph.getSuccessors(v).asScala.toSet

  def outputEdgesOf(v: Vertex[VP]): Set[Edge[ES, EP]] = jungGraph.getOutEdges(v).asScala.toSet

  def descendantsIntersectsSet(v: Vertex[VP], s: Set[Vertex[VP]]): Boolean =
    s.contains(v) || s.exists(x => descendantsIntersectsSet(x, s))

  def removeInputs(to: Set[Vertex[VP]]): JungDirectedGraph[VP, EP] = filterEdges(v => !to.contains(v._2))

  def removeOutputs(from: Set[Vertex[VP]]): JungDirectedGraph[VP, EP] = filterEdges(v => !from.contains(v._1))

  def moralGraph: Boolean = ???

  def isAcyclic: Boolean = ???

  def shortestPath(source: Vertex[VP], goal: Vertex[VP]): Option[List[Edge[ES, EP]]] = {
    if (source === goal) {
      Some(Nil)
    } else {
      Option((new DijkstraShortestPath(jungGraph)).getPath(source, goal)) flatMap { path =>
        if (path.size === 0)
          None
        else
          Some(path.asScala.toList)
      }
    }
  }

  def vertexToVisualizationHtml(vp: VP): xml.Node = vp match {
    case x: axle.XmlAble => x.toXml
    case _ => xml.Text(vp.toString)
  }

  def map[NVP: Manifest: Eq, NEP: Eq](vpf: VP => NVP, epf: EP => NEP): JungDirectedGraph[NVP, NEP] =
    JungDirectedGraph(vps.map(vpf),
      (newVs: Seq[Vertex[NVP]]) =>
        ef(vertexSeq).map({
          case (vi, vj, ep) => (Vertex(vpf(vi.payload)), Vertex(vpf(vj.payload)), epf(ep))
        }))

}
