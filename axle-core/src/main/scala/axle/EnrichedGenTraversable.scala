package axle

import scala.collection.GenTraversable
import scala.collection.Seq
import scala.math.exp
import scala.math.log
import spire.math.Number
import spire.algebra._

case class EnrichedGenTraversable[+T: Manifest](gt: GenTraversable[T]) {

  def Σ[N: Ring](f: T => N): N = {
    val ring = implicitly[Ring[N]]
    gt.aggregate(ring.zero)({ case (x, y) => ring.plus(x, f(y)) }, ring.plus(_, _))
  }

  def Sigma[N: Ring](f: T => N) = Σ(f)

  def Π[N: Ring](f: T => N): N = {
    val ring = implicitly[Ring[N]]
    gt.aggregate(ring.one)({ case (a, b) => ring.times(a, f(b)) }, { case (x, y) => ring.times(x, y) })
  }

  def Pi[N: Ring](f: T => N) = Π(f)

  // def Πx(f: T => Double): Double = exp(gt.map(x => log(f(x))).sum) // TODO: use aggregate for sum?
  //  def Π(f: T => (() => Double)): Double = gt.aggregate(1.0)((a, b) => a * f(b)(), (x, y) => x * y)
  //  def Pi(f: T => (() => Double)) = Π(f)

  def ∀(p: T => Boolean) = gt.forall(p)

  def ∃(p: T => Boolean) = gt.exists(p)

  def doubles(): Seq[(T, T)] = gt.toIndexedSeq.permutations(2).map(d => (d(0), d(1))).toSeq

  def triples(): Seq[(T, T, T)] = gt.toIndexedSeq.permutations(3).map(t => (t(0), t(1), t(2))).toSeq

  def ⨯[S](right: GenTraversable[S]) = for (x <- gt; y <- right) yield (x, y)

}
