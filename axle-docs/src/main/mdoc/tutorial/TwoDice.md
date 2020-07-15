---
layout: page
title: Two Dice
permalink: /tutorial/two_dice/
---

Setup

```scala mdoc
import axle.eqSymbol
import axle.stats._
import axle.game.Dice._


implicit val prob = ProbabilityModel[ConditionalProbabilityTable]

val d6 = prob.map(die(6))(numberToUtfFace)

val bothDieModel = prob.chain(d6)(d6)
```

Create and query distributions

```scala mdoc
import axle.algebra._ // for Region*
import axle.showSymbol
import axle.syntax.probabilitymodel._

bothDieModel.P(RegionEqTuple1of2('⚃) and RegionEqTuple2of2('⚃))

bothDieModel.P(RegionNegate(RegionEqTuple1of2('⚃)))
```

Observe rolls of a die

```scala mdoc
import spire.random.Generator.rng

implicit val dist = axle.stats.rationalProbabilityDist

(1 to 10) map { i => d6.observe(rng) }
```

## Simulate the sum of two dice

Imports

```scala mdoc:silent
import cats.implicits._

import spire.algebra._
import spire.math.Rational

import axle.enrichGenSeq
import axle.game.Dice.die
```

Simulate 10k rolls of two dice

```scala mdoc
val seed = spire.random.Seed(42)
val gen = spire.random.Random.generatorFromSeed(seed)
val d6a = die(6)
val d6b = die(6)
val rolls = (0 until 1000) map { i => d6a.observe(gen) + d6b.observe(gen) }

implicit val ringInt: Ring[Int] = spire.implicits.IntAlgebra

val histogram = rolls.tally
```

Define visualization

```scala mdoc:silent
import axle.visualize._
```

```scala mdoc
val chart = BarChart[Int, Int, Map[Int, Int], String](
  () => histogram,
  colorOf = _ => Color.blue,
  xAxis = Some(0),
  title = Some("d6 + d6"),
  labelAngle = Some(0d *: angleDouble.degree),
  drawKey = false)
```

Create SVG

```scala mdoc
import cats.effect._
import axle.web._

chart.svg[IO]("d6plusd6.svg").unsafeRunSync()
```

![Observed d6 + d6](/tutorial/images/d6plusd6.svg)

## Direct computation of the sum of two dice by 

The distribution of two rolls combined can be produced with a for comprehension
and charted directly.

Imports (Note: documentation resets interpreter here)

```scala mdoc:silent:reset
import spire.math._

import axle.stats._
import axle.game.Dice.die
```

Create probability distribution of the addition of two 6-sided die:

```scala mdoc
implicit val prob = ProbabilityModel[ConditionalProbabilityTable]
implicit val intEq: cats.kernel.Eq[Int] = spire.implicits.IntAlgebra

val twoDiceSummed = prob.flatMap(die(6)) { a =>
  prob.map(die(6)) { b =>
    a + b
  }
}
```

Define visualization

```scala mdoc:silent
import axle.visualize._
```

```scala mdoc
import cats.implicits._

val chart = BarChart[Int, Rational, ConditionalProbabilityTable[Int, Rational], String](
  () => twoDiceSummed,
  colorOf = _ => Color.blue,
  xAxis = Some(Rational(0)),
  title = Some("d6 + d6"),
  labelAngle = Some(0d *: angleDouble.degree),
  drawKey = false)
```

Create SVG

```scala mdoc
import cats.effect._
import axle.web._

chart.svg[IO]("distributionMonad.svg").unsafeRunSync()
```

![Monadic d6 + d6](/tutorial/images/distributionMonad.svg)
