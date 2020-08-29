---
layout: page
title: Probability Model
permalink: /tutorial/probability_model/
---

Probability Models are one of the core elements of Axle.
They are foundational and will continue to be the basis for further exploration.

# Creating Probability Models

The `coin` method demonstrates a very simple probability model for type `Symbol`.
This is its implementation:

```scala
def coin(pHead: Rational = Rational(1, 2)): ConditionalProbabilityTable[Symbol, Rational] =
  ConditionalProbabilityTable[Symbol, Rational](
    Map(
      'HEAD -> pHead,
      'TAIL -> (1 - pHead)))
```

A `ProbabilityModel` witness is available for the resulting `ConditionalProbabilityTable[Symbol, Rational]`

```scala mdoc
import cats.implicits._
import axle.stats._
import spire.math._

val fairCoin = coin()

val biasedCoin = coin(Rational(9, 10))
```

In the method signatures below a `ProbalityModel[M]` (`M[A, V]`) typeclass witness, `prob`, is defined for these `ConditionalProbabilityTable`s.

## Observe

The `observe` method's signature looks like:
```scala

def observe(gen: Generator)(implicit spireDist: Dist[V], ringV: Ring[V], orderV: Order[V]): A
```

The `observe` method selects a specific value within the model's domain type.

```scala mdoc
import spire.random.Generator.rng
import axle.syntax.probabilitymodel._

implicit val dist = axle.stats.rationalProbabilityDist

(1 to 10) map { i => fairCoin.observe(rng) }

(1 to 10) map { i => biasedCoin.observe(rng) }
```

# Querying Probability Models

## `Region`

The sealed `Region[A]` trait is extended by the following case classes
that form a way to describe expressions on the event-space of a probability model.
In order of arity, they are:

### Arity 0

* `RegionEmpty` never matches any events or probability mass
* `RegionAll` always matches all events and probability mass

### Arity 1 (not including typeclass witnesses)

* `RegionEq` matches when an event is equal to the supplied object, with respect to the supplied `cats.kernel.Eq[A]` witness.
* `RegionLambda` matches when the supplied condition function returns `true`
* `RegionSet` matches when an event is contained within the supplied `Set[A]`.
* `RegionNegate` negates the supplied region.
* `RegionGTE` is true when an event is greater than or equal to the supplied object, with respect to the supplied `cats.kernel.Order[A]`
* `RegionLTE` is true when an event is less than or equal to the supplied object, with respect to the supplied `cats.kernel.Order[A]`

### Arity 2

* `RegionAnd` is the conjunction of both arguments.  It can be created using the `and` method in the `Region` trait.
* `RegionOr` is the disjunction of both arguments.  It can be created using the `or` method in the `Region` trait.

Note that a "Random Variable" does not appear in this discussion.
The `axle.stats.Variable` class does define a `is` method that returns a `RegionEq`,
but the probability models themselves are not concerned with the notion of a
`Variable`.
They are simply models over regions of events on their single, opaque type
that adhere to the laws of probability.

The eventual formalization of `Region` should connect it with a ∑ Algebra from Meaasure Theory.

## `probabilityOf` (or `P`)

The method signature for `P` is defined in terms of a `Region`.

`P` is a synonym for `probabilityOf`.

```scala
def probabilityOf(predicate: Region[A])(implicit fieldV: Field[V]): V
```

Compute the odds of a `'HEAD` for a single toss of a fair coin

```scala mdoc
import axle.algebra._

fairCoin.P(RegionEq('HEAD))
```

# Kolmogorov's Axioms

The methods above are enough to define Kolmogorov's Axioms of Probablity.
These are literally implemented in `axle.stats.KolmogorovProbabilityAxioms` and
checked during testng with ScalaCheck.
The ability to show adherance to theories such as this is a tenet of Axle's design.

## Basic Measure

Probabilities are non-negative

```scala
model.P(region) >= Field[V].zero
```

## Unit Measure

The sum the probabilities of all possible events is `one`

```scala
model.P(RegionAll()) === Field[V].one
```

## Combination

For disjoint event regions, `e1` and `e2`, the probability of their disjunction `e1 or e2`
is equal to the sum of their independent probabilities.

```scala
(!((e1 and e2) === RegionEmpty() )) || (model.P(e1 or e2) === model.P(e1) + model.P(e2))
```

## Chaining models

Chain two events' models

```scala mdoc
implicit val prob = ProbabilityModel[ConditionalProbabilityTable]

val bothCoinsModel = fairCoin.flatMap({ flip1 =>
  fairCoin.map({ flip2 => (flip1, flip2)})
})
```

This creates a model on events of type `(Symbol, Symbol)`

It can be queried with `P` using `RegionIf` to check fields within the `Tuple2`.

```scala mdoc
type TWOFLIPS = (Symbol, Symbol)

bothCoinsModel.P(RegionIf[TWOFLIPS](_._1 == 'HEAD) and RegionIf[TWOFLIPS](_._2 == 'HEAD))

bothCoinsModel.P(RegionIf[TWOFLIPS](_._1 == 'HEAD) or RegionIf[TWOFLIPS](_._2 == 'HEAD))
```

# Conditioning and Bayes Theorem

Conditioning a probability model is accomplished with the `filter` method.
`|` is a synonym.

```scala
def filter(predicate: Region[A])(implicit fieldV: Field[V]): M[A, V]
```

`filter` allows Bayes Theorem to be expressed and checked with ScalaCheck.

```scala
model.filter(b).P(a) === ( model.filter(a).P(b) * model.P(a) / model.P(b))
```

Which is more recognizable as `P(A|B) = P(B|A) * P(A) / P(B)`

# Probability Model as Monads

The `unit`, `map`, and `flatMap` methods of `ProbabilityModel` allow us to think of them as `Monad`s.
(They are not (yet) literaly defined as `cats` `Monad`s, but may be at some point.)

For some historical reading on the origins of probability monads, see the literature on the
Giry Monad.

To see these in action in Axle, see the [Two Dice](/tutorial/two_dice/) examples.

A stochastic version of `if` (aka `iffy`) can be implemented in terms of `flatMap`
using this pattern:

```scala
input.flatMap { i =>
  if( predicate(i) ) {
    trueClause
  } else {
    falseClause
  }
}
```

# Future work

## Measure Theory

Further refining and extending Axle to incorporate Measure Theory is a likely follow-on step.

## Markov Categories

As an alternative to Measure Theory, see Tobias Fritz's work on Markov Categories

## Probabilistic and Differentiable Programming

In general, the explosion of work on probabilistic and differentible programming is fertile ground
for Axle's lawful approach.
