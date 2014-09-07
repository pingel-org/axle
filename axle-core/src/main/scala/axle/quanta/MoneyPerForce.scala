package axle.quanta

import axle.graph.DirectedGraph
import axle.algebra.Bijection
import spire.algebra._
import spire.math.Rational
import spire.implicits.eqOps
import spire.implicits.moduleOps
import spire.implicits.groupOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.additiveGroupOps
import spire.implicits.additiveSemigroupOps

abstract class MoneyPerForce extends Quantum {
  def wikipediaUrl = "TODO"
}

object MoneyPerForce extends MoneyPerForce {

  type Q = MoneyPerForce

  def units[N: Field: Eq] = List[UnitOfMeasurement[Q, N]](
    unit("$/lb", "$/lb") // derive
    )

  def links[N: Field: Eq] = {
    implicit val baseCG = cgnDisconnected[N]
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])]()
  }

  def USDperPound[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "$/lb")

}
