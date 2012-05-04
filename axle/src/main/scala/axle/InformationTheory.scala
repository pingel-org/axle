package axle

import scala.collection._
import scala.math.log

import org.pingel.axle.quanta.Information

object InformationTheory {

  def log2(x: Double) = log(x) / log(2)

  trait Distribution[A, INPUT] {

    def getValues(): Set[A]

    // val d: Map[A, Double]

    def choose(): A

    def entropy(): Information#UOM

    def huffmanCode[S](alphabet: Set[S]): Map[A, Seq[S]] = {
      // TODO
      // http://en.wikipedia.org/wiki/Huffman_coding
      Map()
    }

  }

  // Conditional probability table
  class CPT2[A, P1, P2](
    prior1: RandomVariable[P1, _],
    prior2: RandomVariable[P2, _],
    values: Set[A],
    m: Map[(P1, P2), Map[A, Double]]) extends Distribution[A, (P1, P2)] {

    import Information._
    
    def getValues() = values

    def entropy() = {
      val n = 1 // TODO
      n *: bit
    }

    def choose(): A = {
      // val pv1 = prior1.choose
      // val pv2 = prior2.choose
      null.asInstanceOf[A] // TODO
    }
    
  }

  def cpt[A, P1, P2](
    p1: RandomVariable[P1, _],
    p2: RandomVariable[P2, _],
    values: Set[A],
    m: Map[(P1, P2), Map[A, Double]]) = new CPT2(p1, p2, values, m)

  def distribution[A](m: Map[A, Double]) = {

    // TODO: verify that the distribution sums to 1

    new Distribution[A, Unit] {

      import Information._

      override def getValues() = m.keySet

      def choose(): A = {
        var r = scala.math.random
        for ((k, v) <- m) {
          r -= v
          if (r < 0) {
            return k
          }
        }
        throw new Exception("malformed distribution")
      }

      def entropy() = {
        val n = m.values.foldLeft(0.0)((x: Double, y: Double) => x - y * log2(y))
        n *: bit
      }

    }
  }

  def coin(pHead: Double = 0.5) = distribution(Map('HEAD -> pHead, 'TAIL -> (1.0 - pHead)))

  trait Case {
    def ∧(right: Case) = CaseAnd(this, right)
    def ∨(right: Case) = CaseOr(this, right)
    def |(given: Case) = CaseGiven(this, given)
  }

  case class CaseAnd(left: Case, right: Case) extends Case {

  }

  case class CaseOr(left: Case, right: Case) extends Case {

  }

  case class CaseGiven(c: Case, given: Case) extends Case {
    // TODO: use phantom types to ensure that only one "given" clause is specified
  }

  case class CaseIs[A](v: A) extends Case {

  }

  case class CaseIsnt[A](v: A) extends Case {

  }

  case class RandomVariable[A, INPUT](name: String, distribution: Distribution[A, INPUT]) {
    def ==(v: A) = CaseIs(v)
    def !=(v: A) = CaseIsnt(v)
  }

  // Probability of
  case class P(c: Case) extends Function0[Double] {
    def *(right: () => Double) = PMultiply(this, right)
    def apply() = 1.0
  }

  case class PMultiply(left: P, right: () => Double) extends Function0[Double] {
    def apply() = 1.0
  }

  def bayes(p: P): () => Double = p.c match {
    // TODO: also check that "left" and "right" have no "given"
    case CaseAnd(left, right) => P(left | right) * bayes(P(right))
    case _ => P(p.c)
  }

}

object Test {

  def main(args: Array[String]): Unit = {

    import InformationTheory._

    println("Information Theory test")

    val d = distribution(Map("A" -> 0.2, "B" -> 0.1, "C" -> 0.7))
    (0 until 100).map(i => print(d.choose))
    println
    println("entropy: " + d.entropy)

    val X = RandomVariable("X", distribution(Map("foo" -> 0.1, "food" -> 0.9)))
    val Y = RandomVariable("Y", distribution(Map("bar" -> 0.9, "bard" -> 0.1)))

    val A = RandomVariable("A",
      cpt(X, Y, Set("a", "b"),
        Map(
          ("foo", "bar") -> Map("a" -> 0.3, "b" -> 0.7),
          ("foo", "bard") -> Map("a" -> 0.2, "b" -> 0.8),
          ("food", "bar") -> Map("a" -> 0.9, "b" -> 0.1),
          ("food", "bard") -> Map("a" -> 0.5, "b" -> 0.5)
        )
      )
    )

    val p = P((A == "a") | (X == "foo") ∧ (Y != "bar"))
    println("p = " + p)
    println("p() = " + p())

    val b = bayes(P((A == "a") ∧ (X == "foo")))
    println("b = " + b)
    println("b() = " + b())

    val fairCoin = coin()
    (0 until 100).map(i => print(fairCoin.choose))
    println
    println("entropy: " + fairCoin.entropy)

    val biasedCoin = coin(0.9)
    (0 until 100).map(i => print(biasedCoin.choose))
    println
    println("entropy: " + biasedCoin.entropy)

  }

}