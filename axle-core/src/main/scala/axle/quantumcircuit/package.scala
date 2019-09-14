package axle

import cats.kernel.Eq
//import cats.syntax.all._

import spire.algebra._
import spire.math._
import spire.random._
import spire.random.Generator.rng

import axle.algebra._
import axle.stats._
import axle.syntax.probabilitymodel._

package object quantumcircuit {

  implicit val eqInt: Eq[Int] = spire.implicits.IntAlgebra

  def unindexToDistribution[T: Ring](xs: Vector[Complex[T]]): ConditionalProbabilityTable[Vector[Binary], T] = {
    import cats.implicits._
    val m: Map[Vector[Binary], T] = xs.zipWithIndex.map({ case (x, i) =>
       (0 until xs.size).map({ j => if(i === j) B1 else B0 }).toVector -> (x*x).real
    }).toMap
    ConditionalProbabilityTable(m, Variable("Q"))
  }

  implicit class EnrichedVector[T](vector: Vector[T]) {

    def ⊗(other: Vector[T])(implicit multT: MultiplicativeSemigroup[T]): Vector[T] = 
      for {
        x <- vector
        y <- other
      } yield multT.times(x, y)

  }

 /**
  * Encodings of 1-Qbit functions for Deutsch Oracle
  */

  def constant0ForDeutsch[T: Field](qbits: QBit2[T]): QBit2[T] = {
    import qbits._
    QBit2(a, b, c, d)
  }

  def constant1ForDeutsch[T: Field](qbits: QBit2[T]): QBit2[T] = {
    import qbits._
    QBit2(a, b, d, c)
  }

  def identityForDeutsch[T: Field](qbits: QBit2[T]): QBit2[T] =
    QBit2.cnot(qbits) // a b d c

  def negateForDeutsch[T: Field](qbits: QBit2[T]): QBit2[T] = {
    import qbits._
    QBit2(b, a, d, c)
  }

  def wrapDeutsched[T: Field: NRoot](f: QBit2[T] => QBit2[T])(implicit ev: MultiplicativeSemigroup[Complex[T]]): QBit[T] => QBit[T] =
    (x: QBit[T]) => f(QBit2(x.unindex ⊗ QBit.constant0[T].unindex)).factor.get._1

  private[this] implicit val cptPM = ProbabilityModel[ConditionalProbabilityTable]

  /**
   * for `f`
   * the `input` qbit is the most significant (left hand side or first) bit
   * the second qbit is the 'spare'
   */

  def isConstantDeutschOracle[T: Field: NRoot: Eq: Dist: Order](
    f: QBit2[T] => QBit2[T]
  )(implicit multSemiCT: MultiplicativeSemigroup[Complex[T]]): Boolean = {


    val hx0 = QBit.H(QBit.X(QBit.constant0[T]))

    val (preH0, preH1) = QBit2(f(QBit2(hx0.unindex ⊗ hx0.unindex)).unindex).factor.get

    // |11> => f is constant
    // |01> => f is variable

    val mostSignificantBit = QBit.H(preH0).probabilityModel.observe(rng)
    val leastSignificantBit = QBit.H(preH1).probabilityModel.observe(rng)

    import cats.implicits._
    assert(leastSignificantBit === CBit1)
    mostSignificantBit === CBit1
  }    
}