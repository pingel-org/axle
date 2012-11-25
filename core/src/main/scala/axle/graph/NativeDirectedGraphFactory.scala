package axle.graph

import collection._
import scalaz._
import Scalaz._

trait NativeDirectedGraphFactory extends DirectedGraphFactory {

  type G[VP, EP] = NativeDirectedGraph[VP, EP]
  type V[VP] = NativeDirectedGraphVertex[VP]
  type E[VP, EP] = NativeDirectedGraphEdge[VP, EP]

  // type S = (Set[NativeDirectedGraphVertex[VP]], Set[NativeDirectedGraphEdge[VP, EP]], Map[NativeDirectedGraphVertex[VP], Set[NativeDirectedGraphEdge[VP, EP]]], Map[NativeDirectedGraphVertex[VP], Set[NativeDirectedGraphEdge[VP, EP]]])

  override def apply[VP, EP](vps: Seq[VP], ef: Seq[V[VP]] => Seq[(V[VP], V[VP], EP)]) = new NativeDirectedGraph(vps, ef)

  case class NativeDirectedGraph[VP, EP](vps: Seq[VP], ef: Seq[V[VP]] => Seq[(V[VP], V[VP], EP)])
    extends DirectedGraph[VP, EP] {

    lazy val _vertices: Seq[V[VP]] = vps.map(new NativeDirectedGraphVertex(_))

    lazy val _verticesSet = _vertices.toSet

    lazy val _edges: Seq[E[VP, EP]] = ef(_vertices).map({
      case (vi, vj, ep) => new NativeDirectedGraphEdge(vi, vj, ep)
    })

    lazy val _edgesSet = _edges.toSet

    lazy val vertex2outedges: Map[V[VP], Set[E[VP, EP]]] =
      _edges.groupBy(_.source).map({ case (k, v) => (k, v.toSet) }).withDefaultValue(Set[E[VP, EP]]())

    lazy val vertex2inedges: Map[V[VP], Set[E[VP, EP]]] =
      _edges.groupBy(_.dest).map({ case (k, v) => (k, v.toSet) }).withDefaultValue(Set[E[VP, EP]]())

    def storage() = (_verticesSet, _edgesSet, vertex2outedges, vertex2inedges)

    override def vertices(): Set[V[VP]] = _verticesSet

    override def edges(): Set[E[VP, EP]] = _edgesSet

    def size(): Int = vps.size

    def findEdge(from: V[VP], to: V[VP]): Option[E[VP, EP]] = vertex2outedges(from).find(_.dest == to)

    // TODO findVertex needs an index
    def findVertex(f: V[VP] => Boolean): Option[V[VP]] = _vertices.find(f(_))

    def deleteEdge(e: E[VP, EP]): G[VP, EP] = filterEdges(_ != e)

    def deleteVertex(v: V[VP]): G[VP, EP] =
      NativeDirectedGraph(_vertices.filter(_ != v).map(_.payload), ef)

    def leaves(): Set[V[VP]] = vertices().filter(isLeaf(_))

    def neighbors(v: V[VP]): Set[V[VP]] = predecessors(v) ++ successors(v)

    def precedes(v1: V[VP], v2: V[VP]): Boolean = predecessors(v2).contains(v1)

    def predecessors(v: V[VP]): Set[V[VP]] = vertex2inedges(v).map(_.source)

    def isLeaf(v: V[VP]): Boolean = vertex2outedges(v).size == 0

    def successors(v: V[VP]): Set[V[VP]] = vertex2outedges(v).map(_.dest)

    def outputEdgesOf(v: V[VP]): Set[E[VP, EP]] = vertex2outedges(v).toSet

    def descendantsIntersectsSet(v: V[VP], s: Set[V[VP]]): Boolean =
      s.contains(v) || s.exists(x => descendantsIntersectsSet(x, s))

    def filterEdges(f: ((V[VP], V[VP], EP)) => Boolean): G[VP, EP] = {
      val filter = (es: Seq[(V[VP], V[VP], EP)]) => es.filter(f(_))
      NativeDirectedGraph(vps, filter.compose(ef))
    }

    def removeInputs(to: Set[V[VP]]): G[VP, EP] = filterEdges(v => !to.contains(v._2))

    def removeOutputs(from: Set[V[VP]]): G[VP, EP] = filterEdges(v => !from.contains(v._1))

    def isAcyclic() = true // TODO !!!

    /**
     * shortestPath
     *
     * TODO: This is just a quick, dirty, slow, and naive algorithm.
     */

    def _shortestPath(source: V[VP], goal: V[VP], visited: Set[V[VP]]): Option[List[E[VP, EP]]] = if (source == goal) {
      Some(List())
    } else {
      null
      // TODO
      //    outputEdgesOf(source).filter(edge => !visited.contains(edge.dest))
      //      .flatMap(edge => _shortestPath(edge.dest, goal, visited + source).map(sp => edge :: sp))
      //      .reduceOption((l1, l2) => (l1.length < l2.length) ? l1 | l2)
    }

    def shortestPath(source: V[VP], goal: V[VP]): Option[List[E[VP, EP]]] = _shortestPath(source, goal, Set())

  }

  class NativeDirectedGraphEdge[VP, EP](vi: V[VP], vj: V[VP], ep: EP) extends DirectedGraphEdge[VP, EP] {
    def source(): V[VP] = vi
    def dest(): V[VP] = vj
    def payload() = ep
  }

  class NativeDirectedGraphVertex[VP](vp: VP) extends DirectedGraphVertex[VP] {
    def payload() = vp
  }

}

object NativeDirectedGraph extends NativeDirectedGraphFactory
