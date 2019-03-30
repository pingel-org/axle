---
layout: page
title: Statistics
permalink: /tutorial/statistics/
---

Topics include: Random Variables, Distributions, Probability, and Standard Deviation.

## Uniform Distribution

Imports

```scala mdoc:silent
import spire.math._
import spire.algebra._

import axle._
import axle.stats._

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
```

Example

```scala mdoc
val X = uniformDistribution(List(2d, 4d, 4d, 4d, 5d, 5d, 7d, 9d), Variable("X"))
```

## Standard Deviation

Example

```scala mdoc
implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra

standardDeviation(X)
```

## Random Variables

Example fair and biased coins:

```scala mdoc
val fairCoin = coin()

val biasedCoin = coin(Rational(9, 10))
```

The `observe` method selects a value for the random variable based on the distribution.

```scala mdoc
import spire.random.Generator.rng
import axle.syntax.probabilitymodel._

implicit val dist = axle.stats.rationalProbabilityDist

(1 to 10) map { i => fairCoin.observe(rng) }

(1 to 10) map { i => biasedCoin.observe(rng) }
```

Create and query distributions

```scala mdoc
val flip1 = coin()

flip1.P('HEAD)

val flip2 = coin()

import cats.syntax.all._
type F[T] = ConditionalProbabilityTable[T, Rational]

val bothFlips = for {
  a <- flip1 : F[Symbol]
  b <- flip2 : F[Symbol]
} yield (a, b)

bothFlips.P(('HEAD, 'HEAD))

bothFlips.P({ flips: (Symbol, Symbol) => (flips._1 === 'HEAD) || (flips._2 === 'HEAD) })
```

## Dice examples

Setup

```scala mdoc
import axle.game.Dice._

val d6a = (die(6) : F[Int]).map(numberToUtfFace)
val d6b = (die(6) : F[Int]).map(numberToUtfFace)

val bothDie = for {
  a <- d6a : F[Symbol]
  b <- d6b : F[Symbol]
} yield (a, b)

```

Create and query distributions

```scala mdoc
bothDie.P({ dice: (Symbol, Symbol) => (dice._1 === '⚃) && (dice._2 === '⚃) })

bothDie.P({ dice: (Symbol, _) => (dice._1 =!= '⚃) })
```

Observe rolls of a die

```scala mdoc
(1 to 10) map { i => d6a.observe(rng) }
```

See also [Two Dice](/tutorial/two_dice/) examples.
