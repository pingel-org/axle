---
layout: page
title: Pythagorean Means
permalink: /tutorial/pythagorean_means/
---

Arithmetic, Geometric, and Harmonic Means are all 'Pythagorean'.

See the wikipedia page on [Pythagorean Means](https://en.wikipedia.org/wiki/Pythagorean_means)
for more.

Arithmetic, Geometric, and Harmonic Mean Examples
-------------------------------------------------

Imports

```tut:silent
import axle._
import axle.algebra._
```

Examples

Arithmetic mean

```tut:book
import spire.implicits.DoubleAlgebra

arithmeticMean(List(2d, 3d, 4d, 5d))
```

Geometric mean

```tut:book
import spire.math.Real

geometricMean[Real, List[Real]](List(1d, 5d, 25d))
```

Harmonic mean

```tut:book
harmonicMean(List(2d, 3d, 4d, 5d))
```

Generalized Mean
----------------

See the wikipedia page on [Generalized Mean](https://en.wikipedia.org/wiki/Generalized_mean).

When the parameter `p` is 1, it is the arithmetic mean.

```tut:book
generalizedMean[Double, List[Double]](1d, List(2d, 3d, 4d, 5d))
```

As `p` approaches 0, it is the geometric mean.

```tut:book
generalizedMean[Double, List[Double]](0.0001, List(1d, 5d, 25d))
```

At -1 it is the harmonic mean.

```tut:book
generalizedMean[Double, List[Double]](-1d, List(2d, 3d, 4d, 5d))
```

Moving means
------------

```tut:silent
import spire.math._
```

Moving arithmetic mean

```tut:book
val xs = (1 to 100).toList.map(_.toDouble)

val window = 5

val moved = movingArithmeticMean[List[Double], Int, Double, List[(Double, Double)]](xs, window)
```

Moving geometric mean

```tut:book
val xs: List[Real] = List(1d, 5d, 25d, 125d, 625d)

val window = 3

val moved = movingGeometricMean[List[Real], Int, Real, List[(Real, Real)]](xs, window)
```

Moving harmonic mean

```tut:book
val xs: List[Real] = (1 to 5).toList.map(v => Real(v))

val window = 3

val moved = movingHarmonicMean[List[Real], Int, Real, List[(Real, Real)]](xs, window)
```
