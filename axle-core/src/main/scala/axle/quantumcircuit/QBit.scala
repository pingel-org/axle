package axle.quantumcircuit

import cats.kernel.Eq
import cats.syntax.all._

import spire.math.Complex
import spire.algebra.Field

import axle.algebra.Binary
import axle.algebra.{B0, B1}
import axle.stats.Variable
import axle.stats.ConditionalProbabilityTable

case class QBit[T: Field](a: Complex[T], b: Complex[T]) {

  require((a * a) + (b * b) === Complex(Field[T].one, Field[T].zero))

  def unindex: Vector[Complex[T]] = Vector(a, b)

  // A QBit (a b) collapses to an actual value of 0 or 1
  //  0 with probability a^2
  //  1 with probability b^2

  def probabilityModel: ConditionalProbabilityTable[Binary, T] = {
    val m = Map[Binary, T](
      B0 -> (a * a).real,
      B1 -> (b * b).real
    )
    ConditionalProbabilityTable.apply(m, Variable("Q"))
  }
    
}

object QBit {

  implicit def eqQBit[T]: Eq[QBit[T]] =
    (x: QBit[T], y: QBit[T]) => (x.a === y.a && x.b === y.b)

  // Two operations on no bits

  def constant0[T](implicit fieldT: Field[T]): QBit[T] =
    QBit[T](Complex(fieldT.one), Complex(fieldT.zero))

  def constant1[T](implicit fieldT: Field[T]): QBit[T] =
    QBit[T](Complex(fieldT.zero), Complex(fieldT.one))

  // Four operations on 1 bit
  def identity[T](qbit: QBit[T]): QBit[T] = qbit

  def negate[T](qbit: QBit[T])(implicit fieldT: Field[T]): QBit[T] =
    QBit[T](- qbit.a, - qbit.b)

  def constant0[T](qbit: QBit[T])(implicit fieldT: Field[T]): QBit[T] =
    QBit[T](Complex(fieldT.one), Complex(fieldT.zero))

  def constant1[T](qbit: QBit[T])(implicit fieldT: Field[T]): QBit[T] =
    QBit[T](Complex(fieldT.zero), Complex(fieldT.one))

  // CNOT
  def cnot[T](control: QBit[T], target: QBit[T])(implicit fieldT: Field[T]): (QBit[T], QBit[T]) =
    if(control === constant1[T]) {
      (control, negate(target))
    } else {
      (control, target)
    }

}