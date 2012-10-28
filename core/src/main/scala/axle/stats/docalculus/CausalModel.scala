package axle.stats.docalculus

import axle.stats._

case class CausalModelNode(rv: RandomVariable[_], observable: Boolean=true)

case class PFunction(rv: RandomVariable[_], inputs: Seq[RandomVariable[_]])

object CausalModel {

  def apply(name: String): CausalModel = new CausalModel(name)
}

class CausalModel(_name: String)
  extends Model[CausalModelNode] {
  
  def name(): String = _name

  def duplicate(): CausalModel = null // TODO

  // TODO: this should probably be Option[Boolean] ?
  def observes(rv: RandomVariable[_]): Boolean = findVertex(_.rv == rv).map(_.payload.observable).getOrElse(false)

  def nodesFor(rvs: Set[RandomVariable[_]]): Set[this.V] = rvs.flatMap(rv => findVertex(_.rv == rv))

  def nodeFor(rv: RandomVariable[_]): this.V = findVertex(_.rv == rv).getOrElse(null.asInstanceOf[this.V]) // TODO

  def vertexPayloadToRandomVariable(cmn: CausalModelNode): RandomVariable[_] = cmn.rv

  def addFunctions(pf: Seq[PFunction]): CausalModel = null // TODO

  def getVariable(name: String): Int = 1 // TODO
  
}