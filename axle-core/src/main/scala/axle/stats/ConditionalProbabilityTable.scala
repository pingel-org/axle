package axle.stats

import spire.algebra.Field
import spire.algebra.Order
import spire.implicits.additiveSemigroupOps
import spire.implicits.orderOps
import spire.random.Dist
import spire.random.mutable.Cmwc5
import spire.compat.ordering

class ConditionalProbabilityTable0[A: Order, N: Field: Order: Dist](p: Map[A, N]) extends Distribution0[A, N] {

  val field = implicitly[Field[N]]

  val rng = Cmwc5()

  // def randomStream(): Stream[Double] = Stream.cons(math.random, randomStream())

//  def map[B](f: A => B): Distribution[B, N] = 42
//
//  def flatMap[B](f: A => Distribution[B, N]): Distribution[B, N] = 42
  
  // TODO Is there a version of scanLeft that is more like a reduce?
  // This would allow me to avoid having to construct the initial dummy element
  val bars = p.scanLeft((null.asInstanceOf[A], field.zero))((x, y) => (y._1, x._2 + y._2))

  def observe(): A = {
    val r = rng.next[N]
    bars.find(_._2 > r).getOrElse(throw new Exception("malformed distribution"))._1
  }

  def values: IndexedSeq[A] = p.keys.toIndexedSeq.sorted

  def probabilityOf(a: A): N = p(a)
}

class ConditionalProbabilityTable2[A: Order, G1, G2, N: Field: Order](p: Map[(G1, G2), Map[A, N]])
  extends Distribution2[A, G1, G2, N] {

  lazy val _values = p.values.map(_.keySet).reduce(_ union _).toVector.sorted

  def values: IndexedSeq[A] = _values

  def observe(): A = ???

  def observe(gv1: G1, gv2: G2): A = ???

  def probabilityOf(a: A): N = ???

  def probabilityOf(a: A, given1: Case[G1, N], given2: Case[G2, N]): N = ???

}

