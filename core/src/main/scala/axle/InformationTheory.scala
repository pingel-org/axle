package axle

import collection._
import math.log
import axle.Enrichments._
import axle.quanta.Information

object InformationTheory {

  import axle.Statistics._
  import Information._

  def log2(x: Double) = log(x) / log(2)

  def entropy[A](X: RandomVariable[A]) = rv2it(X).Σ(x => {
    val px = P(X eq x)()
    px match {
      case 0.0 => 0.0
      case _ => -px * log2(px)
    }
  }) *: bit

  // def H_:(): Information#UOM = entropy()

  def huffmanCode[A, S](alphabet: Set[S]): Map[A, Seq[S]] = {
    //   // TODO
    //   // http://en.wikipedia.org/wiki/Huffman_coding
    Map()
  }

}
