package axle.pgm.docalculus

import axle._
import axle.pgm._
import axle.stats._
import axle.graph._
import axle.graph.JungDirectedGraph
import spire.algebra._
import spire.implicits._

case class CausalModelNode[T: Eq, N: Field](rv: RandomVariable[T, N], observable: Boolean = true)

object CausalModelNode {
  implicit def cmnEq[T: Eq, N: Field]: Eq[CausalModelNode[T, N]] = new Eq[CausalModelNode[T, N]] {
    def eqv(x: CausalModelNode[T, N], y: CausalModelNode[T, N]): Boolean =
      (x.rv === y.rv) && (x.observable === y.observable)
  }
}

case class PFunction[T: Eq, N: Field](rv: RandomVariable[T, N], inputs: Seq[RandomVariable[T, N]])

class CausalModel[T: Eq, N: Field](_name: String, graph: DirectedGraph[CausalModelNode[T, N], String])
{
  import graph._

  def name: String = _name

  def duplicate: CausalModel[T, N] = ???

  def randomVariables: Vector[RandomVariable[T, N]] =
    graph.vertices.map(_.payload.rv).toVector

  // TODO: this should probably be Option[Boolean] ?
  def observes(rv: RandomVariable[T, N]): Boolean = findVertex((n: Vertex[CausalModelNode[T, N]]) => n.payload.rv === rv).map(_.payload.observable).getOrElse(false)

  def nodesFor(rvs: Set[RandomVariable[T, N]]): Set[Vertex[CausalModelNode[T, N]]] =
    rvs.flatMap(rv => findVertex((n: Vertex[CausalModelNode[T, N]]) => n.payload.rv === rv))

  def nodeFor(rv: RandomVariable[T, N]): Option[Vertex[CausalModelNode[T, N]]] = findVertex((n: Vertex[CausalModelNode[T, N]]) => n.payload.rv === rv)

  // def vertexPayloadToRandomVariable(cmn: CausalModelNode[T]): RandomVariable[T] = cmn.rv

  def addFunctions(pf: Seq[PFunction[T, N]]): CausalModel[T, N] = ???

  def getVariable(name: String): Int = ??? // TODO

}

object CausalModel {

  def apply[T: Eq, N: Field](name: String, vps: Seq[CausalModelNode[T, N]]): CausalModel[T, N] =
    new CausalModel(name, JungDirectedGraph[CausalModelNode[T, N], String](vps, vs => Nil))

}
