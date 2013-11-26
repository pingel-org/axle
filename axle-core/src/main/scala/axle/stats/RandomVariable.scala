package axle.stats

import axle._
import spire.math._
import spire.implicits._
import spire.algebra._

trait RandomVariable[A] {
  def name: String
  def values: Option[IndexedSeq[A]]
  def is(v: A): CaseIs[A]
  def isnt(v: A): CaseIsnt[A]
  def probability(a: A): Real
  def observe(): Option[A]
  lazy val charWidth: Int = (name.length :: values.map(vs => vs.map(_.toString.length).toList).getOrElse(Nil)).reduce(math.max)
}

case class RandomVariable0[A](_name: String, _values: Option[IndexedSeq[A]] = None, distribution: Option[Distribution0[A]] = None)
  extends RandomVariable[A] {

  def name: String = _name
  def values: Option[IndexedSeq[A]] = _values
  def is(v: A): CaseIs[A] = CaseIs(this, v)
  def isnt(v: A): CaseIsnt[A] = CaseIsnt(this, v)
  def probability(a: A): Real = distribution.map(_.probabilityOf(a)).getOrElse(Real(0))
  def observe(): Option[A] = distribution.map(_.observe)

}

case class RandomVariable1[A, G1](_name: String, _values: Option[IndexedSeq[A]] = None,
  grv: RandomVariable[G1], distribution: Option[Distribution1[A, G1]] = None)
  extends RandomVariable[A] {

  def name: String = _name
  def values: Option[IndexedSeq[A]] = _values
  def is(v: A): CaseIs[A] = CaseIs(this, v)
  def isnt(v: A): CaseIsnt[A] = CaseIsnt(this, v)
  def probability(a: A): Real = ???
  def probability(a: A, given: Case[G1]): Real = distribution.map(_.probabilityOf(a, given)).getOrElse(Real(0))
  def observe(): Option[A] = grv.observe().flatMap(observe(_))
  def observe(gv: G1): Option[A] = distribution.map(_.observe(gv))

}

case class RandomVariable2[A, G1, G2](_name: String, _values: Option[IndexedSeq[A]] = None,
  grv1: RandomVariable[G1], grv2: RandomVariable[G2], distribution: Option[Distribution2[A, G1, G2]] = None)
  extends RandomVariable[A] {

  def name: String = _name
  def values: Option[IndexedSeq[A]] = _values
  def is(v: A): CaseIs[A] = CaseIs(this, v)
  def isnt(v: A): CaseIsnt[A] = CaseIsnt(this, v)
  def probability(a: A): Real = ???
  def probability(a: A, given1: Case[G1], given2: Case[G2]): Real = distribution.map(_.probabilityOf(a, given1, given2)).getOrElse(Real(0))

  def observe(): Option[A] = for {
    g1 <- grv1.observe
    g2 <- grv2.observe
    r <- observe(g1, g2)
  } yield r

  def observe(gv1: G1, gv2: G2): Option[A] = distribution.map(_.observe(gv1, gv2))

}
