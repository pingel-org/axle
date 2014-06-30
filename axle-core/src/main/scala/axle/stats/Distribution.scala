package axle.stats

import axle.Σ
import spire.math.Real
import spire.algebra.Order

trait Distribution[A, N] {

  //  def σ: Real = {
  //    val μ = Σ(values.map(xi => probabilityOf(xi) * xi))(identity)
  //    (Σ(Xs.map(xi => p(xi) * ((xi - μ) ** 2)))(identity)).sqrt
  //  }

}

trait Distribution0[A, N] {

  def values: IndexedSeq[A]

  def map[B](f: A => B): Distribution0[B, N]

  def flatMap[B](f: A => Distribution0[B, N]): Distribution0[B, N]

  def observe(): A

  def probabilityOf(a: A): N

  def show(implicit order: Order[A]): String
  
}

trait Distribution1[A, G1, N] {

  def values: IndexedSeq[A]

  def observe(gv: G1): A

  def probabilityOf(a: A, given: Case[G1, N]): N
}

trait Distribution2[A, G1, G2, N] {

  def values: IndexedSeq[A]

  def observe(gv1: G1, gv2: G2): A

  def probabilityOf(a: A, given1: Case[G1, N], given2: Case[G2, N]): N
}
