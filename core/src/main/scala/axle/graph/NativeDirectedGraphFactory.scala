package axle.graph

import collection._
import scalaz._
import Scalaz._

trait NativeDirectedGraphFactory extends GenDirectedGraphFactory {

  def apply[VP, EP](): NativeDirectedGraph[VP, EP] = new NativeDirectedGraph[VP, EP]() {}

  def apply[VP, EP](vps: Seq[VP], ef: (Seq[NativeDirectedGraphVertex[VP]]) => Seq[(NativeDirectedGraphVertex[VP], NativeDirectedGraphVertex[VP], EP)]): NativeDirectedGraph[VP, EP] =
    new NativeDirectedGraphImpl[VP, EP](vps, ef)

}

object NativeDirectedGraph extends NativeDirectedGraphFactory

trait NativeDirectedGraphVertex[VP] extends DirectedGraphVertex[VP]

trait NativeDirectedGraphEdge[VP, EP] extends DirectedGraphEdge[VP, EP]

class NativeDirectedGraphVertexImpl[VP](vp: VP) extends NativeDirectedGraphVertex[VP] {
  def payload() = vp
}

class NativeDirectedGraphEdgeImpl[VP, EP](vi: NativeDirectedGraphVertex[VP], vj: NativeDirectedGraphVertex[VP], ep: EP)
  extends NativeDirectedGraphEdge[VP, EP] {
  def source(): NativeDirectedGraphVertex[VP] = vi
  def dest(): NativeDirectedGraphVertex[VP] = vj
  def payload() = ep
}

trait NativeDirectedGraph[VP, EP] extends GenDirectedGraph[VP, EP]

class NativeDirectedGraphImpl[VP, EP](vps: Seq[VP], ef: (Seq[NativeDirectedGraphVertex[VP]]) => Seq[(NativeDirectedGraphVertex[VP], NativeDirectedGraphVertex[VP], EP)])
  extends NativeDirectedGraph[VP, EP] {

  type V = NativeDirectedGraphVertex[VP]
  type E = NativeDirectedGraphEdge[VP, EP]

  type S = (Set[V], Set[E], Map[V, Set[E]], Map[V, Set[E]])

  lazy val _vertices: Seq[V] = vps.map(new NativeDirectedGraphVertexImpl(_))
  lazy val _verticesSet = _vertices.toSet

  lazy val _edges: Seq[E] = ef(_vertices).map({
    case (vi, vj, ep) => new NativeDirectedGraphEdgeImpl[VP, EP](vi, vj, ep)
  })

  lazy val _edgesSet = _edges.toSet

  lazy val vertex2outedges: Map[V, Set[E]] = _edges.groupBy(_.source).map({ case (k, v) => (k, v.toSet) }).withDefaultValue(Set[E]())
  lazy val vertex2inedges: Map[V, Set[E]] = _edges.groupBy(_.dest).map({ case (k, v) => (k, v.toSet) }).withDefaultValue(Set[E]())

  def storage(): S = (_verticesSet, _edgesSet, vertex2outedges, vertex2inedges)
  def vertices(): Set[V] = _verticesSet
  def edges(): Set[E] = _edgesSet
  def size(): Int = vps.size

  def findEdge(from: V, to: V): Option[E] = vertex2outedges(from).find(_.dest == to)

  def edge(source: V, dest: V, payload: EP): (NativeDirectedGraph[VP, EP], E) = 4

  def vertex(payload: VP): (NativeDirectedGraph[VP, EP], V) = 4

  def deleteEdge(e: E): NativeDirectedGraph[VP, EP] = {
    val filter = (vs: Seq[(NativeDirectedGraphVertex[VP], NativeDirectedGraphVertex[VP], EP)]) => vs.filter(_ != (e.source, e.dest, e.payload))
    NativeDirectedGraph(vps, filter.compose(ef))
  }

  def deleteVertex(v: V): NativeDirectedGraph[VP, EP] =
    NativeDirectedGraph(_vertices.filter(_ != v).map(_.payload), ef)

  def leaves(): Set[V] = vertices().filter(isLeaf(_))

  def neighbors(v: V): Set[V] = predecessors(v) ++ successors(v)

  def precedes(v1: V, v2: V): Boolean = predecessors(v2).contains(v1)

  def predecessors(v: V): Set[V] = vertex2inedges(v).map(_.source)

  def isLeaf(v: V): Boolean = vertex2outedges(v).size == 0

  def successors(v: V): Set[V] = vertex2outedges(v).map(_.dest)

  def outputEdgesOf(v: V): immutable.Set[E] = vertex2outedges(v).toSet

  def descendantsIntersectsSet(v: V, s: Set[V]): Boolean =
    s.contains(v) || s.exists(x => descendantsIntersectsSet(x, s))

  def removeInputs(to: Set[V]): NativeDirectedGraph[VP, EP] = {
    val filter = (vs: Seq[(NativeDirectedGraphVertex[VP], NativeDirectedGraphVertex[VP], EP)]) => vs.filter(v => !to.contains(v._2))
    NativeDirectedGraph(vps, filter.compose(ef))
  }

  def removeOutputs(from: Set[V]): NativeDirectedGraph[VP, EP] = {
    val filter = (vs: Seq[(NativeDirectedGraphVertex[VP], NativeDirectedGraphVertex[VP], EP)]) => vs.filter(v => !from.contains(v._1))
    NativeDirectedGraph(vps, filter.compose(ef))
  }

  def isAcyclic() = true // TODO !!!

  /**
   * shortestPath
   *
   * TODO: This is just a quick, dirty, slow, and naive algorithm.
   */

  def _shortestPath(source: V, goal: V, visited: Set[V]): Option[List[E]] = if (source == goal) {
    Some(List())
  } else {
    outputEdgesOf(source).filter(edge => !visited.contains(edge.dest))
      .flatMap(edge => _shortestPath(edge.dest, goal, visited + source).map(sp => edge :: sp))
      .reduceOption((l1, l2) => (l1.length < l2.length) ? l1 | l2)
  }

  def shortestPath(source: V, goal: V): Option[List[E]] = _shortestPath(source, goal, Set())

}
