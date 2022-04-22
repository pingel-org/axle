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

```scala
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

```scala
arithmeticMean(List(2d, 3d, 4d, 5d))
// res0: Double = 3.5
```

Geometric mean

```scala
geometricMean[Real, List](List(1d, 5d, 25d))
// res1: Real = Inexact(
//   f = spire.math.Real$$Lambda$10789/0x000000080281e4d0@27a23a05
// )
```

Harmonic mean

```scala
harmonicMean(List(2d, 3d, 4d, 5d))
// res2: Double = 3.116883116883117
```

## Generalized Mean

See the wikipedia page on [Generalized Mean](https://en.wikipedia.org/wiki/Generalized_mean).

When the parameter `p` is 1, it is the arithmetic mean.

```scala
generalizedMean[Double, List](1d, List(2d, 3d, 4d, 5d))
// res3: Double = 3.5
```

As `p` approaches 0, it is the geometric mean.

```scala
generalizedMean[Double, List](0.0001, List(1d, 5d, 25d))
// res4: Double = 5.00043173370165
```

At -1 it is the harmonic mean.

```scala
generalizedMean[Double, List](-1d, List(2d, 3d, 4d, 5d))
// res5: Double = 3.116883116883117
```

## Moving means

```scala
import spire.math._
```

Moving arithmetic mean

```scala
movingArithmeticMean[List, Int, Double](
    (1 to 100).toList.map(_.toDouble),
    5)
// res6: List[Double] = List(
//   3.0,
//   4.0,
//   5.0,
//   6.0,
//   7.0,
//   8.0,
//   9.0,
//   10.0,
//   11.0,
//   12.0,
//   13.0,
//   14.0,
//   15.0,
//   16.0,
//   17.0,
//   18.0,
//   19.0,
//   20.0,
//   21.0,
//   22.0,
//   23.0,
//   24.0,
//   25.0,
//   26.0,
//   27.0,
//   28.0,
//   29.0,
//   30.0,
//   31.0,
//   32.0,
//   33.0,
//   34.0,
//   35.0,
//   36.0,
//   37.0,
//   38.0,
//   39.0,
//   40.0,
//   41.0,
//   42.0,
//   43.0,
//   44.0,
//   45.0,
//   46.0,
//   47.0,
//   48.0,
//   49.0,
//   50.0,
// ...
```

Moving geometric mean

```scala
movingGeometricMean[List, Int, Real](
    List(1d, 5d, 25d, 125d, 625d),
    3)
// res7: List[Real] = List(
//   Inexact(f = spire.math.Real$$Lambda$10789/0x000000080281e4d0@5a52a5d1),
//   Inexact(f = spire.math.Real$$Lambda$9897/0x00000008025cdb10@1ea5f646),
//   Inexact(f = spire.math.Real$$Lambda$9897/0x00000008025cdb10@3b9228ce)
// )
```

Moving harmonic mean

```scala
movingHarmonicMean[List, Int, Real](
    (1 to 5).toList.map(v => Real(v)),
    3)
// res8: List[Real] = List(
//   Exact(n = 18/11),
//   Exact(n = 36/13),
//   Exact(n = 180/47)
// )
```
