---
layout: page
title: Probability Model
permalink: /tutorial/probability_model/
---

Modeling probability and uncertainly is one of the primary objectives of Axle.

The capabilies are available via four typeclasses and a trait

* Perceivable
* Region (modeling Sigma Algebra)
* Kolmogorov
* Bayes
* Monad (`cats.Monad`)

## Creating Probability Models

`axle.data.Coin.flipModel` demonstrates a very simple probability model for type `Symbol`.

This is its implementation:

```scala
val head = Symbol("HEAD")
val tail = SYMBOL("TAIL")

def flipModel(pHead: Rational = Rational(1, 2)): ConditionalProbabilityTable[Symbol, Rational] =
  ConditionalProbabilityTable[Symbol, Rational](
    Map(
      head -> pHead,
      tail -> (1 - pHead)))
```

For example, it cal be called with or without a "bias" for the "head" side.
Otherwise it is assumed to be a fair coin.

```scala mdoc
import axle.probability._
import spire.math._
import axle.data.Coin

val fairCoin = Coin.flipModel()

val biasedCoin = Coin.flipModel(Rational(9, 10))
```

## Perceivable.perceive

The `percieve` method's signature looks like:

```scala
def perceive(gen: Generator)(implicit spireDist: Dist[V], ringV: Ring[V], orderV: Order[V]): A
```

The `perceive` method selects a specific value within the model's domain type.

```scala mdoc
import spire.random.Generator.rng
import axle.syntax.perceivable._

implicit val dist = axle.probability.rationalProbabilityDist

(1 to 10) map { i => fairCoin.perceive(rng) }

(1 to 10) map { i => biasedCoin.perceive(rng) }
```

## Sigma Algebra Regions

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
The `axle.probability.Variable` class does define a `is` method that returns a `RegionEq`,
but the probability models themselves are not concerned with the notion of a
`Variable`.
They are simply models over regions of events on their single, opaque type
that adhere to the laws of probability.

The eventual formalization of `Region` should connect it with a ∑ Algebra from Meaasure Theory.

## Kolmogorov -- for Querying Probability Models

### probabilityOf (aka "P")

The method signature for `P` is defined in terms of a `Region`.

`P` is a synonym for `probabilityOf`.

```scala
def probabilityOf(predicate: Region[A])(implicit fieldV: Field[V]): V
```

Compute the odds of a `head` for a single toss of a fair coin

```scala mdoc
import cats.implicits._
import axle.algebra._
import axle.syntax.kolmogorov._
import Coin.head

fairCoin.P(RegionEq(head))
```

### Kolmogorov's Axioms

The single `probabilityOf` method is enough to define Kolmogorov's Axioms of Probability.
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

`filter` allows Bayes Theorem to be expressed and checked with ScalaCheck.

```scala
model.filter(b).P(a) * model.P(b) === model.filter(a).P(b) * model.P(a)
```

For non-zero `model.P(a)` and `model.P(b)`

The theorem is more recognizable as `P(A|B) = P(B|A) * P(A) / P(B)`

## Probability Model as Monads

The `pure`, `map`, and `flatMap` methods of `cats.Monad` are defined
for `ConditionalProbabilityTable`, `TallyDistribution`.

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

### Uses of probability models as monads

#### Chaining models

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

## Future work

### Measure Theory

Further refining and extending Axle to incorporate Measure Theory is a likely follow-on step.

### Markov Categories

As an alternative to Measure Theory, see Tobias Fritz's work on Markov Categories

### Probabilistic and Differentiable Programming

In general, the explosion of work on probabilistic and differentible programming is fertile ground for Axle's lawful approach.
