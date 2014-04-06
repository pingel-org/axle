package axle.stats

import axle._
import spire.math._
import spire.algebra._
import spire.implicits._

trait Distribution[A, N] {
  
  def probabilityOf(a: A): N

  val square: (Real => Real) = (x: Real) => x ** 2

  /**
   * http://en.wikipedia.org/wiki/Standard_deviation
   */

  def σ: Real = {
    val p: (Real => Real) = (x: Real) => Real(0) // TODO
    val Xs: List[Real] = Nil // TODO
    val μ = Σ(Xs.map(xi => p(xi) * xi))(identity)
    (Σ(Xs.map(xi => p(xi) * square(xi - μ)))(identity)).sqrt
  }

}

trait Distribution0[A, N] extends Distribution[A, N] {
  def observe(): A
}

trait Distribution1[A, G1, N] {
  def observe(gv: G1): A
  def probabilityOf(a: A, given: Case[G1, N]): N
}

trait Distribution2[A, G1, G2, N] {
  def observe(gv1: G1, gv2: G2): A
  def probabilityOf(a: A, given1: Case[G1, N], given2: Case[G2, N]): N
}
