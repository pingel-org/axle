package axle

import axle._
import collection._
import axle.quanta.Information
import math.log

package object stats {

  implicit def probability2double = (p: Probability) => p()
  
  implicit def rv2it[K](rv: RandomVariable[K]): IndexedSeq[K] = rv.values.getOrElse(Vector())
  
  implicit def enrichCaseGenTraversable[A : ClassManifest](cgt: GenTraversable[Case[A]]) = EnrichedCaseGenTraversable(cgt)

  def coin(pHead: Double = 0.5) = RandomVariable0("coin",
    Some(List('HEAD, 'TAIL).toIndexedSeq),
    distribution = Some(new ConditionalProbabilityTable0(immutable.Map('HEAD -> pHead, 'TAIL -> (1.0 - pHead)))))

  def log2(x: Double) = log(x) / log(2)

  import Information._
  import axle.quanta._
  
  def entropy[A : ClassManifest](X: RandomVariable[A]): Information.Q = X.values.map(_.Σ(x => {
    val px = P(X eq x)()
    if (px > 0) (-px * log2(px)) else 0.0
  })).getOrElse(0.0) *: bit

  def H[A : ClassManifest](X: RandomVariable[A]): Information.Q = entropy(X)

  def huffmanCode[A, S](alphabet: Set[S]): Map[A, Seq[S]] = {
    //   // TODO
    //   // http://en.wikipedia.org/wiki/Huffman_coding
    Map()
  }

}
