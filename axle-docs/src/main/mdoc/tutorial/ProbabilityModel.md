---
layout: page
title: Probability Model
permalink: /tutorial/probability_model/
---

Modeling probability, randomness, and uncertainly is one of the primary objectives of Axle.

The capabilies are available via four typeclasses and one trait

* Perceivable
* Region (trait modeling Sigma Algebra)
* Kolmogorov
* Bayes
* Monad (`cats.Monad`)

## Imports

Preamble to pull in the commonly-used functions in this document:

```scala mdoc:silent
import cats.implicits._
import cats.effect._
import spire.math._
import axle.probability._
import axle.algebra._
import axle.visualize._
import axle.web._
```

## Creating Probability Models

There are a few type of probability models in Axle.
The simplest is the `ConditionalProbabilityTable`, which is used throughout this document.

`axle.data.Coin.flipModel` demonstrates a very simple probability model for type `Symbol`.

This is its implementation:

```scala mdoc:silent
val head = Symbol("HEAD")
val tail = SYMBOL("TAIL")

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

``` scala mdoc:silent
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
d6vis.svg[IO]("distributionMonad.svg").unsafeRunSync()
```

![d6](/tutorial/images/d6.svg)

## Sampler

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

Note that it must be provided with a Spire `Generator`.
It also requires context bounds on the value type `V` that give the method
the ability to produces values with a distribution conforming to the probability model.

```scala mdoc
(1 to 10) map { _ => fairCoin.sample(rng) }

(1 to 10) map { _ => biasedCoin.sample(rng) }

(1 to 10) map { _ => d6.sample(rng) }
```

Simulate 1k rolls of one d6

```scala mdoc
val rolls = (0 until 1000) map { _ => d6.sample(rng) }

implicit val ringInt: CRing[Int] = spire.implicits.IntAlgebra

import axle.syntax.talliable._

val oneKd6Histogram = rolls.tally
```


```scala mdoc
val d6oneKvis = BarChart[Int, Int, Map[Int, Int], String](
  () => oneKd6Histogram,
  colorOf = _ => Color.blue,
  xAxis = Some(0),
  title = Some("1k d6 samples"),
  labelAngle = Some(0d *: angleDouble.degree),
  drawKey = false)
```

Create SVG

```scala mdoc
d6oneKvis.svg[IO]("d6-1Ksamples.svg").unsafeRunSync()
```

![1k d6 samples](/tutorial/images/d6-1Ksamples.svg)

## Sigma Algebra Regions

The sealed `Region[A]` trait is extended by the following case classes
that form a way to describe expressions on the event-space of a probability model.
In Measure Theory, these expressions are said to form a "sigma-algebra" ("σ-algebra")

In order of arity, they case classes extending this trait are:

### Arity 0

* `RegionEmpty` never matches any events or probability mass
* `RegionAll` always matches all events and probability mass

### Arity 1 (not including typeclass witnesses)

* `RegionEq` matches when an event is equal to the supplied object, with respect to the supplied `cats.kernel.Eq[A]` witness.
* `RegionIf` matches when the supplied condition function returns `true`
* `RegionSet` matches when an event is contained within the supplied `Set[A]`.
* `RegionNegate` negates the supplied region.
* `RegionGTE` is true when an event is greater than or equal to the supplied object, with respect to the supplied `cats.kernel.Order[A]`
* `RegionLTE` is true when an event is less than or equal to the supplied object, with respect to the supplied `cats.kernel.Order[A]`

### Arity 2

* `RegionAnd` is the conjunction of both arguments.  It can be created using the `and` method in the `Region` trait.
* `RegionOr` is the disjunction of both arguments.  It can be created using the `or` method in the `Region` trait.

Note that a "Random Variable" does not appear in this discussion.
The `axle.probability.Variable` class does define a `is` method that returns a `RegionEq`,
but the probability models themselves are not concerned with the notion of a
`Variable`.
They are simply models over regions of events on their single, opaque type
that adhere to the laws of probability.

The eventual formalization of `Region` should connect it with a ∑ Algebra from Meaasure Theory.

## Kolmogorov for querying Probability Models

### probabilityOf (aka "P")

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

### Kolmogorov's Axioms

The single `probabilityOf` method together with the `Region` trait
is enough to define Kolmogorov's Axioms of Probability.
The axioms are implemented in `axle.laws.KolmogorovProbabilityAxioms` and
checked during testing with ScalaCheck.

#### Basic Measure

Probabilities are non-negative (for all satisfiable `Region`)

```scala
model.P(region) >= Field[V].zero
```

#### Unit Measure

The sum the probabilities of all possible events is `one`

```scala
model.P(RegionAll()) === Field[V].one
```

#### Combination

For disjoint event regions, `e1` and `e2`, the probability of their disjunction `e1 or e2`
is equal to the sum of their independent probabilities.

```scala
(!((e1 and e2) === RegionEmpty() )) || (model.P(e1 or e2) === model.P(e1) + model.P(e2))
```

## Bayes Theorem, Conditioning, and Filtering

The `Bayes` typeclass implements the conditioning of a probability model
via the `filter` (`|` is also an alias).

```scala
def filter(predicate: Region[A])(implicit fieldV: Field[V]): M[A, V]
```

`filter` -- along with `probabilityOf` from `Kolomogorov` -- allows Bayes' Theorem
to be expressed and checked with ScalaCheck.

```scala
model.filter(b).P(a) * model.P(b) === model.filter(a).P(b) * model.P(a)
```

For non-zero `model.P(a)` and `model.P(b)`

The theorem is more recognizable as `P(A|B) = P(B|A) * P(A) / P(B)`

## Probability Model as Monads

The `pure`, `map`, and `flatMap` methods of `cats.Monad` are defined
for `ConditionalProbabilityTable`, `TallyDistribution`.

For some historical reading on the origins of probability monads,
see the literature on the Giry Monad.

### Iffy

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

### Chaining models

Chain two events' models

```scala mdoc
val bothCoinsModel = (fairCoin: CPTR[Symbol]).flatMap({ flip1 =>
  (fairCoin: CPTR[Symbol]).map({ flip2 => (flip1, flip2)})
})
```

This creates a model on events of type `(Symbol, Symbol)`

It can be queried with `P` using `RegionIf` to check fields within the `Tuple2`.

```scala mdoc
type TWOFLIPS = (Symbol, Symbol)

bothCoinsModel.P(RegionIf[TWOFLIPS](_._1 == head) and RegionIf[TWOFLIPS](_._2 == head))

bothCoinsModel.P(RegionIf[TWOFLIPS](_._1 == head) or RegionIf[TWOFLIPS](_._2 == head))
```

### Two Dice

For more examples of combining rolls of six-sided dice, see [Two Dice](/tutorial/two_dice/).

## Future work

### Measure Theory

Further refining and extending Axle to incorporate Measure Theory is a likely follow-on step.

### Markov Categories

As an alternative to Measure Theory, see Tobias Fritz's work on Markov Categories

### Probabilistic and Differentiable Programming

In general, the explosion of work on probabilistic and differentible programming is fertile ground for Axle's lawful approach.
