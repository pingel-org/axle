---
layout: page
title: Information Theory
permalink: /tutorial/information_theory/
---

## Entropy

The calculation of the entropy of a distribution is available as a function called `entropy`
as well as the traditional `H`:

Imports and implicits

```scala mdoc:silent
import edu.uci.ics.jung.graph.DirectedSparseGraph

import cats.implicits._

import spire.math._
import spire.algebra._

import axle._
import axle.stats._
import axle.quanta.Information
import axle.jung.directedGraphJung
import axle.game.Dice.die

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra

implicit val informationConverter = Information.converterGraphK2[Double, DirectedSparseGraph]

implicit val monad = {
  implicit val fieldRational = spire.algebra.Field[Rational]
  ProbabilityModel.monad[({ type λ[T] = ConditionalProbabilityTable0[T, Rational] })#λ, Rational]
}
implicit val prob = {
  implicit val fieldRational = spire.algebra.Field[Rational]
  implicitly[ProbabilityModel[({ type λ[T] = ConditionalProbabilityTable0[T, Rational] })#λ, Rational]]
}
```

Usage

Entropy of fair 6-sided die

```scala mdoc
val d6 = die(6)

H[({ type λ[T] = ConditionalProbabilityTable0[T, Rational] })#λ, Int, Rational](d6).show
```

Entropy of fair and biased coins

```scala mdoc
val fairCoin = coin()

H[({ type λ[T] = ConditionalProbabilityTable0[T, Rational] })#λ, Symbol, Rational](fairCoin).show

val biasedCoin = coin(Rational(7, 10))

entropy[({ type λ[T] = ConditionalProbabilityTable0[T, Rational] })#λ, Symbol, Rational](biasedCoin).show
```

See also the [Coin Entropy](/tutorial/entropy_biased_coin/) example.
