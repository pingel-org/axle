---
layout: page
title: Two Dice
permalink: /tutorial/two_dice/
---

This page describes two ways of calculation the sum of two dice rolls.

Simulation
----------

Imports

```tut:silent
import cats.implicits._

import spire.math._
import spire.algebra._

import axle._
import axle.stats._
import axle.game.Dice._
```

Simulate 10k rolls of two dice

```tut:book
val d6a = die(6)
val d6b = die(6)

import spire.implicits.IntAlgebra

val histogram =
  (0 until 10000).
  map(i => d6a.observe + d6b.observe).
  tally
```

Define visualization

```tut:silent
import axle.visualize.Color.blue
import axle.visualize._
```

```tut:book
val chart = BarChart[Int, Int, Map[Int, Int], String](
  () => histogram,
  colorOf = _ => Color.blue,
  xAxis = Some(0),
  title = Some("d6 + d6"),
  labelAngle = Some(0d *: angleDouble.degree),
  drawKey = false)
```

Create SVG

```tut:book
import axle.web._
svg(chart, "d6plusd6.svg")
```

![Observed d6 + d6](/tutorial/images/d6plusd6.svg)

Distribution Monad
------------------

The distribution of two rolls combined can be produced with a for comprehension
and charted directly.

Imports (Note: documentation resets interpreter here)

```tut:silent:reset
import spire.math._
import spire.algebra._

import cats.implicits._

import axle._
import axle.stats._
import axle.game.Dice.die
```

Create probability distribution of the addition of two 6-sided die:

```tut:book
val distribution = for {
  a <- die(6)
  b <- die(6)
} yield a + b
```

Define visualization

```tut:silent
import axle.visualize._
import axle.visualize.Color.blue
import spire.implicits.IntAlgebra
```

```tut:book
val chart = BarChart[Int, Rational, Distribution0[Int, Rational], String](
  () => distribution,
  colorOf = _ => Color.blue,
  xAxis = Some(Rational(0)),
  title = Some("d6 + d6"),
  labelAngle = Some(0d *: angleDouble.degree),
  drawKey = false)
```

Create SVG

```tut:book
import axle.web._
svg(chart, "distributionMonad.svg")
```

![Monadic d6 + d6](/tutorial/images/distributionMonad.svg)
