---
layout: page
title: Information Theory
permalink: /tutorial/information_theory/
---

Entropy
-------

The calculation of the entropy of a distribution is available as a function called `entropy`
as well as the traditional `H`:

Imports and implicits

```tut:book:silent
import axle._
import axle.stats._
import spire.math._
import spire.algebra._
import axle.quanta.Information
import spire.implicits.DoubleAlgebra
import axle.algebra.modules.doubleRationalModule
import axle.jung.directedGraphJung
import edu.uci.ics.jung.graph.DirectedSparseGraph
import axle.quanta.UnitOfMeasurement
import axle.game.Dice.die

implicit val informationConverter = Information.converterGraphK2[Double, DirectedSparseGraph]
```

Usage

Entropy of fair 6-sided die

```tut:book
val d6 = die(6)

string(H(d6))
```

Entropy of fair and biased coins

```tut:book
val fairCoin = coin()

string(H(fairCoin))

val biasedCoin = coin(Rational(7, 10))

string(entropy(biasedCoin))
```

See also the [Coin Entropy](/tutorial/entropy_biased_coin/) example.
