---
layout: page
title: Pythagorean Means
permalink: /tutorial/pythagorean_means/
---

Arithmetic, Geometric, and Harmonic Means are all 'Pythagorean'.

See the wikipedia page on [Pythagorean Means](https://en.wikipedia.org/wiki/Pythagorean_means)
for more.

## Arithmetic, Geometric, and Harmonic Mean Examples

Imports

```scala mdoc:silent
import cats.implicits._

import spire.math.Real
import spire.algebra.Field
import spire.algebra.NRoot

import axle.math._

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra
```

Examples

Arithmetic mean

```scala mdoc
arithmeticMean(List(2d, 3d, 4d, 5d))
```

Geometric mean

```scala mdoc
geometricMean[Real, List](List(1d, 5d, 25d))
```

Harmonic mean

```scala mdoc
harmonicMean(List(2d, 3d, 4d, 5d))
```

## Generalized Mean

See the wikipedia page on [Generalized Mean](https://en.wikipedia.org/wiki/Generalized_mean).

When the parameter `p` is 1, it is the arithmetic mean.

```scala mdoc
generalizedMean[Double, List](1d, List(2d, 3d, 4d, 5d))
```

As `p` approaches 0, it is the geometric mean.

```scala mdoc
generalizedMean[Double, List](0.0001, List(1d, 5d, 25d))
```

At -1 it is the harmonic mean.

```scala mdoc
generalizedMean[Double, List](-1d, List(2d, 3d, 4d, 5d))
```

## Moving means

```scala mdoc:silent
import spire.math._
```

Moving arithmetic mean

```scala mdoc
movingArithmeticMean[List, Int, Double](
    (1 to 100).toList.map(_.toDouble),
    5)
```

Moving geometric mean

```scala mdoc
movingGeometricMean[List, Int, Real](
    List(1d, 5d, 25d, 125d, 625d),
    3)
```

Moving harmonic mean

```scala mdoc
movingHarmonicMean[List, Int, Real](
    (1 to 5).toList.map(v => Real(v)),
    3)
```
