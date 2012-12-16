package axle.graph

import collection.JavaConverters._
import collection._
import axle._
import edu.uci.ics.jung.graph.UndirectedSparseGraph

class JungUndirectedGraph[VP, EP](
  vps: Seq[VP],
  ef: Seq[Vertex[VP]] => Seq[(Vertex[VP], Vertex[VP], EP)])
  extends UndirectedGraph[VP, EP] {

  type G[VP, EP] = JungUndirectedGraph[VP, EP]

  lazy val jungGraph = new UndirectedSparseGraph[Vertex[VP], Edge[EP]]()

  lazy val vertexSeq = vps.map(Vertex(_))

  vertexSeq.map(v => jungGraph.addVertex(v)) // TODO check return value

  lazy val vertexSet = vertexSeq.toSet

  ef(vertexSeq).map({
    case (vi, vj, ep) => {
      jungGraph.addEdge(Edge(ep), vi, vj) // TODO check return value
    }
  })

  def storage(): UndirectedSparseGraph[Vertex[VP], Edge[EP]] = jungGraph

  def vertices() = vertexSet

  def allEdges() = jungGraph.getEdges().asScala.toSet

  def size(): Int = jungGraph.getVertexCount()

  // TODO findVertex needs an index
  def findVertex(f: Vertex[VP] => Boolean) = vertexSeq.find(f(_))

  def filterEdges(f: ((Vertex[VP], Vertex[VP], EP)) => Boolean) = {
    val filter = (es: Seq[(Vertex[VP], Vertex[VP], EP)]) => es.filter(f(_))
    JungUndirectedGraph(vps, filter.compose(ef))
  }

  def unlink(e: Edge[EP]): JungUndirectedGraph[VP, EP] = filterEdges(_ != e)

  // JungUndirectedGraph[VP, EP]
  def unlink(v1: Vertex[VP], v2: Vertex[VP]) =
    filterEdges(e => (e._1 == v1 && e._2 == v2) || (e._2 == v1 && e._1 == v2))

  def areNeighbors(v1: Vertex[VP], v2: Vertex[VP]): Boolean = {
    edgesTouching(v1).exists(edge => connects(edge, v1, v2))
  }

  def forceClique(among: Set[Vertex[VP]], payload: (Vertex[VP], Vertex[VP]) => EP) = {

    val cliqued = (newVs: Seq[Vertex[VP]]) => {

      val old2new: Map[Vertex[VP], Vertex[VP]] = null // TODO _vertices.zip(newVs).toMap

      val newEdges = among.toIndexedSeq.permutations(2)
        .map({ case vi :: vj :: Nil => (vi, vj) })
        .filter({ case (vi, vj) => !areNeighbors(vi, vj) })
        .map({
          case (vi, vj) => {
            val newVi = old2new(vi)
            val newVj = old2new(vj)
            (newVi, newVj, payload(newVi, newVj))
          }
        })

      ef(newVs) ++ newEdges
    }

    JungUndirectedGraph(vps, cliqued(_))
  }

  def degree(v: Vertex[VP]): Int = edgesTouching(v).size

  def edgesTouching(v: Vertex[VP]) =
    jungGraph.getIncidentEdges(v).asScala.toSet

  def neighbors(v: Vertex[VP]) =
    jungGraph.getNeighbors(v).asScala.toSet

  def delete(v: Vertex[VP]) = JungUndirectedGraph(vertices().toSeq.filter(_ != v).map(_.payload), ef)

  // a "leaf" is vertex with only one neighbor
  def firstLeafOtherThan(r: Vertex[VP]) = vertices().find(v => neighbors(v).size == 1 && !v.equals(r))

  /**
   * "decompositions" page 3 (Definition 3, Section 9.3)
   * turn the neighbors of v into a clique
   */

  def eliminate(v: Vertex[VP], payload: (Vertex[VP], Vertex[VP]) => EP): JungUndirectedGraph[VP, EP] = {

    // TODO
    //    val vs = neighbors(v)
    //    makeFunctional.jungGraph.removeVertex(v)
    //    forceClique(vs, payload)
    null
  }

  def map[NVP, NEP](vpf: VP => NVP, epf: EP => NEP) = {

    val newVps = vps.map(vpf(_))

    val oldVs = null // TODO

    val newEf = (newVs: Seq[Vertex[NVP]]) =>
      ef(oldVs).map({
        case (vi, vj, ep) => (Vertex(vpf(vi.payload)), Vertex(vpf(vj.payload)), epf(ep))
      })

    JungUndirectedGraph(newVps, newEf)
  }

}

//class Edge[VP, EP](ep: EP) extends UndirectedGraphEdge[VP, EP] {
//
//  type V[VP] = JungVertex[VP]
//
//  def vertices(): (JungVertex[VP], JungVertex[VP]) = null // TODO (v1, v2)
//  def payload(): EP = ep
//}

object JungUndirectedGraph {

  def apply[VP, EP](vps: Seq[VP], ef: Seq[Vertex[VP]] => Seq[(Vertex[VP], Vertex[VP], EP)]) =
    new JungUndirectedGraph(vps, ef)

}

