package axle.algebra

import spire.algebra.Eq

trait UndirectedGraph[UG[_, _]] {

  def make[VP, EP](vps: Seq[VP], ef: Seq[Vertex[VP]] => Seq[(Vertex[VP], Vertex[VP], EP)]): UG[VP, EP]

  def vertices[VP, EP](jug: UG[VP, EP]): Iterable[Vertex[VP]]

  def edges[VP, EP](jug: UG[VP, EP]): Iterable[UndirectedEdge[VP, EP]]

  def size[VP, EP](jug: UG[VP, EP]): Int

  // TODO findVertex needs an index
  def findVertex[VP, EP](jug: UG[VP, EP], f: Vertex[VP] => Boolean): Option[Vertex[VP]]

  def filterEdges[VP, EP](jug: UG[VP, EP], f: UndirectedEdge[VP, EP] => Boolean): UG[VP, EP]

  //  def unlink(e: Edge[ES, EP]): UG[VP, EP]

  //  // UG[VP, EP]
  //  def unlink(v1: Vertex[VP], v2: Vertex[VP])

  def areNeighbors[VP: Eq, EP](jug: UG[VP, EP], v1: Vertex[VP], v2: Vertex[VP]): Boolean

  def isClique[VP: Eq, EP](jug: UG[VP, EP], vs: collection.GenTraversable[Vertex[VP]]): Boolean

  def forceClique[VP: Eq: Manifest, EP](jug: UG[VP, EP], among: Set[Vertex[VP]], payload: (Vertex[VP], Vertex[VP]) => EP): UG[VP, EP]

  def degree[VP, EP](jug: UG[VP, EP], v: Vertex[VP]): Int

  def edgesTouching[VP, EP](jug: UG[VP, EP], v: Vertex[VP]): Set[UndirectedEdge[VP, EP]]

  def neighbors[VP, EP](jug: UG[VP, EP], v: Vertex[VP]): Set[Vertex[VP]]

  //  def delete(v: Vertex[VP]): UG[VP, EP]

  // a "leaf" is vertex with only one neighbor
  def firstLeafOtherThan[VP: Eq, EP](jug: UG[VP, EP], r: Vertex[VP]): Option[Vertex[VP]]

  /**
   * "decompositions" page 3 (Definition 3, Section 9.3)
   * turn the neighbors of v into a clique
   */

  def eliminate[VP, EP](jug: UG[VP, EP], v: Vertex[VP], payload: (Vertex[VP], Vertex[VP]) => EP): UG[VP, EP]

  def other[VP: Eq, EP](jug: UG[VP, EP], edge: UndirectedEdge[VP, EP], u: Vertex[VP]): Vertex[VP]

  def connects[VP: Eq, EP](jug: UG[VP, EP], edge: UndirectedEdge[VP, EP], a1: Vertex[VP], a2: Vertex[VP]): Boolean

  def map[VP, EP, NVP, NEP](jug: UG[VP, EP], vpf: VP => NVP, epf: EP => NEP): UG[NVP, NEP]

  //  def numEdgesToForceClique(vs: collection.GenTraversable[Vertex[VP]], payload: (Vertex[VP], Vertex[VP]) => EP): Int =
  //    (for {
  //      vi <- vs
  //      vj <- vs
  //    } yield { if (areNeighbors(vi, vj)) 1 else 0 }).sum

  //
  //  // assert: among is a subset of vertices
  //  def vertexWithFewestEdgesToEliminateAmong(among: Set[Vertex[VP]], payload: (Vertex[VP], Vertex[VP]) => EP): Vertex[VP] =
  //    among.map(v => (v, numEdgesToForceClique(neighbors(v), payload))).minBy(_._2)._1
  //
  //  // assert: among is a subset of vertices
  //  def vertexWithFewestNeighborsAmong(among: Set[Vertex[VP]]): Vertex[VP] =
  //    among.map(v => (v, neighbors(v).size)).minBy(_._2)._1

}
