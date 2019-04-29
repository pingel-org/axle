package axle.quantumcircuit

import spire.algebra._
import spire.math._

case class QBit2[T: Field](a: Complex[T], b: Complex[T], c: Complex[T], d: Complex[T]) {

  require((a * a) + (b * b) + (c * c) + (d * d) === Complex(Field[T].one, Field[T].zero))

  def unindex: Vector[Complex[T]] = Vector(a, b, c, d)

  def factor: Option[(QBit[T], QBit[T])] = ???

}
  
object QBit2 {

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