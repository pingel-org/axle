package axle

import scala.collection.GenTraversable
import scala.collection.Seq
import scala.math.exp
import scala.math.log
import spire.math.Number
import spire.algebra._

case class EnrichedGenTraversable[+T: Manifest](gt: GenTraversable[T]) {

  def Σ[N: AdditiveMonoid](f: T => N): N = {
    val monoid = implicitly[AdditiveMonoid[N]]
    gt.aggregate(monoid.zero)({ case (x, y) => monoid.plus(x, f(y)) }, monoid.plus(_, _))
  }

  def Sigma[N: AdditiveMonoid](f: T => N) = Σ(f)

  def Π[N: MultiplicativeMonoid](f: T => N): N = {
    val monoid = implicitly[MultiplicativeMonoid[N]]
    gt.aggregate(monoid.one)({ case (x, y) => monoid.times(x, f(y)) }, monoid.times(_, _))
  }

  def Pi[N: MultiplicativeMonoid](f: T => N) = Π(f)

  def ∀(p: T => Boolean) = gt.forall(p)

  def ∃(p: T => Boolean) = gt.exists(p)

  def doubles(): Seq[(T, T)] = gt.toIndexedSeq.permutations(2).map(d => (d(0), d(1))).toSeq

  def triples(): Seq[(T, T, T)] = gt.toIndexedSeq.permutations(3).map(t => (t(0), t(1), t(2))).toSeq

  def ⨯[S](right: GenTraversable[S]) = for (x <- gt; y <- right) yield (x, y)

}
