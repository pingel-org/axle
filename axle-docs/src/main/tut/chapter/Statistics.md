---
layout: page
title: Statistics
permalink: /chapter/statistics/
---

Topics include: Random Variables, Distributions, Probability, and Standard Deviation.

Uniform Distribution
--------------------

Imports

```tut:book
import axle._
import axle.stats._
import spire.math._
import spire.algebra._
```

Example

```tut:book
val dist = uniformDistribution(List(2d, 4d, 4d, 4d, 5d, 5d, 7d, 9d), "some doubles")
```

Standard Deviation
------------------

Example

```tut:book
import spire.implicits.DoubleAlgebra

standardDeviation(dist)
```

Random Variables
----------------

Example fiar and biased coins:

```tut:book
val fairCoin = coin()

val biasedCoin = coin(Rational(9, 10))
```

The `observe` method selects a value for the random variable based on the distribution.

```tut:book
(1 to 10) map { i => fairCoin.observe }
(1 to 10) map { i => biasedCoin.observe }
```

Create and query distributions

```tut:book
val flip1 = coin()
val flip2 = coin()

P(flip1 is 'HEAD).apply()

P((flip1 is 'HEAD) and (flip2 is 'HEAD)).apply()

P((flip1 is 'HEAD) or (flip2 is 'HEAD)).apply()

P((flip1 is 'HEAD) | (flip2 is 'TAIL)).apply()
```

Dice examples

Setup

```tut:book
import axle.game.Dice._

val d6a = utfD6
val d6b = utfD6
```

Create and query distributions

```tut:book
P((d6a is '⚃) and (d6b is '⚃)).apply()

P((d6a isnt '⚃)).apply()
```

Observe rolls of a die

```
(1 to 10) map { i => utfD6.observe }
```

See also [Two Dice](/chapter/two_dice/) examples.
