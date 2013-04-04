package axle.stats

import math.sqrt

trait Distribution[A] {

  def probabilityOf(a: A): Double

  val square: (Double => Double) = (x: Double) => x * x

  /**
   * http://en.wikipedia.org/wiki/Standard_deviation
   */
  
  def σ(): Double = {
    val p: (Double => Double) = (x: Double) => 0.0 // TODO
    val Xs: List[Double] = Nil // TODO
    val μ = Xs.map(xi => p(xi) * xi).sum
    sqrt(Xs.map(xi => p(xi) * square(xi - μ)).sum)
  }
  
}

trait Distribution0[A] extends Distribution[A] {
  def observe(): A
}

trait Distribution1[A, G1] {
  def observe(gv: G1): A
  def probabilityOf(a: A, given: Case[G1]): Double
}

trait Distribution2[A, G1, G2] {
  def observe(gv1: G1, gv2: G2): A
  def probabilityOf(a: A, given1: Case[G1], given2: Case[G2]): Double
}
