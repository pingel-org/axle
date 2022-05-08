# Randomness and Uncertainty

## Probability Model

Modeling probability, randomness, and uncertainly is one of the primary objectives of Axle.

The capabilies are available via four typeclasses and one trait

* Sampler
* Region (trait modeling σ-algebra)
* Kolmogorov
* Bayes
* Monad (`cats.Monad`)

Concrete number type are avoided in favor of structures from Abstract Algebra --
primarily `Ring` and `Field`.
These are represented as context bounds, usually passed implicitly.

The examples in this document use the `spire.math.Rational` number type, but work as well
for `Double`, `Float`, etc.
The precise number type `Rational` is used in tests because their precision allows the assertions to be expressed without any error tolerance.

### Imports

Preamble to pull in the commonly-used functions in this section:

```scala mdoc:silent
import cats.implicits._
import cats.effect._

import spire.math._
import spire.algebra._

import axle.probability._
import axle.algebra._
import axle.visualize._
import axle.web._
```

### Creating Probability Models

There are a few type of probability models in Axle.
The simplest is the `ConditionalProbabilityTable`, which is used throughout this document.

`axle.data.Coin.flipModel` demonstrates a very simple probability model for type `Symbol`.

This is its implementation:

```scala mdoc:silent
val head = Symbol("HEAD")
val tail = Symbol("TAIL")

def flipModel(pHead: Rational = Rational(1, 2)): ConditionalProbabilityTable[Symbol, Rational] =
  ConditionalProbabilityTable[Symbol, Rational](
    Map(
      head -> pHead,
      tail -> (1 - pHead)))
```

Its argument is the bias for the `HEAD` side.
Without a provided bias, it is assumed to be a fair coin.

```scala mdoc
val fairCoin = flipModel()

val biasedCoin = flipModel(Rational(9, 10))
```

Rolls of dice are another common example.

```scala mdoc:silent
def rollModel(n: Int): ConditionalProbabilityTable[Int, Rational] =
  ConditionalProbabilityTable(
    (1 to n).map(i => (i, Rational(1, n.toLong))).toMap)
```

The values `d6` and `d10` model rolls of 6 and 10-sided dice.

```scala mdoc
val d6 = rollModel(6)

val d10 = rollModel(10)
```

Define a visualization of the distribution of events in the `d6` model:

```scala mdoc:silent
val d6vis = BarChart[Int, Rational, ConditionalProbabilityTable[Int, Rational], String](
  () => d6,
  colorOf = _ => Color.blue,
  xAxis = Some(Rational(0)),
  title = Some("d6"),
  labelAngle = Some(0d *: angleDouble.degree),
  drawKey = false)
```

Create an SVG

```scala mdoc:silent
d6vis.svg[IO]("@DOCWD@/images/d6.svg").unsafeRunSync()
```

![d6](/images/d6.svg)

### Sampler

The `Sampler` typeclass provides the ability to "execute" the model and product
a random sample via the `sample` method.

It's type signature is:

```scala
def sample(gen: Generator)(implicit spireDist: Dist[V], ringV: Ring[V], orderV: Order[V]): A
```

These imports make available a `Generator` as source of entropy

```scala mdoc
import spire.random._

val rng = Random.generatorFromSeed(Seed(42))
```

And then the `.sample` syntax:

```scala mdoc:silent
import axle.syntax.sampler._
```

`sample` requires a Spire `Generator`.
It also requires context bounds on the value type `V` that give the method
the ability to produces values with a distribution conforming to the probability model.

```scala mdoc
(1 to 10) map { _ => fairCoin.sample(rng) }
```

```scala mdoc
(1 to 10) map { _ => biasedCoin.sample(rng) }
```

```scala mdoc
(1 to 10) map { _ => d6.sample(rng) }
```

Simulate 1k rolls of one d6

```scala mdoc:silent
val rolls = (0 until 1000) map { _ => d6.sample(rng) }
```

Then tally them

```scala mdoc:silent
implicit val ringInt: CRing[Int] = spire.implicits.IntAlgebra

import axle.syntax.talliable._
```

```scala mdoc
val oneKd6Histogram = rolls.tally
```

Create a visualization

```scala mdoc:silent
val d6oneKvis = BarChart[Int, Int, Map[Int, Int], String](
  () => oneKd6Histogram,
  colorOf = _ => Color.blue,
  xAxis = Some(0),
  title = Some("1,000 d6 samples"),
  labelAngle = Some(0d *: angleDouble.degree),
  drawKey = false)
```

Create the SVG

```scala mdoc:silent
d6oneKvis.svg[IO]("@DOCWD@/images/d6-1Ksamples.svg").unsafeRunSync()
```

![1k d6 samples](/images/d6-1Ksamples.svg)

### Sigma Algebra Regions

The sealed `Region[A]` trait is extended by the following case classes
that form a way to describe expressions on the event-space of a probability model.
In Measure Theory, these expressions are said to form a "sigma-algebra" ("σ-algebra")

In order of arity, they case classes extending this trait are:

#### Arity 0

* `RegionEmpty` never matches any events or probability mass
* `RegionAll` always matches all events and probability mass

#### Arity 1 (not including typeclass witnesses)

* `RegionEq` matches when an event is equal to the supplied object, with respect to the supplied `cats.kernel.Eq[A]` witness.
* `RegionIf` matches when the supplied condition function returns `true`
* `RegionSet` matches when an event is contained within the supplied `Set[A]`.
* `RegionNegate` negates the supplied region.
* `RegionGTE` is true when an event is greater than or equal to the supplied object, with respect to the supplied `cats.kernel.Order[A]`
* `RegionLTE` is true when an event is less than or equal to the supplied object, with respect to the supplied `cats.kernel.Order[A]`

#### Arity 2

* `RegionAnd` is the conjunction of both arguments.  It can be created using the `and` method in the `Region` trait.
* `RegionOr` is the disjunction of both arguments.  It can be created using the `or` method in the `Region` trait.

Note that a "Random Variable" does not appear in this discussion.
The `axle.probability.Variable` class does define a `is` method that returns a `RegionEq`,
but the probability models themselves are not concerned with the notion of a
`Variable`.
They are simply models over regions of events on their single, opaque type
that adhere to the laws of probability.

The eventual formalization of `Region` should connect it with a σ-algebra from Meaasure Theory.

### Kolmogorov for querying Probability Models

#### probabilityOf (aka "P")

The method `probabilityOf` is defined in terms of a `Region`.

```scala
def probabilityOf(predicate: Region[A])(implicit fieldV: Field[V]): V
```

Note that `probabilityOf` is aliased to `P` in `axle.syntax.kolmogorov._`

```scala mdoc:silent
import axle.syntax.kolmogorov._
```

The probability of a `head` for a single toss of a fair coin is `1/2`

```scala mdoc
fairCoin.P(RegionEq(head))
```

The probability that a toss is not `head` is also `1/2`.

```scala mdoc
fairCoin.P(RegionNegate(RegionEq(head)))
```

The probability that a toss is both `head` and `tail` is zero.

```scala mdoc
fairCoin.P(RegionEq(head) and RegionEq(tail))
```

The probability that a toss is either `head` or `tail` is one.

```scala mdoc
fairCoin.P(RegionEq(head) or RegionEq(tail))
```

#### Kolmogorov's Axioms

The single `probabilityOf` method together with the `Region` trait
is enough to define Kolmogorov's Axioms of Probability.
The axioms are implemented in `axle.laws.KolmogorovProbabilityAxioms` and
checked during testing with ScalaCheck.

##### Basic Measure

Probabilities are non-negative.

```scala
model.P(region) >= Field[V].zero
```

##### Unit Measure

The sum the probabilities of all possible events is `one`

```scala
model.P(RegionAll()) === Field[V].one
```

##### Combination

For disjoint event regions, `e1` and `e2`, the probability of their disjunction `e1 or e2`
is equal to the sum of their independent probabilities.

```scala
(!((e1 and e2) === RegionEmpty() )) || (model.P(e1 or e2) === model.P(e1) + model.P(e2))
```

### Bayes Theorem, Conditioning, and Filtering

The `Bayes` typeclass implements the conditioning of a probability model
via the `filter` (`|` is also an alias).

```scala
def filter(predicate: Region[A])(implicit fieldV: Field[V]): M[A, V]
```

Syntax is available via this import

```scala mdoc:silent
import axle.syntax.bayes._
```

`filter` -- along with `probabilityOf` from `Kolomogorov` -- allows Bayes' Theorem
to be expressed and checked with ScalaCheck.

```scala
model.filter(b).P(a) * model.P(b) === model.filter(a).P(b) * model.P(a)
```

For non-zero `model.P(a)` and `model.P(b)`

The theorem is more recognizable as `P(A|B) = P(B|A) * P(A) / P(B)`

Filter is easier to motivate with composite types, but two examples
with a d6 show the expected semantics:

Filtering the d6 roll model to 1 and 5:

```scala mdoc
d6.filter(RegionIf(_ % 4 == 1))
```

Filter the d6 roll model to 1, 2, and 3:

```scala mdoc
d6.filter(RegionLTE(3))
```

### Probability Model as Monads

The `pure`, `map`, and `flatMap` methods of `cats.Monad` are defined
for `ConditionalProbabilityTable`, `TallyDistribution`.

#### Monad Laws

The short version is that the three methods are constrained by a few laws that
make them very useful for composing programs.
Those laws are:

* Left identity: `pure(x).flatMap(f) === f(x)`
* Right identity: `model.flatMap(pure) === model`
* Associativity: `model.flatMap(f).flatMap(g) === model.flatMap(f.flatMap(g))`

#### Monad Syntax

There is syntax support in `cats.implicits._` for all three methods.

However, due to limitations of Scala's type inference, it cannot see
`ConditionalProbabilityTable[E, V]` as the `M[_]` expected by `Monad`.

The most straigtfoward workaround is just to conjure the monad witness
directly and use it, passing the model in as the sole argument to the
first parameter group.

```scala mdoc:silent
val monad = ConditionalProbabilityTable.monadWitness[Rational]
```

```scala mdoc
monad.flatMap(d6) { a => monad.map(d6) { b => a + b } }
```

Another strategy to use `map` and `flatMap` directly on
the model is a type that can be seen as `M[_]` along with
a type annotation:

```scala mdoc:silent
type CPTR[E] = ConditionalProbabilityTable[E, Rational]

(d6: CPTR[Int]).flatMap { a => (d6: CPTR[Int]).map { b => a + b } }
```

Or similar to use a for comprehension:

```scala mdoc:silent
for {
  a <- d6: CPTR[Int]
  b <- d6: CPTR[Int]
} yield a + b
```

#### Chaining models

Chain two events' models

```scala mdoc
val bothCoinsModel = monad.flatMap(fairCoin) { flip1 =>
  monad.map(fairCoin) { flip2 =>
    (flip1, flip2)
  }
}
```

This creates a model on events of type `(Symbol, Symbol)`

It can be queried with `P` using `RegionIf` to check fields within the `Tuple2`.

```scala mdoc
type TWOFLIPS = (Symbol, Symbol)

bothCoinsModel.P(RegionIf[TWOFLIPS](_._1 == head) and RegionIf[TWOFLIPS](_._2 == head))

bothCoinsModel.P(RegionIf[TWOFLIPS](_._1 == head) or RegionIf[TWOFLIPS](_._2 == head))
```

### Summing two dice rolls

```scala mdoc
val twoDiceSummed = monad.flatMap(d6) { a =>
  monad.map(d6) { b =>
    a + b
  }
}
```

Create a visualization

```scala mdoc:silent
val monadicChart = BarChart[Int, Rational, ConditionalProbabilityTable[Int, Rational], String](
  () => twoDiceSummed,
  colorOf = _ => Color.blue,
  xAxis = Some(Rational(0)),
  title = Some("d6 + d6"),
  labelAngle = Some(0d *: angleDouble.degree),
  drawKey = false)
```

Create SVG

```scala mdoc:silent
monadicChart.svg[IO]("@DOCWD@/images/distributionMonad.svg").unsafeRunSync()
```

![Monadic d6 + d6](/images/distributionMonad.svg)

#### Iffy

A stochastic version of `if` (aka `iffy`) can be implemented in terms of `flatMap`
using this pattern for any probability model type `M[A]` such that a `Monad` is defined.

```scala
def iffy[A, B, M[_]: Monad](
  input      : M[A],
  predicate  : A => Boolean,
  trueClause : M[B],
  falseClause: M[B]): M[B] =
  input.flatMap { i =>
    if( predicate(i) ) {
      trueClause
    } else {
      falseClause
    }
  }
```

An example of that pattern: "if heads, d6+d6, otherwise d10+d10"

```scala mdoc:silent
import cats.Eq

val headsD6D6taildD10D10 = monad.flatMap(fairCoin) { side =>
  if( Eq[Symbol].eqv(side, head) ) {
    monad.flatMap(d6) { a => monad.map(d6) { b => a + b } }
  } else {
    monad.flatMap(d10) { a => monad.map(d10) { b => a + b } }
  }
}
```

Create visualization

```scala mdoc:silent
val iffyChart = BarChart[Int, Rational, ConditionalProbabilityTable[Int, Rational], String](
  () => headsD6D6taildD10D10,
  colorOf = _ => Color.blue,
  xAxis = Some(Rational(0)),
  title = Some("if heads, d6+d6, else d10+d10"),
  labelAngle = Some(0d *: angleDouble.degree),
  drawKey = false)
```

Create the SVG

```scala mdoc:silent
iffyChart.svg[IO]("@DOCWD@/images/iffy.svg").unsafeRunSync()
```

![heads => d6+d6, else d10+d10](/images/iffy.svg)

#### Further Reading

Motiviating the Monad typeclass is out of scope of this document.
Please see the functional programming literature for more about monads
and their relationship to functors, applicative functors, monoids, categories,
and other structures.

For some historical reading on the origins of probability monads,
see the literature on the Giry Monad.

### Future work

#### Measure Theory

Further refining and extending Axle to incorporate Measure Theory is a likely follow-on step.

#### Markov Categories

As an alternative to Measure Theory, see Tobias Fritz's work on Markov Categories

#### Probabilistic and Differentiable Programming

In general, the explosion of work on probabilistic and differentible programming is fertile ground for Axle's lawful approach.

## Statistics

Common imports and implicits

```scala mdoc:silent:reset
import cats.implicits._
import spire.algebra._
import axle.probability._

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
```

### Uniform Distribution

Example

```scala mdoc
val X = uniformDistribution(List(2d, 4d, 4d, 4d, 5d, 5d, 7d, 9d))
```

### Standard Deviation

Example

```scala mdoc:silent
import axle.stats._

implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra
```

```scala mdoc
standardDeviation(X)
```

See also [Probability Model](#probability-model)

## Root-mean-square deviation

See the Wikipedia page on
[Root-mean-square deviation](https://en.wikipedia.org/wiki/Root-mean-square_deviation).

```scala mdoc:silent:reset
import cats.implicits._

import spire.algebra.Field
import spire.algebra.NRoot

import axle.stats._

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra
```

Given four numbers and an estimator function, compute the RMSD:

```scala mdoc:silent
val data = List(1d, 2d, 3d, 4d)
```

```scala mdoc
def estimator(x: Double): Double =
  x + 0.2

rootMeanSquareDeviation[List, Double](data, estimator)
```

## Reservoir Sampling

Reservoir Sampling is the answer to a common interview question.

```scala mdoc:silent:reset
import spire.random.Generator.rng
import spire.algebra.Field

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra

import axle.stats._
```

Demonstrate it uniformly sampling 15 of the first 100 integers

```scala mdoc
val sample = reservoirSampleK(15, LazyList.from(1), rng).drop(100).head
```

The mean of the sample should be in the ballpark of the mean of the entire list (50.5):

```scala mdoc
import axle.math.arithmeticMean

arithmeticMean(sample.map(_.toDouble))
```

Indeed it is.

## Information Theory

### Entropy

The calculation of the entropy of a distribution is available as a function called `entropy`
as well as the traditional `H`:

Imports and implicits

```scala mdoc:silent:reset
import edu.uci.ics.jung.graph.DirectedSparseGraph

import cats.implicits._

import spire.math._
import spire.algebra._

import axle._
import axle.probability._
import axle.stats._
import axle.quanta.Information
import axle.jung.directedGraphJung
import axle.data.Coin
import axle.game.Dice.die

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra

implicit val informationConverter = Information.converterGraphK2[Double, DirectedSparseGraph]
```

Usage

Entropy of fair 6-sided die

```scala mdoc
val d6 = die(6)

H[Int, Rational](d6).show
```

Entropy of fair and biased coins

```scala mdoc
val fairCoin = Coin.flipModel()

H[Symbol, Rational](fairCoin).show

val biasedCoin = Coin.flipModel(Rational(7, 10))

entropy[Symbol, Rational](biasedCoin).show
```

See also the following example of the entropy of a biased coin.

### Example: Entropy of a Biased Coin

Visualize the relationship of a coin's bias to its entropy with this code snippet.

Imports and implicits:

```scala mdoc:silent:reset
import scala.collection.immutable.TreeMap
import cats.implicits._
import spire.math.Rational
import spire.algebra._
import axle.stats.H
import axle.data.Coin
import axle.quanta.UnittedQuantity
import axle.quanta.Information

type D = TreeMap[Rational, UnittedQuantity[Information, Double]]

import edu.uci.ics.jung.graph.DirectedSparseGraph
import axle.jung.directedGraphJung
import cats.kernel.Order
import axle.quanta.unittedTics

implicit val id = {
  implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
  Information.converterGraphK2[Double, DirectedSparseGraph]
}

implicit val or: Order[Rational] = new cats.kernel.Order[Rational] {
  implicit val doubleOrder = Order.fromOrdering[Double]
  def compare(x: Rational, y: Rational): Int = doubleOrder.compare(x.toDouble, y.toDouble)
}
implicit val bitDouble = id.bit
```

Create dataset

```scala mdoc:silent
val hm: D =
  new TreeMap[Rational, UnittedQuantity[Information, Double]]() ++
    (0 to 100).map({ i =>
      val r = Rational(i.toLong, 100L)
      r -> H[Symbol, Rational](Coin.flipModel(r))
    }).toMap
```

Define visualization

```scala mdoc:silent
import axle.visualize._

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra

val plot = Plot[String, Rational, UnittedQuantity[Information, Double], D](
  () => List(("h", hm)),
  connect = true,
  colorOf = _ => Color.black,
  drawKey = false,
  xAxisLabel = Some("p(x='HEAD)"),
  yAxisLabel = Some("H"),
  title = Some("Entropy")).zeroAxes
```

Create the SVG

```scala mdoc
import axle.web._
import cats.effect._

plot.svg[IO]("@DOCWD@/images/coinentropy.svg").unsafeRunSync()
```

The result is the classic Claude Shannon graph

![coin entropy](/images/coinentropy.svg)

## Probabilistic Graphical Models

Currently only Bayesian Networks

Eventually others including Pearl's causal models.

### Bayesian Networks

See the Wikipedia page on [Bayesian networks](https://en.wikipedia.org/wiki/Bayesian_network)

### Example: Alarm

Define random variables

```scala mdoc:silent:reset
import axle.probability._

val bools = Vector(true, false)

val B = Variable[Boolean]("Burglary")
val E = Variable[Boolean]("Earthquake")
val A = Variable[Boolean]("Alarm")
val J = Variable[Boolean]("John Calls")
val M = Variable[Boolean]("Mary Calls")
```

Define Factor for each variable

```scala mdoc:silent
import spire.math._
import cats.implicits._

val bFactor =
  Factor(Vector(B -> bools), Map(
    Vector(B is true) -> Rational(1, 1000),
    Vector(B is false) -> Rational(999, 1000)))

val eFactor =
  Factor(Vector(E -> bools), Map(
    Vector(E is true) -> Rational(1, 500),
    Vector(E is false) -> Rational(499, 500)))

val aFactor =
  Factor(Vector(B -> bools, E -> bools, A -> bools), Map(
    Vector(B is false, E is false, A is true) -> Rational(1, 1000),
    Vector(B is false, E is false, A is false) -> Rational(999, 1000),
    Vector(B is true, E is false, A is true) -> Rational(940, 1000),
    Vector(B is true, E is false, A is false) -> Rational(60, 1000),
    Vector(B is false, E is true, A is true) -> Rational(290, 1000),
    Vector(B is false, E is true, A is false) -> Rational(710, 1000),
    Vector(B is true, E is true, A is true) -> Rational(950, 1000),
    Vector(B is true, E is true, A is false) -> Rational(50, 1000)))

val jFactor =
  Factor(Vector(A -> bools, J -> bools), Map(
    Vector(A is true, J is true) -> Rational(9, 10),
    Vector(A is true, J is false) -> Rational(1, 10),
    Vector(A is false, J is true) -> Rational(5, 100),
    Vector(A is false, J is false) -> Rational(95, 100)))

val mFactor =
  Factor(Vector(A -> bools, M -> bools), Map(
    Vector(A is true, M is true) -> Rational(7, 10),
    Vector(A is true, M is false) -> Rational(3, 10),
    Vector(A is false, M is true) -> Rational(1, 100),
    Vector(A is false, M is false) -> Rational(99, 100)))
```

Arrange into a graph

```scala mdoc:silent
import axle.pgm._
import axle.jung._
import edu.uci.ics.jung.graph.DirectedSparseGraph

// edges: ba, ea, aj, am

val bn: BayesianNetwork[Boolean, Rational, DirectedSparseGraph[BayesianNetworkNode[Boolean, Rational], Edge]] =
  BayesianNetwork.withGraphK2[Boolean, Rational, DirectedSparseGraph](
    Map(
      B -> bFactor,
      E -> eFactor,
      A -> aFactor,
      J -> jFactor,
      M -> mFactor))
```

Create an SVG visualization

```scala mdoc:silent
import axle.visualize._

val bnVis  = BayesianNetworkVisualization(bn, 1000, 1000, 20)
```

Render as SVG file

```scala mdoc:silent
import axle.web._
import cats.effect._

bnVis.svg[IO]("@DOCWD@/images/alarm_bayes.svg").unsafeRunSync()
```

![alarm bayes network](/images/alarm_bayes.svg)

The network can be used to compute the joint probability table:

```scala mdoc:silent
import axle.math.showRational

val jpt = bn.jointProbabilityTable
```

```scala mdoc
jpt.show
```

Variables can be summed out of the factor:

```scala mdoc
import axle._

jpt.sumOut(M).sumOut(J).sumOut(A).sumOut(B).sumOut(E)
```

Also written as:

```scala mdoc
jpt.Σ(M).Σ(J).Σ(A).Σ(B).Σ(E)
```

Multiplication of factors also works:

```scala mdoc:silent
import spire.implicits.multiplicativeSemigroupOps

val f = (bn.factorFor(A) * bn.factorFor(B)) * bn.factorFor(E)
```

```scala mdoc
f.show
```

Markov assumptions:

```scala mdoc
bn.markovAssumptionsFor(M).show
```

This is read as "M is independent of E, B, and J given A".

## Probability Model Future Work

### Later

* `{CPT,TD}.tailRecM` then ScalaCheck `Monad[CPT,TD]`
* Functor for CPT, TD

* `SigmaAlgebra` for the CPT
  * Clean up expressions like `RegionIf[TWOROLLS](_._1 == '⚃)`
  * Laws for `Region` ("Sigma Algebra"? [video](https://www.youtube.com/watch?v=21a85f1YS5Q))
  * `OrderedRegion` for the `Order` used in `RegionLTE` and `RegionGTE`?

* Measure Theory

* Test: start with `ABE.jointProbabilityTable` (monotype `tuple5[Boolean]`)
  * Factor out each variable until original 5-note network is reached
  * Basically the inverse of factor multiplication
  * `bn.factorFor(B) * bn.factorFor(E)` should be defined? (It errors)
  * `MonotypeBayesanNetwork.filter` collapase into a single BNN

* Rename `ConditionalProbabilityTable`?

* Laws for `Factor`

* Review `InteractionGraph`, `EliminationGraph`, `JoinTree` and the functions they power

* Consider a "case" to be a `Map` vs a `Vector`
* Consider usefulness of `Factor` in terms of `Region`

* `MonotypeBayesanNetwork`.{`pure`, `map`, `flatMap`, `tailRecR`}
* Reconcile `MBN` `combine1` & `combine2`
* Monad tests for `MonotypeBayesanNetwork[Alarm-Burglary-Earthquake]`

* `Bayes[MonotypeBayesanNetwork]` -- could be viewed as "belief updating" (vs "conditioning")
  * If it took a ProbabilityModel itself

* Bettings odds

* Multi-armed bandit
* Recursive grid search
* P-values
* z & t scores
* Correlation
* Regression
* Accuracy, Precision
* Bias, Variance
* Cohen's Kappa

* Rm throws from axle.stats.TallyDistribution

* do-calculus (Causality)

* Stochastic Lambda Calculus
* Abadi Plotkin pathology
* Jacobian Vector Products (JVP)

* FLDR probability
  * [probcomp github](https://github.com/probcomp/fast-loaded-dice-roller/)
  * [MIT FSAAD slides](http://fsaad.mit.edu/assets/2020-08-20-fldr-slides.pdf)

### Docs

* Reorder Probability mdoc (Creation, Kolmogorov/Region, Sampler, Bayes, Monad)?
  * Footnotes (Giry, etc)
