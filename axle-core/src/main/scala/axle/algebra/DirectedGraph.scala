package axle.algebra

import spire.algebra.Eq
import scala.annotation.implicitNotFound

@implicitNotFound("No member of typeclass DirectedGraph found for type ${DG}")
trait DirectedGraph[DG[_, _]] {

  def make[VP, EP](vps: Seq[VP], ef: Seq[Vertex[VP]] => Seq[(Vertex[VP], Vertex[VP], EP)]): DG[VP, EP]

  def vertices[VP, EP](jdg: DG[VP, EP]): Iterable[Vertex[VP]]

  def edges[VP, EP](jdg: DG[VP, EP]): Iterable[DirectedEdge[VP, EP]]

  def size[VP, EP](jdg: DG[VP, EP]): Int

  // TODO findVertex needs an index
  def findVertex[VP, EP](jdg: DG[VP, EP], f: Vertex[VP] => Boolean): Option[Vertex[VP]]

  def filterEdges[VP, EP](jdg: DG[VP, EP], f: DirectedEdge[VP, EP] => Boolean): DG[VP, EP]

  def areNeighbors[VP: Eq, EP](jdg: DG[VP, EP], v1: Vertex[VP], v2: Vertex[VP]): Boolean

  def isClique[VP: Eq, EP](jdg: DG[VP, EP], vs: collection.GenTraversable[Vertex[VP]]): Boolean

  def forceClique[VP: Eq: Manifest, EP](jdg: DG[VP, EP], among: Set[Vertex[VP]], payload: (Vertex[VP], Vertex[VP]) => EP): DG[VP, EP]

  def degree[VP, EP](jdg: DG[VP, EP], v: Vertex[VP]): Int

  def edgesTouching[VP, EP](jdg: DG[VP, EP], v: Vertex[VP]): Set[DirectedEdge[VP, EP]]

  def neighbors[VP, EP](jdg: DG[VP, EP], v: Vertex[VP]): Set[Vertex[VP]]

  // a "leaf" is vertex with only one neighbor
  def firstLeafOtherThan[VP: Eq, EP](jdg: DG[VP, EP], r: Vertex[VP]): Option[Vertex[VP]]

  def eliminate[VP, EP](jdg: DG[VP, EP], v: Vertex[VP], payload: (Vertex[VP], Vertex[VP]) => EP): DG[VP, EP]

  def other[VP: Eq, EP](jdg: DG[VP, EP], edge: DirectedEdge[VP, EP], u: Vertex[VP]): Vertex[VP]

  def connects[VP: Eq, EP](jdg: DG[VP, EP], edge: DirectedEdge[VP, EP], a1: Vertex[VP], a2: Vertex[VP]): Boolean

  def map[VP, EP, NVP, NEP](jdg: DG[VP, EP], vpf: VP => NVP, epf: EP => NEP): DG[NVP, NEP]

  def leaves[VP: Eq, EP](jdg: DG[VP, EP]): Set[Vertex[VP]]

  def precedes[VP, EP](jdg: DG[VP, EP], v1: Vertex[VP], v2: Vertex[VP]): Boolean

  def predecessors[VP, EP](jdg: DG[VP, EP], v: Vertex[VP]): Set[Vertex[VP]]

  def isLeaf[VP, EP](jdg: DG[VP, EP], v: Vertex[VP]): Boolean

  def successors[VP, EP](jdg: DG[VP, EP], v: Vertex[VP]): Set[Vertex[VP]]

  def outputEdgesOf[VP, EP](jdg: DG[VP, EP], v: Vertex[VP]): Set[DirectedEdge[VP, EP]]

  def descendantsIntersectsSet[VP, EP](jdg: DG[VP, EP], v: Vertex[VP], s: Set[Vertex[VP]]): Boolean

  def removeInputs[VP, EP](jdg: DG[VP, EP], to: Set[Vertex[VP]]): DG[VP, EP]

  def removeOutputs[VP, EP](jdg: DG[VP, EP], from: Set[Vertex[VP]]): DG[VP, EP]

  def moralGraph[VP, EP](jdg: DG[VP, EP]): Boolean

  def isAcyclic[VP, EP](jdg: DG[VP, EP]): Boolean

  def shortestPath[VP: Eq, EP](jdg: DG[VP, EP], source: Vertex[VP], goal: Vertex[VP]): Option[List[DirectedEdge[VP, EP]]]

  // inefficient
  def _descendants[VP, EP](jdg: DG[VP, EP], v: Vertex[VP], accumulator: Set[Vertex[VP]]): Set[Vertex[VP]] =
    if (!accumulator.contains(v)) {
      successors(jdg, v).foldLeft(accumulator + v)((a, v) => _descendants(jdg, v, a))
    } else {
      accumulator
    }

  def descendants[VP, EP](jdg: DG[VP, EP], v: Vertex[VP]): Set[Vertex[VP]] =
    _descendants(jdg, v, Set[Vertex[VP]]())

  //  // inefficient
  //  def _ancestors(v: Vertex[VP], accumulator: Set[Vertex[VP]]): Set[Vertex[VP]] =
  //    if (!accumulator.contains(v)) {
  //      predecessors(v).foldLeft(accumulator + v)((a, v) => _ancestors(v, a))
  //    } else {
  //      accumulator
  //    }

  //  def ancestors(v: Vertex[VP]): Set[Vertex[VP]] = _ancestors(v, Set[Vertex[VP]]())

  //  def ancestors(vs: Set[Vertex[VP]]): Set[Vertex[VP]] =
  //    vs.foldLeft(Set[Vertex[VP]]())((a, v) => _ancestors(v, a))

}
