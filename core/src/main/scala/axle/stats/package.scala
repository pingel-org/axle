package axle

import axle._
import collection._
import axle.quanta.Information
import math.log
import scalaz._
import Scalaz._

package object stats {

  implicit def rv2it[K](rv: RandomVariable[K]): IndexedSeq[K] = rv.getValues.getOrElse(Vector())
  
  implicit def enrichCaseGenTraversable[A](cgt: GenTraversable[Case[A]]) = EnrichedCaseGenTraversable(cgt)

  def coin(pHead: Double = 0.5) = RandomVariable0("coin",
    values = Some(List('HEAD, 'TAIL).toIndexedSeq),
    distribution = Some(new ConditionalProbabilityTable0(immutable.Map('HEAD -> pHead, 'TAIL -> (1.0 - pHead)))))

  def d6() = RandomVariable0("d6",
    values = Some(List('⚀, '⚁, '⚂, '⚃, '⚄, '⚅).toIndexedSeq),
    distribution = Some(new ConditionalProbabilityTable0(immutable.Map(
      '⚀ -> 1d / 6,
      '⚁ -> 1d / 6,
      '⚂ -> 1d / 6,
      '⚃ -> 1d / 6,
      '⚄ -> 1d / 6,
      '⚅ -> 1d / 6))))

  def log2(x: Double) = log(x) / log(2)

  import Information._

  def entropy[A](X: RandomVariable[A]): Information.UOM = X.getValues.map(_.Σ(x => {
    val px = P(X eq x)()
    (px > 0) ? (-px * log2(px)) | 0.0
  })).getOrElse(0.0) *: bit

  def H[A](X: RandomVariable[A]): Information.UOM = entropy(X)

  def huffmanCode[A, S](alphabet: Set[S]): Map[A, Seq[S]] = {
    //   // TODO
    //   // http://en.wikipedia.org/wiki/Huffman_coding
    Map()
  }

}