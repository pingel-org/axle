
Two Dice
========

This page describes two ways of calculation the sum of two dice rolls.

Simulation
----------

Imports

```scala
import spire.math._
import spire.algebra._
import axle._
import axle.stats._
import axle.game.Dice._
```

Simulate 10k rolls of two dice

```scala
scala> val d6a = die(6)
d6a: axle.stats.Distribution0[Int,spire.math.Rational] = ConditionalProbabilityTable0(Map(5 -> 1/6, 1 -> 1/6, 6 -> 1/6, 2 -> 1/6, 3 -> 1/6, 4 -> 1/6),d6)

scala> val d6b = die(6)
d6b: axle.stats.Distribution0[Int,spire.math.Rational] = ConditionalProbabilityTable0(Map(5 -> 1/6, 1 -> 1/6, 6 -> 1/6, 2 -> 1/6, 3 -> 1/6, 4 -> 1/6),d6)

scala> import spire.implicits.IntAlgebra
import spire.implicits.IntAlgebra

scala> val histogram =
     |   (0 until 10000)
histogram: scala.collection.immutable.Range = Range(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, ...

scala>   .map(i => d6a.observe + d6b.observe)
<console>:2: error: illegal start of definition
  .map(i => d6a.observe + d6b.observe)
  ^

scala>   .tally
<console>:2: error: illegal start of definition
  .tally
  ^
```

Define visualization

```scala
scala> import axle.visualize.Color.blue
import axle.visualize.Color.blue

scala> import axle.visualize._
import axle.visualize._

scala> val chart = BarChart[Int, Int, Map[Int, Int]](
     |   histogram,
     |   xAxis = Some(0),
     |   title = Some("d6 + d6"),
     |   labelAngle = 0d *: angleDouble.degree,
     |   colors = List(blue),
     |   drawKey = false)
<console>:34: error: type mismatch;
 found   : scala.collection.immutable.Range
 required: Map[Int,Int]
Error occurred in an application involving default arguments.
         histogram,
         ^
```

Create SVG

```scala
     | import axle.web._
<console>:8: error: illegal start of simple expression
import axle.web._
^
     | svg(chart, "d6plusd6.svg")
```

![Observed d6 + d6](../images/d6plusd6.svg)

Distribution Monad

The distribution of two rolls combined can be produced with a for comprehension
and charted directly.

Imports (Note: documentation resets interpreter here)

```scala
     | import spire.math._
<console>:9: error: ')' expected but 'import' found.
import spire.math._
^
     | import spire.algebra._
<console>:9: error: ')' expected but 'import' found.
import spire.algebra._
^
     | import axle._
<console>:9: error: ')' expected but 'import' found.
import axle._
^
     | import axle.stats._
<console>:9: error: ')' expected but 'import' found.
import axle.stats._
^
     | import axle.game.Dice.die
<console>:9: error: ')' expected but 'import' found.
import axle.game.Dice.die
^
```

Create probability distribution of the addition of two 6-sided die:

```scala
     | val distribution = for {
<console>:9: error: ')' expected but 'val' found.
val distribution = for {
^
     |   a <- die(6)
<console>:9: error: ')' expected but '<-' found.
  a <- die(6)
    ^
     |   b <- die(6)
<console>:9: error: ')' expected but '<-' found.
  b <- die(6)
    ^
     | } yield a + b
<console>:9: error: ')' expected but '}' found.
} yield a + b
^
```

Define visualization

```scala
     | import axle.visualize._
<console>:9: error: ')' expected but 'import' found.
import axle.visualize._
^
     | import axle.visualize.Color.blue
<console>:9: error: ')' expected but 'import' found.
import axle.visualize.Color.blue
^
     | import spire.implicits.IntAlgebra
<console>:9: error: ')' expected but 'import' found.
import spire.implicits.IntAlgebra
^
     | 
     | val chart = BarChart[Int, Rational, Distribution0[Int, Rational]](
<console>:10: error: ')' expected but 'val' found.
val chart = BarChart[Int, Rational, Distribution0[Int, Rational]](
^
     |   distribution,
     |   xAxis = Some(Rational(0)),
     |   title = Some("d6 + d6"),
     |   labelAngle = 0d *: angleDouble.degree,
     |   colors = List(blue),
     |   drawKey = false)
<console>:12: error: not found: value BarChart
       val chart = BarChart[Int, Int, Map[Int, Int]](
                   ^
<console>:13: error: not found: value histogram
         histogram,
         ^
<console>:14: error: not found: value xAxis
         xAxis = Some(0),
         ^
<console>:15: error: not found: value title
         title = Some("d6 + d6"),
         ^
<console>:16: error: not found: value labelAngle
         labelAngle = 0d *: angleDouble.degree,
         ^
<console>:17: error: not found: value colors
         colors = List(blue),
         ^
<console>:18: error: not found: value svg
       svg(chart, "d6plusd6.svg")
       ^
<console>:21: error: not found: value xAxis
         xAxis = Some(Rational(0)),
         ^
<console>:22: error: not found: value title
         title = Some("d6 + d6"),
         ^
<console>:23: error: not found: value labelAngle
         labelAngle = 0d *: angleDouble.degree,
         ^
<console>:24: error: not found: value colors
         colors = List(blue),
         ^
<console>:25: error: not found: value drawKey
         drawKey = false)
         ^
```

Create SVG

```scala
     | import axle.web._
<console>:15: error: illegal start of simple expression
import axle.web._
^
     | svg(chart, "distributionMonad.svg")
```

![Monadic d6 + d6](../images/distributionMonad.svg)
