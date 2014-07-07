package axle.stats

import spire.algebra.Field
import spire.algebra.Order
import spire.random.Dist

case class UnknownDistribution0[A, N: Field: Order: Dist](_values: IndexedSeq[A], _name: String)
extends Distribution0[A, N] {

  def name: String = _name
  
  def values: IndexedSeq[A] = _values
  
  def probabilityOf(a: A): N = ???

  def map[B](f: A => B): Distribution0[B, N] = ???

  def flatMap[B](f: A => Distribution0[B, N]): Distribution0[B, N] = ???

  def is(v: A): CaseIs[A, N] = CaseIs(this, v)

  def isnt(v: A): CaseIsnt[A, N] = CaseIsnt(this, v)
  
  def observe(): A = ???

  def show(implicit order: Order[A]): String = "unknown"
  
}
