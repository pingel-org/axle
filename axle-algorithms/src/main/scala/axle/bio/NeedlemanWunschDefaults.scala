package axle.bio

import spire.algebra.AdditiveMonoid
import spire.algebra.Eq
import spire.algebra.Module
import spire.algebra.Order
import spire.algebra.Ring

object NeedlemanWunschDefaults {

  // Default evidence for optimalAlignment[IndexedSeq, Char, DoubleMatrix, Int, Double]

  implicit val charEq: Eq[Char] = spire.implicits.CharAlgebra
  implicit val intRing: Ring[Int] = spire.implicits.IntAlgebra
  implicit val orderRing: Order[Int] = spire.implicits.IntAlgebra
  implicit val dim: Module[Double, Int] = axle.algebra.modules.doubleIntModule
  implicit val amd: AdditiveMonoid[Double] = spire.implicits.DoubleAlgebra
  implicit val od: Order[Double] = spire.implicits.DoubleAlgebra

  /**
   * similarity function for nucleotides
   *
   * S(a, b) === S(b, a)
   *
   */

  def similarity(x: Char, y: Char): Double = {
    val result = (x, y) match {
      case ('A', 'A') => 10
      case ('A', 'G') => -1
      case ('A', 'C') => -3
      case ('A', 'T') => -4
      case ('G', 'A') => -1
      case ('G', 'G') => 7
      case ('G', 'C') => -5
      case ('G', 'T') => -3
      case ('C', 'A') => -3
      case ('C', 'G') => -5
      case ('C', 'C') => 9
      case ('C', 'T') => 0
      case ('T', 'A') => -4
      case ('T', 'G') => -3
      case ('T', 'C') => 0
      case ('T', 'T') => 8
    }
    result
  }

  val gap = '-'

  val gapPenalty = -5d

}