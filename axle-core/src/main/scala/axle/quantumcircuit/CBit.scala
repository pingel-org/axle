package axle.quantumcircuit

import cats.kernel.Eq
import cats.syntax.all._

import axle.algebra.Binary

case class CBit(a: Binary, b: Binary) {

  def negate: CBit = CBit(a.negate, b.negate)

  def reverseIndex: Vector[Binary] = Vector(a, b)

}

object CBit0 extends CBit(1, 0)

object CBit1 extends CBit(0, 1)

object CBit {

  implicit val eqCBit: Eq[CBit] =
    (x: CBit, y: CBit) => (x.a === y.a && x.b === y.b)

  // implicit def toEnrichedBinaryVector(cbit: CBit): EnrichedVector[Binary] =
  //   new EnrichedVector(Vector(cbit.a, cbit.b))

  def fromBinary(b: Binary): CBit =
    if( b === axle.algebra.B0 ) {
      CBit0
    } else {
      CBit1
    }

}
