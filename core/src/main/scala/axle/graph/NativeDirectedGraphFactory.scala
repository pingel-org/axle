package axle.graph

import collection._
import scalaz._
import Scalaz._

class NativeDirectedGraph[VP, EP](vps: Seq[VP], ef: Seq[Vertex[VP]] => Seq[(Vertex[VP], Vertex[VP], EP)])
  extends DirectedGraph[VP, EP] {

  type G[VP, EP] = NativeDirectedGraph[VP, EP]

  lazy val _vertices = vps.map(new Vertex(_))

  lazy val _verticesSet = _vertices.toSet

  lazy val _edges = ef(_vertices).map({ case (vi, vj, ep) => Edge(vi, vj, ep) })

  lazy val _edgesSet = _edges.toSet

  lazy val vertex2outedges: Map[Vertex[VP], Set[Edge[EP]]] =
    _edges.groupBy(_.source).map({ case (k, v) => (k, v.toSet) }).withDefaultValue(Set[Edge[VP, EP]]())

  lazy val vertex2inedges: Map[Vertex[VP], Set[Edge[EP]]] =
    _edges.groupBy(_.dest).map({ case (k, v) => (k, v.toSet) }).withDefaultValue(Set[Edge[VP, EP]]())

  def storage() = (_verticesSet, _edgesSet, vertex2outedges, vertex2inedges)

  def vertices(): Set[Vertex[VP]] = _verticesSet

  def allEdges() = _edgesSet

  def size(): Int = vps.size

  def findEdge(from: Vertex[VP], to: Vertex[VP]): Option[Edge[EP]] = vertex2outedges(from).find(_.dest == to)

  // TODO findVertex needs an index
  def findVertex(f: Vertex[VP] => Boolean): Option[Vertex[VP]] = _vertices.find(f(_))

  def deleteEdge(e: Edge[EP]): NativeDirectedGraph[VP, EP] = filterEdges(_ != e)

  def deleteVertex(v: Vertex[VP]): NativeDirectedGraph[VP, EP] =
    NativeDirectedGraph(_vertices.filter(_ != v).map(_.payload), ef)

  def leaves(): Set[Vertex[VP]] = vertices().filter(isLeaf(_))

  def neighbors(v: Vertex[VP]): Set[Vertex[VP]] = predecessors(v) ++ successors(v)

  def precedes(v1: Vertex[VP], v2: Vertex[VP]): Boolean = predecessors(v2).contains(v1)

  def predecessors(v: Vertex[VP]): Set[Vertex[VP]] = vertex2inedges(v).map(_.source)

  def isLeaf(v: Vertex[VP]): Boolean = vertex2outedges(v).size == 0

  def successors(v: Vertex[VP]): Set[Vertex[VP]] = vertex2outedges(v).map(_.dest)

  def outputEdgesOf(v: Vertex[VP]): Set[Edge[EP]] = vertex2outedges(v).toSet

  def descendantsIntersectsSet(v: Vertex[VP], s: Set[Vertex[VP]]): Boolean =
    s.contains(v) || s.exists(x => descendantsIntersectsSet(x, s))

  def filterEdges(f: ((Vertex[VP], Vertex[VP], EP)) => Boolean): NativeDirectedGraph[VP, EP] = {
    val filter = (es: Seq[(Vertex[VP], Vertex[VP], EP)]) => es.filter(f(_))
    NativeDirectedGraph(vps, filter.compose(ef))
  }

  def removeInputs(to: Set[Vertex[VP]]): NativeDirectedGraph[VP, EP] = filterEdges(v => !to.contains(v._2))

  def removeOutputs(from: Set[Vertex[VP]]): NativeDirectedGraph[VP, EP] = filterEdges(v => !from.contains(v._1))

  def isAcyclic() = true // TODO !!!

  /**
   * shortestPath
   *
   * TODO: This is just a quick, dirty, slow, and naive algorithm.
   */

  def _shortestPath(source: Vertex[VP], goal: Vertex[VP], visited: Set[Vertex[VP]]): Option[List[Edge[EP]]] = if (source == goal) {
    Some(List())
  } else {
    null
    // TODO
    //    outputEdgesOf(source).filter(edge => !visited.contains(edge.dest))
    //      .flatMap(edge => _shortestPath(edge.dest, goal, visited + source).map(sp => edge :: sp))
    //      .reduceOption((l1, l2) => (l1.length < l2.length) ? l1 | l2)
  }

  def shortestPath(source: Vertex[VP], goal: Vertex[VP]): Option[List[Edge[EP]]] = _shortestPath(source, goal, Set())

}

//class Edge[VP, EP](vi: Vertex[VP], vj: Vertex[VP], ep: EP) extends DirectedGraphEdge[VP, EP] {
//
//  type V[VP] = Vertex[VP]
//
//  def source(): Vertex[VP] = vi
//  def dest(): Vertex[VP] = vj
//  def payload() = ep
//}

object NativeDirectedGraph {

  // type S = (Set[Vertex[VP]], Set[Edge[VP, EP]], Map[Vertex[VP], Set[Edge[VP, EP]]], Map[Vertex[VP], Set[Edge[VP, EP]]])

  def apply[VP, EP](vps: Seq[VP], ef: Seq[Vertex[VP]] => Seq[(Vertex[VP], Vertex[VP], EP)]) =
    new NativeDirectedGraph(vps, ef)
}

