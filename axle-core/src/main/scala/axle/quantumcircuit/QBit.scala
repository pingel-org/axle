package axle.quantumcircuit

import cats.kernel.Eq
//import cats.syntax.all._

import spire.math._
// import spire.implicits._
import spire.algebra.Field
import spire.algebra.NRoot

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
    QBit[T](qbit.b, qbit.a)

  def constant0[T](qbit: QBit[T])(implicit fieldT: Field[T]): QBit[T] =
    QBit[T](Complex(fieldT.one), Complex(fieldT.zero))

  def constant1[T](qbit: QBit[T])(implicit fieldT: Field[T]): QBit[T] =
    QBit[T](Complex(fieldT.zero), Complex(fieldT.one))

  def X[T](qbit: QBit[T])(implicit fieldT: Field[T]): QBit[T] =
    negate(qbit)

  // CNOT
  def cnot[T](control: QBit[T], target: QBit[T])(implicit fieldT: Field[T]): (QBit[T], QBit[T]) = {
    import cats.syntax.all._
    if(control === constant1[T]) {
      (control, negate(target))
    } else {
      (control, target)
    }
  }

  // Hadamard
  //
  // can be implemented with a 2x2 matrix
  // [1/sqrt(2) 1/sqrt(2); 1/sqrt(2) -1/sqrt(2)]

  def hadamard[T](qbit: QBit[T])(implicit fieldT: Field[T], nrootT: NRoot[T]): QBit[T] = {

    import spire.implicits._

    val two = fieldT.one + fieldT.one
    val sqrtHalf = Complex[T](fieldT.one / sqrt(two), fieldT.zero)

    QBit(
     sqrtHalf * qbit.a + sqrtHalf * qbit.b,
     sqrtHalf * qbit.a - sqrtHalf * qbit.b)
  }

  def H[T](qbit: QBit[T])(implicit fieldT: Field[T], nrootT: NRoot[T]): QBit[T] =
    hadamard(qbit)

 /**
  * Encodings of 1-Qbit functions for Deutsch Oracle
  */

  def identityForDeutsch[T: Field](x: QBit[T], spare: QBit[T]) =
    cnot(x, spare)

  def constant0ForDeutsch[T](x: QBit[T], spare: QBit[T]) =
    (x, spare)

  def negateForDeutsch[T: Field](x: QBit[T], spare: QBit[T]) = {
    val intermediate = cnot(x, spare)
    (intermediate._1, X(intermediate._2))
  }

  def constant1ForDeutsch[T: Field](x: QBit[T], spare: QBit[T]) =
    (x, X(spare))

  /**
   * for `f`
   * the `input` qbit is the most significant (left hand side or first) bit
   * the second qbit is the 'spare'
   */

  def isConstantDeutschOracle[T: Field: NRoot: Eq](
    f: (QBit[T], QBit[T]) => (QBit[T], QBit[T])
  ): Boolean = {

    val unprocessedOutput = f(H(X(constant0[T])), H(X(constant0[T])))

    val output = (H(unprocessedOutput._1), H(unprocessedOutput._2))

    // |11> => f is constant
    // |01> => f is variable

    val constantPattern = |("11").>.unindex.map({ b => b match {
      case B0 => Complex(Field[T].zero)
      case B1 => Complex(Field[T].one)
    }})

    import cats.implicits._
    (output._1.unindex ++ output._2.unindex) === constantPattern
  }

}