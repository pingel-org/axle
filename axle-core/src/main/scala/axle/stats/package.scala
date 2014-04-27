package axle

import scala.collection.GenTraversable
import axle._
import axle.quanta.Information
import spire.algebra._
import spire.implicits._
import spire.math._
import spire.random.Dist

package object stats {

  implicit val rationalProbabilityDist: Dist[Rational] = {
    val biggishInt = 1000000
    Dist(Rational(_: Int, biggishInt))(Dist.intrange(0, biggishInt))
  }

  implicit def evalProbability[N]: Probability[N] => N = _()

  implicit def rv2it[K, N: Field](rv: RandomVariable[K, N]): IndexedSeq[K] = rv.values.getOrElse(Vector())

  implicit def enrichCaseGenTraversable[A: Manifest, N: Field](cgt: GenTraversable[Case[A, N]]): EnrichedCaseGenTraversable[A, N] = EnrichedCaseGenTraversable(cgt)

  def coin(pHead: Rational = Rational(1, 2)): RandomVariable[Symbol, Rational] =
    RandomVariable0("coin",
      Some(List('HEAD, 'TAIL).toIndexedSeq),
      distribution = Some(new ConditionalProbabilityTable0(Map('HEAD -> pHead, 'TAIL -> (1 - pHead)))))

  def log2[N: Field: ConvertableFrom](x: N) = math.log(x.toDouble) / math.log(2)

  def mean[N: Field: Manifest](xs: GenTraversable[N]): N = Σ(xs)(identity) / xs.size

  def square[N: Ring](x: N): N = x ** 2

  // http://en.wikipedia.org/wiki/Standard_deviation

  def stddev[N: NRoot: Field: Manifest: AdditiveMonoid](xs: GenTraversable[N]): N = {
    val adder = implicitly[AdditiveMonoid[N]]
    val μ = mean(xs)
    (Σ(xs.map(x => square(x - μ)))(identity)(adder) / xs.size).sqrt
  }

  import Information._
  import axle.quanta._

  def entropy[A: Manifest, N: Field: Order: ConvertableFrom](X: RandomVariable[A, N]): Information.Q = {
    val adder = implicitly[AdditiveMonoid[Real]]
    val field = implicitly[Field[N]]
    val cf = implicitly[ConvertableFrom[N]]
    val H = X.values.map { xs =>
      Σ(xs)({ x =>
        val px = P(X is x).apply()
        if (px > field.zero) Real(cf.toDouble(-px * log2(px))) else adder.zero
      })(adder)
    }.getOrElse(adder.zero)
    Number(H.toDouble) *: bit // TODO Number(_.toDouble) should not be necessary
  }

  def H[A: Manifest, N: Field: Order: ConvertableFrom](X: RandomVariable[A, N]): Information.Q = entropy(X)

  def huffmanCode[A, S](alphabet: Set[S]): Map[A, Seq[S]] = {
    // TODO
    // http://en.wikipedia.org/wiki/Huffman_coding
    Map()
  }

}
