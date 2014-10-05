package axle

import scala.Vector
import scala.collection.GenTraversable

import axle.quanta.UnittedQuantity
import axle.quanta.Information
import axle.quanta.Information.bit
import axle.stats.Case
import axle.stats.ConditionalProbabilityTable0
import axle.stats.Distribution
import axle.stats.EnrichedCaseGenTraversable
import axle.stats.P
import spire.algebra.AdditiveMonoid
import spire.algebra.Eq
import spire.algebra.Field
import spire.algebra.NRoot
import spire.algebra.Order
import spire.algebra.Ring
import spire.implicits.additiveGroupOps
import spire.implicits.convertableOps
import spire.implicits.literalIntAdditiveGroupOps
import spire.implicits.moduleOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.nrootOps
import spire.implicits.orderOps
import spire.implicits.semiringOps
import spire.implicits._
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

  implicit def enrichCaseGenTraversable[A: Manifest, N: Field](cgt: Iterable[Case[A, N]]): EnrichedCaseGenTraversable[A, N] = EnrichedCaseGenTraversable(cgt)

  val sides = Vector('HEAD, 'TAIL)

  def coin(pHead: Rational = Rational(1, 2)): Distribution[Symbol, Rational] =
    new ConditionalProbabilityTable0[Symbol, Rational](
      Map('HEAD -> pHead, 'TAIL -> (1 - pHead)), "coin")

  def binaryDecision(yes: Rational): Distribution0[Boolean, Rational] =
    new ConditionalProbabilityTable0(Map(true -> yes, false -> (1 - yes)), s"binaryDecision $yes")

  def uniformDistribution[T](values: Seq[T], name: String): Distribution0[T, Rational] =
    new ConditionalProbabilityTable0(values.map(v => (v, Rational(1, values.size))).toMap, name)

  def iffy[C: Eq, N: Field: Order: Dist](
    decision: Distribution0[Boolean, N],
    trueBranch: Distribution[C, N],
    falseBranch: Distribution[C, N]): Distribution0[C, N] = {

    val addN = implicitly[AdditiveMonoid[N]]
    val pTrue = decision.probabilityOf(true)
    val pFalse = decision.probabilityOf(false)

    val parts = (trueBranch.values.map(v => (v, trueBranch.probabilityOf(v) * pTrue)) ++
      falseBranch.values.map(v => (v, falseBranch.probabilityOf(v) * pFalse)))

    val newDist = parts.groupBy(_._1).mapValues(xs => xs.map(_._2).reduce(addN.plus)).toMap

    new ConditionalProbabilityTable0(newDist, "todo")
  }

  def log2[N: Field: ConvertableFrom](x: N) = math.log(x.toDouble) / math.log(2)

  def mean[N: Field: Manifest](xs: GenTraversable[N]): N = Σ(xs)(identity) / xs.size

  def square[N: Ring](x: N): N = x ** 2

  /**
   * http://en.wikipedia.org/wiki/Standard_deviation
   */

  def stddev[N: NRoot: Field: Manifest: AdditiveMonoid](xs: GenTraversable[N]): N = {
    val μ = mean(xs)
    (Σ(xs)(x => square(x - μ)) / xs.size).sqrt
  }

  // A: NRoot: Field: Manifest: AdditiveMonoid
  // TODO Distribution should have type [A, N]
  def standardDeviation[N: NRoot: Field: Manifest: AdditiveMonoid](distribution: Distribution[N, N]): N = {
    val xs = distribution.values
    val μ = Σ(xs)(x => distribution.probabilityOf(x) * x)
    (Σ(xs)(x => distribution.probabilityOf(x) * square(x - μ))).sqrt
  }

  def σ[N: NRoot: Field: Manifest: AdditiveMonoid](xs: GenTraversable[N]): N = stddev(xs)

  def entropy[A: Manifest, N: Field: Order: ConvertableFrom](X: Distribution[A, N]): UnittedQuantity[Information, Real] = {
    import Information._
    val convertN = implicitly[ConvertableFrom[N]]
    val H = Σ(X.values) { x =>
      val px: N = P(X is x).apply()
      if (implicitly[Order[N]].gt(px, implicitly[Field[N]].zero)) {
        convertN.toReal(-px) * log2(px)
      } else {
        implicitly[Field[Real]].zero
      }
    }
    UnittedQuantity(H, bit[Real])
  }

  def H[A: Manifest, N: Field: Order: ConvertableFrom](X: Distribution[A, N]): UnittedQuantity[Information, Real] = entropy(X)

  def huffmanCode[A, S](alphabet: Set[S]): Map[A, Seq[S]] = {
    // TODO
    // http://en.wikipedia.org/wiki/Huffman_coding
    Map()
  }

}
