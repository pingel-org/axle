package axle

import scala.Vector
import scala.collection.GenTraversable

import axle.quanta.Information
import axle.quanta.Information.bit
import axle.stats.Case
import axle.stats.ConditionalProbabilityTable0
import axle.stats.EnrichedCaseGenTraversable
import axle.stats.RandomVariable
import spire.algebra.AdditiveMonoid
import spire.algebra.Field
import spire.algebra.NRoot
import spire.algebra.Order
import spire.algebra.Ring
import spire.implicits.additiveGroupOps
import spire.implicits.convertableOps
import spire.implicits.literalIntAdditiveGroupOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.nrootOps
import spire.implicits.orderOps
import spire.implicits.semiringOps
import spire.math.ConvertableFrom
import spire.math.Number
import spire.math.Rational
import spire.math.Real
import spire.random.Dist

package object stats {

  implicit val rationalProbabilityDist: Dist[Rational] = {
    val biggishInt = 1000000
    Dist(Rational(_: Int, biggishInt))(Dist.intrange(0, biggishInt))
  }

  //implicit def evalProbability[N]: Probability[N] => N = _()

  implicit def enrichCaseGenTraversable[A: Manifest, N: Field](cgt: GenTraversable[Case[A, N]]): EnrichedCaseGenTraversable[A, N] = EnrichedCaseGenTraversable(cgt)

  val sides = Vector('HEAD, 'TAIL)

  def coin(pHead: Rational = Rational(1, 2)): RandomVariable[Symbol, Rational] =
    RandomVariable0("coin", new ConditionalProbabilityTable0[Symbol, Rational](Map('HEAD -> pHead, 'TAIL -> (1 - pHead))))

  def log2[N: Field: ConvertableFrom](x: N) = math.log(x.toDouble) / math.log(2)

  def mean[N: Field: Manifest](xs: GenTraversable[N]): N = Σ(xs)(identity) / xs.size

  def square[N: Ring](x: N): N = x ** 2

  /**
   * http://en.wikipedia.org/wiki/Standard_deviation
   */

  def stddev[N: NRoot: Field: Manifest: AdditiveMonoid](xs: GenTraversable[N]): N = {
    val adder = implicitly[AdditiveMonoid[N]]
    val μ = mean(xs)
    (Σ(xs.map(x => square(x - μ)))(identity)(adder) / xs.size).sqrt
  }

  def σ[N: NRoot: Field: Manifest: AdditiveMonoid](xs: GenTraversable[N]): N = stddev(xs)
  
  import Information._
  import axle.quanta._

  def entropy[A: Manifest, N: Field: Order: ConvertableFrom](X: RandomVariable[A, N]): Information.Q = {
    val adder = implicitly[AdditiveMonoid[Real]]
    val field = implicitly[Field[N]]
    val cf = implicitly[ConvertableFrom[N]]
    val H = Σ(X.values)({ x =>
      val px = P(X is x).apply()
      if (px > field.zero) Real(cf.toDouble(-px * log2(px))) else adder.zero
    })(adder)
    Number(H.toDouble) *: bit // TODO Number(_.toDouble) should not be necessary
  }

  def H[A: Manifest, N: Field: Order: ConvertableFrom](X: RandomVariable[A, N]): Information.Q = entropy(X)

  def huffmanCode[A, S](alphabet: Set[S]): Map[A, Seq[S]] = {
    // TODO
    // http://en.wikipedia.org/wiki/Huffman_coding
    Map()
  }

}
