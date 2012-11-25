package axle.graph

import collection.JavaConverters._
import collection._

trait JungDirectedGraphFactory extends DirectedGraphFactory {

  type G[VP, EP] = JungDirectedGraph[VP, EP]
  type V[VP] = JungDirectedGraphVertex[VP]
  type E[VP, EP] = JungDirectedGraphEdge[VP, EP]
  // type S = DirectedSparseGraph[JungDirectedGraphVertex[VP], JungDirectedGraphEdge[VP, EP]]

  override def apply[VP, EP](vps: Seq[VP], ef: Seq[V[VP]] => Seq[(V[VP], V[VP], EP)]): G[VP, EP] =
    new JungDirectedGraph(vps, ef)

  case class JungDirectedGraph[VP, EP](vps: Seq[VP], ef: Seq[V[VP]] => Seq[(V[VP], V[VP], EP)])
    extends DirectedGraph[VP, EP] {

    import edu.uci.ics.jung.graph.DirectedSparseGraph

    lazy val jungGraph = new DirectedSparseGraph[V[VP], E[VP, EP]]()

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

    def storage(): DirectedSparseGraph[V[VP], E[VP, EP]] = jungGraph

    def size(): Int = jungGraph.getVertexCount()

    override def edges(): Set[E[VP, EP]] = jungGraph.getEdges().asScala.toSet

    def verticesSeq(): Seq[V[VP]] = vertexSeq

    override def vertices(): Set[V[VP]] = vertexSet

    def findEdge(from: V[VP], to: V[VP]): Option[E[VP, EP]] = Option(jungGraph.findEdge(from, to))

    // TODO: findVertex needs an index
    // TOOD: findVertex f should probably take JungDirectedGraphVertex[VP]
    // def findVertex(f: VP => Boolean): Option[JungDirectedGraphVertex[VP]] = vertices().find(v => f(v.payload))
    def findVertex(f: V[VP] => Boolean): Option[V[VP]] = vertices().find(f(_))

    def deleteEdge(e: E[VP, EP]): G[VP, EP] = filterEdges(_ != e)

    def deleteVertex(v: V[VP]): G[VP, EP] =
      JungDirectedGraph(vertices().toSeq.filter(_ != v).map(_.payload), ef)

    def filterEdges(f: ((V[VP], V[VP], EP)) => Boolean): G[VP, EP] = {
      val filter = (es: Seq[(V[VP], V[VP], EP)]) => es.filter(f(_))
      JungDirectedGraph(vps, filter.compose(ef))
    }

    def leaves(): Set[V[VP]] = vertices().filter(isLeaf(_))

    def neighbors(v: V[VP]): Set[V[VP]] = jungGraph.getNeighbors(v).asScala.toSet

    def precedes(v1: V[VP], v2: V[VP]): Boolean = predecessors(v2).contains(v1)

    def predecessors(v: V[VP]): Set[V[VP]] = jungGraph.getPredecessors(v).asScala.toSet

    def isLeaf(v: JungDirectedGraphVertex[VP]): Boolean = jungGraph.getSuccessorCount(v) == 0

    def successors(v: V[VP]): Set[V[VP]] = jungGraph.getSuccessors(v).asScala.toSet

    def outputEdgesOf(v: V[VP]): Set[E[VP, EP]] = jungGraph.getOutEdges(v).asScala.toSet

    def descendantsIntersectsSet(v: V[VP], s: Set[V[VP]]): Boolean =
      s.contains(v) || s.exists(x => descendantsIntersectsSet(x, s))

    def removeInputs(to: Set[V[VP]]): G[VP, EP] = filterEdges(v => !to.contains(v._2))

    def removeOutputs(from: Set[V[VP]]): G[VP, EP] = filterEdges(v => !from.contains(v._1))

    def moralGraph(): G[_, _] = null // TODO !!!

    def isAcyclic() = true // TODO !!!

    def shortestPath(source: V[VP], goal: V[VP]): Option[List[E[VP, EP]]] = {
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

    def map[NVP, NEP](vpf: VP => NVP, epf: EP => NEP): G[NVP, NEP] = {

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
    def source(): V[VP] = null // TODO
    def dest(): V[VP] = null // TODO
    def payload(): EP = ep
  }

  class JungDirectedGraphVertex[VP](vp: VP) extends DirectedGraphVertex[VP] {
    def payload(): VP = vp
  }

}

object JungDirectedGraph extends JungDirectedGraphFactory
