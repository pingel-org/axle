package axle.quantumcircuit

import spire.algebra._
import spire.math._

case class QBit2[T: Field](a: Complex[T], b: Complex[T], c: Complex[T], d: Complex[T]) {

  require((a * a) + (b * b) + (c * c) + (d * d) === Complex(Field[T].one, Field[T].zero))

  def unindex: Vector[Complex[T]] = Vector(a, b, c, d)

  import cats.syntax.all._

  /**
   * factor is currently a tiny, but brute-force search through
   * common, contrived QBit values
   */

  def factor(implicit nr: NRoot[T], msct: MultiplicativeSemigroup[Complex[T]]): Option[(QBit[T], QBit[T])] =
    (for {
      x <- QBit.commonQBits[T]
      y <- QBit.commonQBits[T]
      if (QBit2(x.unindex ⊗ y.unindex) === this)
    } yield {
      (x, y)
    }).headOption

}
  
object QBit2 {

  implicit def eqQBit2[T]: Eq[QBit2[T]] =
    (x: QBit2[T], y: QBit2[T]) => (x.a === y.a && x.b === y.b && x.c === y.c && x.d === y.d)

  def apply[T: Field](cs: Vector[Complex[T]]): QBit2[T] =
    QBit2[T](cs(0), cs(1), cs(2), cs(3))

  /**
   * CNOT
   * 
   */
    
  def cnot[T](qbits: QBit2[T])(implicit fieldT: Field[T]): QBit2[T] = {
    import qbits._
    QBit2(a, b, d, c)
  }

}