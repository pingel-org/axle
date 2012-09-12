package axle

import axle._
import collection._
import axle.quanta.Information
import math.log
import scalaz._
import Scalaz._

package object stats {

  implicit def rv2it[K](rv: RandomVariable[K]): IndexedSeq[K] = rv.values.getOrElse(Vector())
  
  implicit def enrichCaseGenTraversable[A](cgt: GenTraversable[Case[A]]) = EnrichedCaseGenTraversable(cgt)

  def coin(pHead: Double = 0.5) = RandomVariable0("coin",
    Some(List('HEAD, 'TAIL).toIndexedSeq),
    distribution = Some(new ConditionalProbabilityTable0(immutable.Map('HEAD -> pHead, 'TAIL -> (1.0 - pHead)))))

  def die(n: Int) = RandomVariable0("d"+n, Some(1 to n),
    distribution = Some(new ConditionalProbabilityTable0((1 to n).map(i => (i, 1d/n)).toMap)))

  def utfD6() = RandomVariable0("UTF d6",
    Some(List('⚀, '⚁, '⚂, '⚃, '⚄, '⚅).toIndexedSeq),
    distribution = Some(new ConditionalProbabilityTable0(immutable.Map(
      '⚀ -> 1d / 6,
      '⚁ -> 1d / 6,
      '⚂ -> 1d / 6,
      '⚃ -> 1d / 6,
      '⚄ -> 1d / 6,
      '⚅ -> 1d / 6))))

  def log2(x: Double) = log(x) / log(2)

  import Information._

  def entropy[A](X: RandomVariable[A]): Information.UOM = X.values.map(_.Σ(x => {
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
