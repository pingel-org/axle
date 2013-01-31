package axle.stats.docalculus

import axle._
import axle.stats._
import axle.graph._

case class CausalModelNode(rv: RandomVariable[_], observable: Boolean = true)

case class PFunction(rv: RandomVariable[_], inputs: Seq[RandomVariable[_]])

class CausalModel(_name: String, graph: DirectedGraph[CausalModelNode, String])
  extends Model[CausalModelNode](graph) {

  import graph._

  override def name() = _name
  
  def duplicate(): CausalModel = ???

  // TODO: this should probably be Option[Boolean] ?
  def observes(rv: RandomVariable[_]): Boolean = findVertex((n: Vertex[CausalModelNode]) => n.payload.rv == rv).map(_.payload.observable).getOrElse(false)

  def nodesFor(rvs: Set[RandomVariable[_]]) = rvs.flatMap(rv => findVertex((n: Vertex[CausalModelNode]) => n.payload.rv == rv))

  def nodeFor(rv: RandomVariable[_]) = findVertex((n: Vertex[CausalModelNode]) => n.payload.rv == rv)

  override def vertexPayloadToRandomVariable(cmn: CausalModelNode): RandomVariable[_] = cmn.rv

  def addFunctions(pf: Seq[PFunction]): CausalModel = ???

  def getVariable(name: String): Int = 1 // TODO

}

object CausalModel {

  def apply(name: String, vps: Seq[CausalModelNode]): CausalModel = new CausalModel(name, JungDirectedGraph(vps, vs => Nil))

}
