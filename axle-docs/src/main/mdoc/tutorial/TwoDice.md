---
layout: page
title: Two Dice
permalink: /tutorial/two_dice/
---

This page describes two ways of calculation the sum of two dice rolls.

## Simulation

Imports

```scala mdoc:silent
import cats.implicits._

import spire.algebra._
import spire.math.Rational

import axle.enrichGenSeq
import axle.game.Dice.die
import axle.stats._
import axle.syntax.probabilitymodel._
```

Simulate 10k rolls of two dice

```scala mdoc
implicit val dist = axle.stats.rationalProbabilityDist

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

## Distribution Monad

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
