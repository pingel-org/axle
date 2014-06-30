package axle.stats

import axle.Σ
import spire.math.Real
import spire.algebra.Order
import spire.algebra.Eq

object Distribution {
  implicit def rvEq[A: Eq, N]: Eq[Distribution[A, N]] = new Eq[Distribution[A, N]] {
    def eqv(x: Distribution[A, N], y: Distribution[A, N]): Boolean = x equals y // TODO
  }
}

trait Distribution[A, N] {

  def name: String

  def values: IndexedSeq[A]

  def is(v: A): CaseIs[A, N]

  def isnt(v: A): CaseIsnt[A, N]
  
//  def is(v: A): CaseIs[A, N] = CaseIs(this, v)
//
//  def isnt(v: A): CaseIsnt[A, N] = CaseIsnt(this, v)

  def observe(): A

  def probabilityOf(a: A): N

  lazy val charWidth: Int = (name.length :: values.map(_.toString.length).toList).reduce(math.max)
}

trait Distribution0[A, N] extends Distribution[A, N] {

  def map[B](f: A => B): Distribution0[B, N]

  def flatMap[B](f: A => Distribution0[B, N]): Distribution0[B, N]

  def show(implicit order: Order[A]): String

  //  def σ: Real = {
  //    val μ = Σ(values.map(xi => probabilityOf(xi) * xi))(identity)
  //    (Σ(Xs.map(xi => p(xi) * ((xi - μ) ** 2)))(identity)).sqrt
  //  }
  
}

trait Distribution1[A, G1, N] extends Distribution[A, N] {

  def observe(gv: G1): A

  def probabilityOf(a: A, given: Case[G1, N]): N
}

trait Distribution2[A, G1, G2, N] extends Distribution[A, N] {

  def observe(gv1: G1, gv2: G2): A

  def probabilityOf(a: A, given1: Case[G1, N], given2: Case[G2, N]): N
}
