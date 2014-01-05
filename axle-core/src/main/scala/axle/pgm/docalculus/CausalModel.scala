package axle.pgm.docalculus

import axle._
import axle.pgm._
import axle.stats._
import axle.graph._
import axle.graph.JungDirectedGraph
import spire.algebra._
import spire.implicits._

case class CausalModelNode[T: Eq](rv: RandomVariable[T], observable: Boolean = true)

object CausalModelNode {
  implicit def cmnEq[T: Eq]: Eq[CausalModelNode[T]] = new Eq[CausalModelNode[T]] {
    def eqv(x: CausalModelNode[T], y: CausalModelNode[T]): Boolean =
      (x.rv equals y.rv) && (x.observable equals y.observable)
  }
}

case class PFunction[T: Eq](rv: RandomVariable[T], inputs: Seq[RandomVariable[T]])

class CausalModel[T: Eq](_name: String, graph: DirectedGraph[CausalModelNode[T], String])
{
  import graph._

  def name: String = _name

  def duplicate: CausalModel[T] = ???

  def randomVariables: Vector[RandomVariable[T]] =
    graph.vertices.map(_.payload.rv).toVector

  // TODO: this should probably be Option[Boolean] ?
  def observes(rv: RandomVariable[T]): Boolean = findVertex((n: Vertex[CausalModelNode[T]]) => n.payload.rv == rv).map(_.payload.observable).getOrElse(false)

  def nodesFor(rvs: Set[RandomVariable[T]]): Set[Vertex[CausalModelNode[T]]] =
    rvs.flatMap(rv => findVertex((n: Vertex[CausalModelNode[T]]) => n.payload.rv == rv))

  def nodeFor(rv: RandomVariable[T]): Option[Vertex[CausalModelNode[T]]] = findVertex((n: Vertex[CausalModelNode[T]]) => n.payload.rv == rv)

  // def vertexPayloadToRandomVariable(cmn: CausalModelNode[T]): RandomVariable[T] = cmn.rv

  def addFunctions(pf: Seq[PFunction[T]]): CausalModel[T] = ???

  def getVariable(name: String): Int = ??? // TODO

}

object CausalModel {

  def apply[T: Eq](name: String, vps: Seq[CausalModelNode[T]]): CausalModel[T] =
    new CausalModel(name, JungDirectedGraph[CausalModelNode[T], String](vps, vs => Nil))

}
