package org.pingel.axle

import scala.collection._
import scala.math.log

object InformationTheory {

  def log2(x: Double) = log(x) / log(2)
  
  trait Distribution[A] {

    def getValues(): Set[A]

    val d: Map[A, Double]

    def choose(): A = {
      var r = scala.math.random
      for ((k, v) <- d) {
        r -= v
        if (r < 0) {
          return k
        }
      }
      throw new Exception("malformed distribution")
    }

    def entropy(): Double =
      d.values.foldLeft(0.0)((x: Double, y: Double) => x - y * log2(y))

    def huffmanCode[S](alphabet: Set[S]): Map[A, Seq[S]] = {
      // TODO
      // http://en.wikipedia.org/wiki/Huffman_coding
      Map()
    }
      
  }

  object Distribution {
    
    def makeDistribution[A](m: Map[A, Double]) = {
      // TODO: verify that the distribution sums to 1
      new Distribution[A] {
        override def getValues() = m.keySet
        val d = m
      }
    }

    def makeCoin(pHead: Double=0.5) = makeDistribution(Map('HEAD -> pHead, 'TAIL -> (1.0 - pHead)))

  }

}

object Test {

  def main(args: Array[String]): Unit = {

    import InformationTheory.Distribution._

    println("Information Theory test")

    val d = makeDistribution(Map("A" -> 0.2, "B" -> 0.1, "C" -> 0.7))
    (0 until 100).map(i => print(d.choose))
    println
    println("entropy: " + d.entropy)

    val coin = makeCoin()
    (0 until 100).map(i => print(coin.choose))
    println
    println("entropy: " + coin.entropy)

    val biasedCoin = makeCoin(0.9)
    (0 until 100).map(i => print(biasedCoin.choose))
    println
    println("entropy: " + biasedCoin.entropy)

  }

}