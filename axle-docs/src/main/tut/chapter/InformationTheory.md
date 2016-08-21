---
layout: page
title: Information Theory
permalink: /chapter/information_theory/
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

```tut:book
string(H(die(6)))

string(entropy(coin(Rational(7, 10))))

string(H(coin()))
```

See also the [Coin Entropy](/chapter/entropy_biased_coins/) example.
