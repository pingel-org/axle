package axle.graph

import collection.JavaConverters._
import collection._
import axle._
import edu.uci.ics.jung.graph.UndirectedSparseGraph

case class JungUndirectedGraph[VP, EP](
  vps: Seq[VP],
  ef: Seq[_ <: UndirectedGraphVertex[VP]] => Seq[(UndirectedGraphVertex[VP], UndirectedGraphVertex[VP], EP)])
  extends UndirectedGraph[VP, EP] {

  type G[VP, EP] = JungUndirectedGraph[VP, EP]
  type V[VP] = JungUndirectedGraphVertex[VP]
  type E[VP, EP] = JungUndirectedGraphEdge[VP, EP]

  lazy val jungGraph = new UndirectedSparseGraph[JungUndirectedGraphVertex[VP], JungUndirectedGraphEdge[VP, EP]]()

  lazy val vertexSeq = vps.map(vp => new JungUndirectedGraphVertex(vp))

  vertexSeq.map(v => jungGraph.addVertex(v)) // TODO check return value

  lazy val vertexSet = vertexSeq.toSet

  ef(vertexSeq).map({
    case (vi, vj, ep) => {
      jungGraph.addEdge(new JungUndirectedGraphEdge(ep), vi, vj) // TODO check return value
    }
  })

  def storage(): UndirectedSparseGraph[JungUndirectedGraphVertex[VP], JungUndirectedGraphEdge[VP, EP]] = jungGraph

  // Set[JungUndirectedGraphVertex[VP]]
  def vertices() = vertexSet

  // Set[JungUndirectedGraphEdge[VP, EP]]
  def allEdges() = jungGraph.getEdges().asScala.toSet

  def size(): Int = jungGraph.getVertexCount()

  // TODO findVertex needs an index
  // Option[JungUndirectedGraphVertex[VP]]
  def findVertex(f: JungUndirectedGraphVertex[VP] => Boolean) = vertexSeq.find(f(_))

  // JungUndirectedGraph[VP, EP]
  def filterEdges(f: ((UndirectedGraphVertex[VP], UndirectedGraphVertex[VP], EP)) => Boolean) = {
    val filter = (es: Seq[(UndirectedGraphVertex[VP], UndirectedGraphVertex[VP], EP)]) => es.filter(f(_))
    JungUndirectedGraph(vps, filter.compose(ef))
  }

  def unlink(e: JungUndirectedGraphEdge[VP, EP]): JungUndirectedGraph[VP, EP] = filterEdges(_ != e)

  // JungUndirectedGraph[VP, EP]
  def unlink(v1: JungUndirectedGraphVertex[VP], v2: JungUndirectedGraphVertex[VP]) =
    filterEdges(e => (e._1 == v1 && e._2 == v2) || (e._2 == v1 && e._1 == v2))

  def areNeighbors(v1: UndirectedGraphVertex[VP], v2: UndirectedGraphVertex[VP]): Boolean = {
    val jv1 = v1.asInstanceOf[JungUndirectedGraphVertex[VP]] // TODO cast
    val jv2 = v2.asInstanceOf[JungUndirectedGraphVertex[VP]] // TODO cast
    edgesTouching(v1).exists(_.connects(jv1, jv2))
  }

  // JungUndirectedGraph[VP, EP]
  def forceClique(among: Set[UndirectedGraphVertex[VP]], payload: (UndirectedGraphVertex[VP], UndirectedGraphVertex[VP]) => EP) = {

    val cliqued = (newVs: Seq[UndirectedGraphVertex[VP]]) => {

      val old2new: Map[UndirectedGraphVertex[VP], UndirectedGraphVertex[VP]] = null // TODO _vertices.zip(newVs).toMap

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

  def degree(v: UndirectedGraphVertex[VP]): Int = {
    val jv = v.asInstanceOf[JungUndirectedGraphVertex[VP]]
    edgesTouching(jv).size
  }

  // Set[JungUndirectedGraphEdge[VP, EP]] 
  def edgesTouching(v: UndirectedGraphVertex[VP]) = {
    val jv = v.asInstanceOf[JungUndirectedGraphVertex[VP]] // // TODO cast
    jungGraph.getIncidentEdges(jv).asScala.toSet
  }

  // Set[JungUndirectedGraphVertex[VP]]
  def neighbors(v: UndirectedGraphVertex[VP]) = {
    val jv = v.asInstanceOf[JungUndirectedGraphVertex[VP]]
    jungGraph.getNeighbors(jv).asScala.toSet
  }

  // JungUndirectedGraph[VP, EP]
  def delete(v: JungUndirectedGraphVertex[VP]) = JungUndirectedGraph(vertices().toSeq.filter(_ != v).map(_.payload), ef)

  // a "leaf" is vertex with only one neighbor
  // Option[JungUndirectedGraphVertex[VP]]
  def firstLeafOtherThan(r: JungUndirectedGraphVertex[VP]) = vertices().find(v => neighbors(v).size == 1 && !v.equals(r))

  /**
   * "decompositions" page 3 (Definition 3, Section 9.3)
   * turn the neighbors of v into a clique
   */

  def eliminate(v: JungUndirectedGraphVertex[VP], payload: (JungUndirectedGraphVertex[VP], JungUndirectedGraphVertex[VP]) => EP): JungUndirectedGraph[VP, EP] = {

    // TODO
    //    val vs = neighbors(v)
    //    makeFunctional.jungGraph.removeVertex(v)
    //    forceClique(vs, payload)
    null
  }

  // JungUndirectedGraph[NVP, NEP]
  def map[NVP, NEP](vpf: VP => NVP, epf: EP => NEP) = {

    val newVps = vps.map(vpf(_))

    val oldVs = null // TODO

    val newEf = (newVs: Seq[UndirectedGraphVertex[NVP]]) =>
      ef(oldVs).map({
        case (vi, vj, ep) => (new JungUndirectedGraphVertex(vpf(vi.payload)), new JungUndirectedGraphVertex(vpf(vj.payload)), epf(ep))
      })

    JungUndirectedGraph(newVps, newEf)
  }

}

class JungUndirectedGraphEdge[VP, EP](ep: EP) extends UndirectedGraphEdge[VP, EP] {

  type V[VP] = JungUndirectedGraphVertex[VP]

  def vertices(): (JungUndirectedGraphVertex[VP], JungUndirectedGraphVertex[VP]) = null // TODO (v1, v2)
  def payload(): EP = ep
}

class JungUndirectedGraphVertex[VP](vp: VP) extends UndirectedGraphVertex[VP] {
  def payload(): VP = vp
}

trait JungUndirectedGraphFactory { // extends UndirectedGraphFactory {

  type G[VP, EP] = UndirectedGraph[VP, EP]
  type V[VP] = UndirectedGraphVertex[VP]
  // type S = UndirectedSparseGraph[V, EP]

  override def apply[VP, EP](vps: Seq[VP], ef: Seq[V[VP]] => Seq[(V[VP], V[VP], EP)]): G[VP, EP] = new JungUndirectedGraph(vps, ef)

}

object JungUndirectedGraph extends JungUndirectedGraphFactory
