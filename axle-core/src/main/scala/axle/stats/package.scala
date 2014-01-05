package axle

import collection.GenTraversable
import axle._
import axle.quanta.Information
import spire.algebra._
import spire.implicits._
import spire.math._

package object stats {

  implicit def probability2real: Probability => Real = (p: Probability) => p()

  implicit def rv2it[K](rv: RandomVariable[K]): IndexedSeq[K] = rv.values.getOrElse(Vector())

  implicit def enrichCaseGenTraversable[A: Manifest](cgt: GenTraversable[Case[A]]): EnrichedCaseGenTraversable[A] = EnrichedCaseGenTraversable(cgt)

  def coin(pHead: Real = Real(Rational(1, 2))) = RandomVariable0("coin",
    Some(List('HEAD, 'TAIL).toIndexedSeq),
    distribution = Some(new ConditionalProbabilityTable0(Map('HEAD -> pHead, 'TAIL -> (1d - pHead)))))

  def log2(x: Real): Real = Real(math.log(x.toDouble) / math.log(2))

  def mean[N: Field: Manifest](xs: GenTraversable[N]): N = EnrichedGenTraversable(xs).Σ(identity) / xs.size

  def square[N: Ring](x: N): N = x ** 2

  // http://en.wikipedia.org/wiki/Standard_deviation

  def stddev[N: NRoot: Field: Manifest](xs: Vector[N]): N = {
    val μ = mean(xs)
    (xs.map(x => square(x - μ)).Σ(identity) / xs.size).sqrt
  }

  import Information._
  import axle.quanta._

  def entropy[A: Manifest](X: RandomVariable[A]): Information.Q = {
    val H = X.values.map(_.Σ(x => {
      val px = P(X is x)()
      if (px > 0) (-px * log2(px)) else Real(0)
    })).getOrElse(Real(0))
    Number(H.toDouble) *: bit // TODO Number(_.toDouble) should not be necessary
  }

  def H[A: Manifest](X: RandomVariable[A]): Information.Q = entropy(X)

  def huffmanCode[A, S](alphabet: Set[S]): Map[A, Seq[S]] = {
    // TODO
    // http://en.wikipedia.org/wiki/Huffman_coding
    Map()
  }

}
