---
layout: page
title: Two Dice
permalink: /tutorial/two_dice/
---

Combining probability models representing rolls of 6-sided dice demonstrate
some behavior of `ProbabilityModel`.

Setup

```scala mdoc
import cats.implicits._

import spire.math._

import axle.eqSymbol
import axle.probability._

import axle.syntax.kolmogorov._
import axle.syntax.bayes._
import axle.syntax.sampler._
```

## Monadic map to operate on the event space

Map the model on the integers `1 to 6` to a model of UTF
symbols representing the faces of the dice.

```scala mdoc
import axle.game.Dice._

val monad = ConditionalProbabilityTable.monadWitness[Rational]

val d6utf = monad.map(die(6))(numberToUtfFace)
```

Monadic `flatMap` constructs a model of the sequence of two rolls

```scala mdoc
val bothDieModel = monad.flatMap(d6utf) { flip1 =>
  monad.flatMap(d6utf) { flip2 =>
    (flip1, flip2)
  }
}
```

Query the resulting probability model's distribution of 2-roll events.

```scala mdoc
import axle.algebra._ // for Region*

type TWOROLLS = (Symbol, Symbol)

bothDieModel.P(RegionIf[TWOROLLS](_._1 == Symbol("⚃")) and RegionIf[TWOROLLS](_._2 == Symbol("⚃")))

bothDieModel.P(RegionNegate(RegionIf[TWOROLLS](_._1 == Symbol("⚃"))))
```

Observe rolls of a die

```scala mdoc
import spire.random.Generator.rng
implicit val dist = axle.probability.rationalProbabilityDist

(1 to 10) map { i => d6utf.sample(rng) }
```

## Simulate the sum of two dice

Compare two methods of computing distributions -- simulation and full, precise construction using monads.

The following code shows the simulation of 1,000 2-dice sums.

```scala mdoc:silent
import cats.implicits._

import spire.algebra._
import spire.math.Rational

import axle.game.Dice.die
import axle.syntax.talliable.talliableOps
```

Simulate 1k rolls of two dice

```scala mdoc
val seed = spire.random.Seed(42)
val gen = spire.random.Random.generatorFromSeed(seed)
val d6 = die(6)

val rolls = (0 until 1000) map { i => d6.sample(gen) + d6.sample(gen) }

implicit val ringInt: CRing[Int] = spire.implicits.IntAlgebra

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

## Direct computation of the sum of two dice using monads

The full distribution of two rolls combined can be computed directly and precisely
using monads.  Of course this does become infeasable as the models are combined -- 
axle will have more on that tradeoff in future versions.

## Monadic flatMap

Create probability distribution of the addition of two 6-sided die:

```scala mdoc
implicit val intEq: cats.kernel.Eq[Int] = spire.implicits.IntAlgebra

val twoDiceSummed = monad.flatMap(die(6)) { a =>
  monad.map(die(6)) { b =>
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

val monadicChart = BarChart[Int, Rational, ConditionalProbabilityTable[Int, Rational], String](
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

monadicChart.svg[IO]("distributionMonad.svg").unsafeRunSync()
```

![Monadic d6 + d6](/tutorial/images/distributionMonad.svg)
