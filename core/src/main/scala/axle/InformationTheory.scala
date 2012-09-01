package axle

import collection._
import math.log
import axle._
import axle.quanta.Information
import axle.stats._

object InformationTheory {

  import axle.Statistics._
  import Information._

  def log2(x: Double) = log(x) / log(2)

  // TODO: filter out P(X eq x) == 0
  def entropy[A](X: RandomVariable[A]) = rv2it(X).Σ(x => -P(X eq x)() * log2(P(X eq x)())) *: bit

  // def H_:(): Information#UOM = entropy()

  def huffmanCode[A, S](alphabet: Set[S]): Map[A, Seq[S]] = {
    //   // TODO
    //   // http://en.wikipedia.org/wiki/Huffman_coding
    Map()
  }

}
