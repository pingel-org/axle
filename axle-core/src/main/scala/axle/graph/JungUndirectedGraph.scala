package axle.graph

import collection.JavaConverters._
import axle._
import axle.algebra._
import spire.implicits._
import spire.algebra._
import edu.uci.ics.jung.graph.UndirectedSparseGraph

case class JungUndirectedGraph[VP: Manifest : Eq, EP: Eq](
  vps: Seq[VP],
  ef: Seq[Vertex[VP]] => Seq[(Vertex[VP], Vertex[VP], EP)])
  extends UndirectedGraph[VP, EP] {

  type G[VP, EP] = JungUndirectedGraph[VP, EP]
  type ES = (Vertex[VP], Vertex[VP], EP)

  // Note: ES includes the vertices in order to provide uniquess for jung
  // This could also be accomplished by making Edge not a case class

  val edgePayloadFunction = (es: ES) => es._3

  lazy val jungGraph = new UndirectedSparseGraph[Vertex[VP], Edge[ES, EP]]()

  lazy val vertexSeq = vps.map(Vertex(_))

  vertexSeq.map(v => jungGraph.addVertex(v)) // TODO check return value

  lazy val vertexSet = vertexSeq.toSet

  ef(vertexSeq).map({
    case (vi, vj, ep) => {
      jungGraph.addEdge(Edge((vi, vj, ep), edgePayloadFunction), vi, vj) // TODO check return value
    }
  })

  def storage(): UndirectedSparseGraph[Vertex[VP], Edge[ES, EP]] = jungGraph

  def vertexPayloads() = vps

  def edgeFunction() = ef

  def vertices() = vertexSet

  def allEdges() = jungGraph.getEdges().asScala.toSet

  def size(): Int = jungGraph.getVertexCount()

  def vertices(edge: Edge[ES, EP]) = (edge.storage._1, edge.storage._2)

  // TODO findVertex needs an index
  def findVertex(f: Vertex[VP] => Boolean) = vertexSeq.find(f(_))

  def filterEdges(f: ((Vertex[VP], Vertex[VP], EP)) => Boolean) =
    JungUndirectedGraph(vps, ((es: Seq[(Vertex[VP], Vertex[VP], EP)]) => es.filter(f(_))).compose(ef))

//  def unlink(e: Edge[ES, EP]): JungUndirectedGraph[VP, EP] =
//    filterEdges(t => {
//      val v1 = e.storage._1
//      val v2 = e.storage._2
//      !((v1, v2, e.payload) === t || (v2, v1, e.payload) === t)
//    })
//
//  // JungUndirectedGraph[VP, EP]
//  def unlink(v1: Vertex[VP], v2: Vertex[VP]) =
//    filterEdges(e => (e._1 == v1 && e._2 == v2) || (e._2 == v1 && e._1 == v2))

  def areNeighbors(v1: Vertex[VP], v2: Vertex[VP]): Boolean = {
    edgesTouching(v1).exists(edge => connects(edge, v1, v2))
  }

  def forceClique(among: Set[Vertex[VP]], payload: (Vertex[VP], Vertex[VP]) => EP) = {

    val cliqued = (newVs: Seq[Vertex[VP]]) => {

      val old2new: Map[Vertex[VP], Vertex[VP]] = ??? // TODO _vertices.zip(newVs).toMap

      val newEdges = among.toVector.permutations(2)
        .map({ a => (a(0), a(1)) })
        .filter({ case (vi, vj) => !areNeighbors(vi, vj) })
        .map({
          case (vi: Vertex[VP], vj: Vertex[VP]) => {
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
    ???
  }

  def map[NVP: Manifest: Eq, NEP: Eq](vpf: VP => NVP, epf: EP => NEP) =
    JungUndirectedGraph(vps.map(vpf(_)),
      (newVs: Seq[Vertex[NVP]]) =>
        ef(vertexSeq).map({
          case (vi, vj, ep) => (Vertex(vpf(vi.payload)), Vertex(vpf(vj.payload)), epf(ep))
        }))

}
