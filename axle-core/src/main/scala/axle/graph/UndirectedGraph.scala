package axle.graph

import axle._

import spire.algebra._
import spire.implicits._

trait UndirectedGraph[VP, EP] {

  type G[VP, EP] <: UndirectedGraph[VP, EP]

  type ES

  def vertexPayloads: Seq[VP]
  def edgeFunction: Seq[Vertex[VP]] => Seq[(Vertex[VP], Vertex[VP], EP)]

  def vertices: Set[Vertex[VP]]
  def allEdges: Set[Edge[ES, EP]]

  def findVertex(f: Vertex[VP] => Boolean): Option[Vertex[VP]]
  //  def unlink(e: Edge[ES, EP]): G[VP, EP]
  //  def unlink(v1: Vertex[VP], v2: Vertex[VP]): G[VP, EP]
  def areNeighbors(v1: Vertex[VP], v2: Vertex[VP]): Boolean

  def isClique(vs: collection.GenTraversable[Vertex[VP]]): Boolean

  def numEdgesToForceClique(vs: collection.GenTraversable[Vertex[VP]], payload: (Vertex[VP], Vertex[VP]) => EP): Int =
    (for {
      vi <- vs
      vj <- vs
    } yield { if (areNeighbors(vi, vj)) 1 else 0 }).sum

  def forceClique(vs: Set[Vertex[VP]], payload: (Vertex[VP], Vertex[VP]) => EP): G[VP, EP]

  // assert: among is a subset of vertices
  def vertexWithFewestEdgesToEliminateAmong(among: Set[Vertex[VP]], payload: (Vertex[VP], Vertex[VP]) => EP): Vertex[VP] =
    among.map(v => (v, numEdgesToForceClique(neighbors(v), payload))).minBy(_._2)._1

  // assert: among is a subset of vertices
  def vertexWithFewestNeighborsAmong(among: Set[Vertex[VP]]): Vertex[VP] =
    among.map(v => (v, neighbors(v).size)).minBy(_._2)._1

  def degree(v: Vertex[VP]): Int

  def edgesTouching(v: Vertex[VP]): Set[Edge[ES, EP]]

  def neighbors(v: Vertex[VP]): Set[Vertex[VP]]

  def delete(v: Vertex[VP]): G[VP, EP]

  // a "leaf" is vertex with only one neighbor
  def firstLeafOtherThan(r: Vertex[VP]): Option[Vertex[VP]]

  def eliminate(v: Vertex[VP], payload: (Vertex[VP], Vertex[VP]) => EP): G[VP, EP]

  // def eliminate(vs: List[V], payload: (V, V) => EP): GenUndirectedGraph[VP, EP]

  def vertices(edge: Edge[ES, EP]): (Vertex[VP], Vertex[VP])

  def other(edge: Edge[ES, EP], u: Vertex[VP]): Vertex[VP]

  def connects(edge: Edge[ES, EP], a1: Vertex[VP], a2: Vertex[VP]): Boolean

  def map[NVP: Manifest: Eq, NEP: Eq](vpf: VP => NVP, epf: EP => NEP): G[NVP, NEP]

}
