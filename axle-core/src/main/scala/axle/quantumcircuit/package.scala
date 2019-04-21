package axle

import cats.syntax.all._
import spire.algebra._

package object quantumcircuit {

  implicit class EnrichedVector[T](vector: Vector[T]) {

    def ⊗(other: Vector[T])(implicit multT: MultiplicativeSemigroup[T]): Vector[T] = 
      for {
        x <- vector
        y <- other
      } yield multT.times(x, y)

  }

  // Four operations on 1 bit
  def identity(cbit: CBit): CBit = cbit
  def negate(cbit: CBit): CBit = CBit(cbit.a.negate, cbit.b.negate)
  def constant0(cbit: CBit): CBit = CBit0
  def constant1(cbit: CBit): CBit = CBit1

  // CNOT
  def cnot(control: CBit, target: CBit): (CBit, CBit) =
    if(control === CBit1) {
      (control, target.negate)
    } else {
      (control, target)
    }

}