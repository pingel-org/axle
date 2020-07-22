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
import axle.syntax.probabilitymodel._

implicit val prob = ProbabilityModel[ConditionalProbabilityTable]
```

## Monadic `map` to operate on the event space

```scala mdoc
val d6utf = die(6).map(numberToUtfFace)
```

Chain two rolls together

```scala mdoc
val bothDieModel = d6utf.chain(d6utf)
```

Then query the resulting probability model's distribution of 2-roll events.

```scala mdoc
import axle.algebra._ // for Region*
import axle.showSymbol

bothDieModel.P(RegionEqTuple1of2('⚃) and RegionEqTuple2of2('⚃))

bothDieModel.P(RegionNegate(RegionEqTuple1of2('⚃)))
```

Observe rolls of a die

```scala mdoc
import spire.random.Generator.rng

implicit val dist = axle.stats.rationalProbabilityDist

(1 to 10) map { i => d6utf.observe(rng) }
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
val d6 = die(6)
val rolls = (0 until 1000) map { i => d6.observe(gen) + d6.observe(gen) }

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

## Direct computation of the sum of two dice

The distribution of two rolls combined can be computed directly

Imports (Note: documentation resets interpreter here)

```scala mdoc:silent:reset
import spire.math._

import axle.syntax.probabilitymodel._
import axle.stats._
import axle.game.Dice.die
```

## Monadic `flatMap`

Create probability distribution of the addition of two 6-sided die:

```scala mdoc
implicit val prob = ProbabilityModel[ConditionalProbabilityTable]

implicit val intEq: cats.kernel.Eq[Int] = spire.implicits.IntAlgebra

val twoDiceSummed = die(6).flatMap { a =>
  die(6).map { b => a + b }
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
