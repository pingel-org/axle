
Statistics
==========

Topics include: Random Variables, Distributions, Probability, and Standard Deviation.

Uniform Distribution
--------------------

Imports

```scala
scala> import axle._
import axle._

scala> import axle.stats._
import axle.stats._

scala> import spire.math._
import spire.math._

scala> import spire.algebra._
import spire.algebra._
```

Example

```scala
scala> val dist = uniformDistribution(List(2d, 4d, 4d, 4d, 5d, 5d, 7d, 9d), "some doubles")
dist: axle.stats.Distribution0[Double,spire.math.Rational] = ConditionalProbabilityTable0(Map(5.0 -> 1/4, 9.0 -> 1/8, 2.0 -> 1/8, 7.0 -> 1/8, 4.0 -> 3/8),some doubles)
```

Standard Deviation
------------------

Example

```scala
scala> import spire.implicits.DoubleAlgebra
import spire.implicits.DoubleAlgebra

scala> standardDeviation(dist)
res0: Double = 2.0
```

Random Variables
----------------

Example fiar and biased coins:

```scala
scala> val fairCoin = coin()
fairCoin: axle.stats.Distribution[Symbol,spire.math.Rational] = ConditionalProbabilityTable0(Map('HEAD -> 1/2, 'TAIL -> 1/2),coin)

scala> val biasedCoin = coin(Rational(9, 10))
biasedCoin: axle.stats.Distribution[Symbol,spire.math.Rational] = ConditionalProbabilityTable0(Map('HEAD -> 9/10, 'TAIL -> 1/10),coin)
```

The `observe` method selects a value for the random variable based on the distribution.

```scala
scala> (1 to 10) map { i => fairCoin.observe }
res1: scala.collection.immutable.IndexedSeq[Symbol] = Vector('HEAD, 'HEAD, 'TAIL, 'TAIL, 'TAIL, 'TAIL, 'TAIL, 'TAIL, 'TAIL, 'HEAD)

scala> (1 to 10) map { i => biasedCoin.observe }
res2: scala.collection.immutable.IndexedSeq[Symbol] = Vector('HEAD, 'HEAD, 'HEAD, 'HEAD, 'HEAD, 'HEAD, 'HEAD, 'HEAD, 'HEAD, 'HEAD)
```

Create and query distributions

```scala
scala> val flip1 = coin()
flip1: axle.stats.Distribution[Symbol,spire.math.Rational] = ConditionalProbabilityTable0(Map('HEAD -> 1/2, 'TAIL -> 1/2),coin)

scala> val flip2 = coin()
flip2: axle.stats.Distribution[Symbol,spire.math.Rational] = ConditionalProbabilityTable0(Map('HEAD -> 1/2, 'TAIL -> 1/2),coin)

scala> P(flip1 is 'HEAD).apply()
res3: spire.math.Rational = 1/2

scala> P((flip1 is 'HEAD) and (flip2 is 'HEAD)).apply()
res4: spire.math.Rational = 1/4

scala> P((flip1 is 'HEAD) or (flip2 is 'HEAD)).apply()
res5: spire.math.Rational = 3/4

scala> P((flip1 is 'HEAD) | (flip2 is 'TAIL)).apply()
res6: spire.math.Rational = 1/2
```

Dice examples

Setup

```scala
scala> import axle.game.Dice._
import axle.game.Dice._

scala> val d6a = utfD6
d6a: axle.stats.Distribution0[Symbol,spire.math.Rational] = ConditionalProbabilityTable0(Map('⚄ -> 1/6, '⚅ -> 1/6, '⚁ -> 1/6, '⚂ -> 1/6, '⚀ -> 1/6, '⚃ -> 1/6),UTF d6)

scala> val d6b = utfD6
d6b: axle.stats.Distribution0[Symbol,spire.math.Rational] = ConditionalProbabilityTable0(Map('⚄ -> 1/6, '⚅ -> 1/6, '⚁ -> 1/6, '⚂ -> 1/6, '⚀ -> 1/6, '⚃ -> 1/6),UTF d6)
```

Create and query distributions

```scala
scala> P((d6a is '⚃) and (d6b is '⚃)).apply()
res7: spire.math.Rational = 1/36

scala> P((d6a isnt '⚃)).apply()
res8: spire.math.Rational = 5/6
```

Observe rolls of a die

```
(1 to 10) map { i => utfD6.observe }
```

See also <a href="TwoDice.md">Two Dice</a> examples.
