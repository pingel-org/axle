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

abstract class MoneyFlow extends Quantum {
  def wikipediaUrl = "TODO"
}

object MoneyFlow extends MoneyFlow {

  type Q = MoneyFlow

  def units[N: Field: Eq] = List[UnitOfMeasurement[Q, N]](
    unit("$/hr", "$/hr") // derive
    )

  def links[N: Field: Eq] = {
    implicit val baseCG = cgnDisconnected[N]
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], N => N, N => N)]()
  }

  def USDperHour[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "$/hr")

}
