package axle

import axle._
import collection._
import axle.quanta.Information
import math.log

package object stats {

  implicit def rv2it[K](rv: RandomVariable[K]) = rv.getValues.get

  implicit def enrichCaseGenTraversable[A](cgt: GenTraversable[Case[A]]) = EnrichedCaseGenTraversable(cgt)

  def coin(pHead: Double = 0.5) = RandomVariable0("coin",
    values = Some(List('HEAD, 'TAIL).toIndexedSeq),
    distribution = Some(new ConditionalProbabilityTable0(immutable.Map('HEAD -> pHead, 'TAIL -> (1.0 - pHead)))))

  def log2(x: Double) = log(x) / log(2)

  import Information._

  // TODO: filter out P(X eq x) == 0
  def entropy[A](X: RandomVariable[A]) = rv2it(X).Σ(x => -P(X eq x)() * log2(P(X eq x)())) *: bit

  // def H_:(): Information#UOM = entropy()

  def huffmanCode[A, S](alphabet: Set[S]): Map[A, Seq[S]] = {
    //   // TODO
    //   // http://en.wikipedia.org/wiki/Huffman_coding
    Map()
  }

  def die() = RandomVariable0("die",
    values = Some(List('⚀, '⚁, '⚂, '⚃, '⚄, '⚅).toIndexedSeq),
    distribution = Some(new ConditionalProbabilityTable0(immutable.Map(
      '⚀ -> 1d / 6,
      '⚁ -> 1d / 6,
      '⚂ -> 1d / 6,
      '⚃ -> 1d / 6,
      '⚄ -> 1d / 6,
      '⚅ -> 1d / 6))))

}