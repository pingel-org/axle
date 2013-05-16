package axle

import axle._
import collection._
import axle.quanta.Information
import math.{ log, sqrt }

package object stats {

  implicit def probability2double = (p: Probability) => p()

  implicit def rv2it[K](rv: RandomVariable[K]): IndexedSeq[K] = rv.values.getOrElse(Vector())

  implicit def enrichCaseGenTraversable[A: Manifest](cgt: GenTraversable[Case[A]]) = EnrichedCaseGenTraversable(cgt)

  def coin(pHead: Double = 0.5) = RandomVariable0("coin",
    Some(List('HEAD, 'TAIL).toIndexedSeq),
    distribution = Some(new ConditionalProbabilityTable0(immutable.Map('HEAD -> pHead, 'TAIL -> (1.0 - pHead)))))

  def log2(x: Double) = log(x) / log(2)

  def mean(xs: Vector[Double]): Double = xs.sum / xs.size

  def square(x: Double) = x * x

  // http://en.wikipedia.org/wiki/Standard_deviation

  def stddev(xs: Vector[Double]): Double = {
    val μ = mean(xs)
    sqrt(xs.map(x => square(x - μ)).sum / xs.size)
  }

  import Information._
  import axle.quanta._

  def entropy[A: Manifest](X: RandomVariable[A]): Information.Q = X.values.map(_.Σ(x => {
    val px = P(X is x)()
    if (px > 0) (-px * log2(px)) else 0.0
  })).getOrElse(0.0) *: bit

  def H[A: Manifest](X: RandomVariable[A]): Information.Q = entropy(X)

  def huffmanCode[A, S](alphabet: Set[S]): Map[A, Seq[S]] = {
    //   // TODO
    //   // http://en.wikipedia.org/wiki/Huffman_coding
    Map()
  }

}
