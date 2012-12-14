package axle.stats.docalculus

import axle.stats._
import axle.graph._

case class CausalModelNode(rv: RandomVariable[_], observable: Boolean = true)

case class PFunction(rv: RandomVariable[_], inputs: Seq[RandomVariable[_]])

case class CausalModel(name: String, graph: DirectedGraph[CausalModelNode, String])
  extends Model[CausalModelNode](graph) {

  import graph._

  def duplicate(): CausalModel = null // TODO

  // TODO: this should probably be Option[Boolean] ?
  def observes(rv: RandomVariable[_]): Boolean = findVertex((n: DirectedGraphVertex[CausalModelNode]) => n.payload.rv == rv).map(_.payload.observable).getOrElse(false)

  def nodesFor(rvs: Set[RandomVariable[_]]) = rvs.flatMap(rv => findVertex((n: DirectedGraphVertex[CausalModelNode]) => n.payload.rv == rv))

  def nodeFor(rv: RandomVariable[_]) = findVertex((n: DirectedGraphVertex[CausalModelNode]) => n.payload.rv == rv)

  override def vertexPayloadToRandomVariable(cmn: CausalModelNode): RandomVariable[_] = cmn.rv

  def addFunctions(pf: Seq[PFunction]): CausalModel = null // TODO

  def getVariable(name: String): Int = 1 // TODO

}

object CausalModel {

  def apply(name: String, vps: Seq[CausalModelNode]): CausalModel = new CausalModel(name, JungDirectedGraph(vps, vs => Nil))

}
