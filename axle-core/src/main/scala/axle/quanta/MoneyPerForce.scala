package axle.quanta

import axle.algebra.Vertex
import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field
import spire.math.Rational
import spire.math.Real
import spire.implicits._

//class MoneyPerForce[DG[_, _]: DirectedGraph] extends Quantum {
//  
//  def wikipediaUrl = "TODO"
//
//  type Q = this.type
//
//  def units[N: Field: Eq] = List[UnitOfMeasurement[Q, N]](
//    unit("$/lb", "$/lb") // derive
//    )
//
//  def links[N: Field: Eq] = {
//    implicit val baseCG = cgnDisconnected[N, DG]
//    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])]()
//  }
//
//  def USDperPound[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "$/lb")
//
//}
