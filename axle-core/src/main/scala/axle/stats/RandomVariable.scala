package axle.stats

import spire.algebra.Eq
import spire.algebra.Field

trait RandomVariable[A, N] {
  
  def name: String
  
  def values: IndexedSeq[A]
  
  def is(v: A): CaseIs[A, N]
  
  def isnt(v: A): CaseIsnt[A, N]
  
  def probability(a: A): N
  
  def observe(): A
  
  lazy val charWidth: Int = (name.length :: values.map(_.toString.length).toList).reduce(math.max)
}

object RandomVariable {
  implicit def rvEq[A: Eq, N]: Eq[RandomVariable[A, N]] = new Eq[RandomVariable[A, N]] {
    def eqv(x: RandomVariable[A, N], y: RandomVariable[A, N]): Boolean = x equals y // TODO
  }
}

case class RandomVariable0[A, N: Field](
  _name: String,
  distribution: Distribution0[A, N])
  extends RandomVariable[A, N] {

  val field = implicitly[Field[N]]
  
  def name: String = _name
  
  def values: IndexedSeq[A] = distribution.values
  
  def is(v: A): CaseIs[A, N] = CaseIs(this, v)
  
  def isnt(v: A): CaseIsnt[A, N] = CaseIsnt(this, v)
  
  def probability(a: A): N = distribution.probabilityOf(a)
  
  def observe(): A = distribution.observe
}

case class RandomVariable1[A, G1, N: Field](
  _name: String,
  grv: RandomVariable[G1, N],
  distribution: Distribution1[A, G1, N])
  extends RandomVariable[A, N] {

  val field = implicitly[Field[N]]
  
  def name: String = _name
  
  def values: IndexedSeq[A] = distribution.values
  
  def is(v: A): CaseIs[A, N] = CaseIs(this, v)
  
  def isnt(v: A): CaseIsnt[A, N] = CaseIsnt(this, v)
  
  def probability(a: A): N = ???
  
  def probability(a: A, given: Case[G1, N]): N = distribution.probabilityOf(a, given)
  
  def observe(): A = observe(grv.observe())
  
  def observe(gv: G1): A = distribution.observe(gv)
}

case class RandomVariable2[A, G1, G2, N: Field](
  _name: String,
  grv1: RandomVariable[G1, N],
  grv2: RandomVariable[G2, N],
  distribution: Distribution2[A, G1, G2, N])
  extends RandomVariable[A, N] {

  val field = implicitly[Field[N]]
  
  def name: String = _name
  
  def values: IndexedSeq[A] = distribution.values
  
  def is(v: A): CaseIs[A, N] = CaseIs(this, v)
  
  def isnt(v: A): CaseIsnt[A, N] = CaseIsnt(this, v)
  
  def probability(a: A): N = ???
  
  def probability(a: A, given1: Case[G1, N], given2: Case[G2, N]): N = distribution.probabilityOf(a, given1, given2)

  def observe(): A = observe(grv1.observe, grv2.observe)

  def observe(gv1: G1, gv2: G2): A = distribution.observe(gv1, gv2)

}
