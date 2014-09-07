package axle.quanta

import axle.graph.DirectedGraph
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
    // derive(USD.by[Force.type, this.type](pound, this), Some("$/lb"), Some("$/lb")))
  )

  def links[N: Field: Eq] = {
    implicit val baseCG = cgnDisconnected[N]
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], N => N, N => N)]()
  }

  def USDperPound[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "$/lb")

}
