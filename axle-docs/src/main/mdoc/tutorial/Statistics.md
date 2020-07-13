---
layout: page
title: Statistics
permalink: /tutorial/statistics/
---

Topics include: Random Variables, Distributions, Probability, and Standard Deviation.

## Uniform Distribution

Example

```scala mdoc
import cats.implicits._
import spire.algebra._
import axle.stats._

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra

val X = uniformDistribution(List(2d, 4d, 4d, 4d, 5d, 5d, 7d, 9d))
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
import spire.math._

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
import axle.algebra._

fairCoin.P(RegionEq('HEAD))
```

Chain two events' distributions

```scala mdoc
implicit val prob = ProbabilityModel[ConditionalProbabilityTable]

val bothCoinsModel = prob.chain(fairCoin)(fairCoin)

bothCoinsModel.P(RegionEqTuple1of2('HEAD) and RegionEqTuple2of2('HEAD))

bothCoinsModel.P(RegionEqTuple1of2('HEAD) or RegionEqTuple2of2('HEAD))
```

## Dice examples

Setup

```scala mdoc
import axle.game.Dice._

val d6 = prob.map(die(6))(numberToUtfFace)

val bothDieModel = prob.chain(d6)(d6)
```

Create and query distributions

```scala mdoc
bothDieModel.P(RegionEqTuple1of2('⚃) and RegionEqTuple2of2('⚃))

bothDieModel.P(RegionNegate(RegionEqTuple1of2('⚃)))
```

Observe rolls of a die

```scala mdoc
(1 to 10) map { i => d6.observe(rng) }
```

See also [Two Dice](/tutorial/two_dice/) examples.
