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

import spire.math._
import spire.algebra._

import axle._
import axle.stats._
import axle.game.Dice._
```

Simulate 10k rolls of two dice

```scala mdoc
val d6a = die(6)
val d6b = die(6)

implicit val ringInt: Ring[Int] = spire.implicits.IntAlgebra

val histogram =
  (0 until 10000).
  map(i => d6a.observe + d6b.observe).
  tally
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
import axle.web._
svg(chart, "d6plusd6.svg")
```

![Observed d6 + d6](/tutorial/images/d6plusd6.svg)

## Distribution Monad

The distribution of two rolls combined can be produced with a for comprehension
and charted directly.

Imports (Note: documentation resets interpreter here)

```scala mdoc:silent:reset
import spire.math._
import cats.implicits._
import axle.game.Dice.die
```

Create probability distribution of the addition of two 6-sided die:

```scala mdoc
val distribution = for {
  a <- die(6)
  b <- die(6)
} yield a + b
```

Define visualization

```scala mdoc:silent
import axle.visualize._
```

```scala mdoc
val chart = BarChart[Int, Rational, Distribution0[Int, Rational], String](
  () => distribution,
  colorOf = _ => Color.blue,
  xAxis = Some(Rational(0)),
  title = Some("d6 + d6"),
  labelAngle = Some(0d *: angleDouble.degree),
  drawKey = false)
```

Create SVG

```scala mdoc
import axle.web._
svg(chart, "distributionMonad.svg")
```

![Monadic d6 + d6](/tutorial/images/distributionMonad.svg)
