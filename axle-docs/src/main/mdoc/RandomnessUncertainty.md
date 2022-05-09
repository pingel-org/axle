# Randomness and Uncertainty

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
