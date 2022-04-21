---
layout: page
title: Information Theory
permalink: /tutorial/information_theory/
---

## Entropy

The calculation of the entropy of a distribution is available as a function called `entropy`
as well as the traditional `H`:

Imports and implicits

```scala
import edu.uci.ics.jung.graph.DirectedSparseGraph

import cats.implicits._

import spire.math._
import spire.algebra._

import axle._
import axle.probability._
import axle.stats._
import axle.quanta.Information
import axle.jung.directedGraphJung
import axle.data.Coin
import axle.game.Dice.die

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra

implicit val informationConverter = Information.converterGraphK2[Double, DirectedSparseGraph]
```

Usage

Entropy of fair 6-sided die

```scala
val d6 = die(6)
// d6: ConditionalProbabilityTable[Int, Rational] = ConditionalProbabilityTable(
//   p = HashMap(5 -> 1/6, 1 -> 1/6, 6 -> 1/6, 2 -> 1/6, 3 -> 1/6, 4 -> 1/6)
// )

H[Int, Rational](d6).show
// res0: String = "2.5849625007211565 b"
```

Entropy of fair and biased coins

```scala
val fairCoin = Coin.flipModel()
// fairCoin: ConditionalProbabilityTable[Symbol, Rational] = ConditionalProbabilityTable(
//   p = Map('HEAD -> 1/2, 'TAIL -> 1/2)
// )

H[Symbol, Rational](fairCoin).show
// res1: String = "1.0 b"

val biasedCoin = Coin.flipModel(Rational(7, 10))
// biasedCoin: ConditionalProbabilityTable[Symbol, Rational] = ConditionalProbabilityTable(
//   p = Map('HEAD -> 7/10, 'TAIL -> 3/10)
// )

entropy[Symbol, Rational](biasedCoin).show
// res2: String = "0.8812908992306927 b"
```

See also the [Coin Entropy](/tutorial/entropy_biased_coin/) example.
