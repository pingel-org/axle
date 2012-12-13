package axle.graph

import collection._
import scalaz._
import Scalaz._

case class NativeDirectedGraph[VP, EP](vps: Seq[VP], ef: Seq[NativeDirectedGraphVertex[VP]] => Seq[(NativeDirectedGraphVertex[VP], NativeDirectedGraphVertex[VP], EP)])
  extends DirectedGraph[VP, EP] {

  type G[VP, EP] = NativeDirectedGraph[VP, EP]
  type V[VP] = NativeDirectedGraphVertex[VP]
  type E[VP, EP] = NativeDirectedGraphEdge[VP, EP]

  lazy val _vertices: Seq[NativeDirectedGraphVertex[VP]] = vps.map(new NativeDirectedGraphVertex(_))

  lazy val _verticesSet = _vertices.toSet

  lazy val _edges: Seq[NativeDirectedGraphEdge[VP, EP]] = ef(_vertices).map({
    case (vi, vj, ep) => new NativeDirectedGraphEdge(vi, vj, ep)
  })

  lazy val _edgesSet = _edges.toSet

  lazy val vertex2outedges: Map[NativeDirectedGraphVertex[VP], Set[NativeDirectedGraphEdge[VP, EP]]] =
    _edges.groupBy(_.source).map({ case (k, v) => (k, v.toSet) }).withDefaultValue(Set[NativeDirectedGraphEdge[VP, EP]]())

  lazy val vertex2inedges: Map[NativeDirectedGraphVertex[VP], Set[NativeDirectedGraphEdge[VP, EP]]] =
    _edges.groupBy(_.dest).map({ case (k, v) => (k, v.toSet) }).withDefaultValue(Set[NativeDirectedGraphEdge[VP, EP]]())

  def storage() = (_verticesSet, _edgesSet, vertex2outedges, vertex2inedges)

  override def vertices(): Set[NativeDirectedGraphVertex[VP]] = _verticesSet

  override def edges(): Set[NativeDirectedGraphEdge[VP, EP]] = _edgesSet

  def size(): Int = vps.size

  def findEdge(from: NativeDirectedGraphVertex[VP], to: NativeDirectedGraphVertex[VP]): Option[NativeDirectedGraphEdge[VP, EP]] = vertex2outedges(from).find(_.dest == to)

  // TODO findVertex needs an index
  def findVertex(f: NativeDirectedGraphVertex[VP] => Boolean): Option[NativeDirectedGraphVertex[VP]] = _vertices.find(f(_))

  def deleteEdge(e: NativeDirectedGraphEdge[VP, EP]): NativeDirectedGraph[VP, EP] = filterEdges(_ != e)

  def deleteVertex(v: NativeDirectedGraphVertex[VP]): NativeDirectedGraph[VP, EP] =
    NativeDirectedGraph(_vertices.filter(_ != v).map(_.payload), ef)

  def leaves(): Set[NativeDirectedGraphVertex[VP]] = vertices().filter(isLeaf(_))

  def neighbors(v: NativeDirectedGraphVertex[VP]): Set[NativeDirectedGraphVertex[VP]] = predecessors(v) ++ successors(v)

  def precedes(v1: NativeDirectedGraphVertex[VP], v2: NativeDirectedGraphVertex[VP]): Boolean = predecessors(v2).contains(v1)

  def predecessors(v: NativeDirectedGraphVertex[VP]): Set[NativeDirectedGraphVertex[VP]] = vertex2inedges(v).map(_.source)

  def isLeaf(v: NativeDirectedGraphVertex[VP]): Boolean = vertex2outedges(v).size == 0

  def successors(v: NativeDirectedGraphVertex[VP]): Set[NativeDirectedGraphVertex[VP]] = vertex2outedges(v).map(_.dest)

  def outputEdgesOf(v: NativeDirectedGraphVertex[VP]): Set[NativeDirectedGraphEdge[VP, EP]] = vertex2outedges(v).toSet

  def descendantsIntersectsSet(v: NativeDirectedGraphVertex[VP], s: Set[NativeDirectedGraphVertex[VP]]): Boolean =
    s.contains(v) || s.exists(x => descendantsIntersectsSet(x, s))

  def filterEdges(f: ((NativeDirectedGraphVertex[VP], NativeDirectedGraphVertex[VP], EP)) => Boolean): NativeDirectedGraph[VP, EP] = {
    val filter = (es: Seq[(NativeDirectedGraphVertex[VP], NativeDirectedGraphVertex[VP], EP)]) => es.filter(f(_))
    NativeDirectedGraph(vps, filter.compose(ef))
  }

  def removeInputs(to: Set[NativeDirectedGraphVertex[VP]]): NativeDirectedGraph[VP, EP] = filterEdges(v => !to.contains(v._2))

  def removeOutputs(from: Set[NativeDirectedGraphVertex[VP]]): NativeDirectedGraph[VP, EP] = filterEdges(v => !from.contains(v._1))

  def isAcyclic() = true // TODO !!!

  /**
   * shortestPath
   *
   * TODO: This is just a quick, dirty, slow, and naive algorithm.
   */

  def _shortestPath(source: NativeDirectedGraphVertex[VP], goal: NativeDirectedGraphVertex[VP], visited: Set[NativeDirectedGraphVertex[VP]]): Option[List[NativeDirectedGraphEdge[VP, EP]]] = if (source == goal) {
    Some(List())
  } else {
    null
    // TODO
    //    outputEdgesOf(source).filter(edge => !visited.contains(edge.dest))
    //      .flatMap(edge => _shortestPath(edge.dest, goal, visited + source).map(sp => edge :: sp))
    //      .reduceOption((l1, l2) => (l1.length < l2.length) ? l1 | l2)
  }

  def shortestPath(source: NativeDirectedGraphVertex[VP], goal: NativeDirectedGraphVertex[VP]): Option[List[NativeDirectedGraphEdge[VP, EP]]] = _shortestPath(source, goal, Set())

}

class NativeDirectedGraphEdge[VP, EP](vi: NativeDirectedGraphVertex[VP], vj: NativeDirectedGraphVertex[VP], ep: EP) extends DirectedGraphEdge[VP, EP] {

  type V[VP] = NativeDirectedGraphVertex[VP]
  
  def source(): NativeDirectedGraphVertex[VP] = vi
  def dest(): NativeDirectedGraphVertex[VP] = vj
  def payload() = ep
}

class NativeDirectedGraphVertex[VP](vp: VP) extends DirectedGraphVertex[VP] {
  def payload() = vp
}

trait NativeDirectedGraphFactory extends DirectedGraphFactory {

  type G[VP, EP] = NativeDirectedGraph[VP, EP]
  type V[VP] = NativeDirectedGraphVertex[VP]

  // type S = (Set[NativeDirectedGraphVertex[VP]], Set[NativeDirectedGraphEdge[VP, EP]], Map[NativeDirectedGraphVertex[VP], Set[NativeDirectedGraphEdge[VP, EP]]], Map[NativeDirectedGraphVertex[VP], Set[NativeDirectedGraphEdge[VP, EP]]])

  override def apply[VP, EP](vps: Seq[VP], ef: Seq[NativeDirectedGraphVertex[VP]] => Seq[(NativeDirectedGraphVertex[VP], NativeDirectedGraphVertex[VP], EP)]) = new NativeDirectedGraph(vps, ef)
}

object NativeDirectedGraph extends NativeDirectedGraphFactory
