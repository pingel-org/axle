package axle.stats

import axle._
import spire.math._
import spire.implicits._

trait Distribution[A] {

  def probabilityOf(a: A): Real

  val square: (Real => Real) = (x: Real) => x ** 2

  /**
   * http://en.wikipedia.org/wiki/Standard_deviation
   */
  
  def σ(): Real = {
    val p: (Real => Real) = (x: Real) => Real(0) // TODO
    val Xs: List[Real] = Nil // TODO
    val μ = Xs.map(xi => p(xi) * xi).Σ(identity)
    (Xs.map(xi => p(xi) * square(xi - μ)).Σ(identity)).sqrt
  }
  
}

trait Distribution0[A] extends Distribution[A] {
  def observe(): A
}

trait Distribution1[A, G1] {
  def observe(gv: G1): A
  def probabilityOf(a: A, given: Case[G1]): Real
}

trait Distribution2[A, G1, G2] {
  def observe(gv1: G1, gv2: G2): A
  def probabilityOf(a: A, given1: Case[G1], given2: Case[G2]): Real
}
