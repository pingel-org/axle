# Statistics

## Pythagorean Means

Arithmetic, Geometric, and Harmonic Means are all 'Pythagorean'.

See the wikipedia page on [Pythagorean Means](https://en.wikipedia.org/wiki/Pythagorean_means)
for more.

### Arithmetic, Geometric, and Harmonic Mean Examples

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

### Generalized Mean

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

### Moving means

```scala mdoc:silent
import spire.math._
```

Moving arithmetic mean

```scala mdoc:height=15
movingArithmeticMean[List, Int, Double](
    (1 to 100).toList.map(_.toDouble),
    5)
```

Moving geometric mean

```scala mdoc:height=15
movingGeometricMean[List, Int, Real](
    List(1d, 5d, 25d, 125d, 625d),
    3)
```

Moving harmonic mean

```scala mdoc:height=15
movingHarmonicMean[List, Int, Real](
    (1 to 5).toList.map(v => Real(v)),
    3)
```

## Mean Average Precision at K

See the page on [mean average precision](https://www.kaggle.com/wiki/MeanAveragePrecision) at Kaggle

```scala mdoc:silent:reset
import spire.math.Rational
import axle.ml.RankedClassifierPerformance._
```

Examples (from [benhamner/Metrics](https://github.com/benhamner/Metrics))

```scala mdoc
meanAveragePrecisionAtK[Int, Rational](List(1 until 5), List(1 until 5), 3)

// should be Rational.one
```

```scala mdoc
meanAveragePrecisionAtK[Int, Rational](List(List(1, 3, 4), List(1, 2, 4), List(1, 3)), List(1 until 6, 1 until 6, 1 until 6), 3)

// should be Rational(37, 54) aka 0.6851851851851851
```

```scala mdoc
meanAveragePrecisionAtK[Int, Rational](List(1 until 6, 1 until 6), List(List(6, 4, 7, 1, 2), List(1, 1, 1, 1, 1)), 5)

// should be Rational(13, 50)
```

```scala mdoc
meanAveragePrecisionAtK[Int, Rational](List(List(1, 3), List(1, 2, 3), List(1, 2, 3)), List(1 until 6, List(1, 1, 1), List(1, 2, 1)), 3)

// should be Rational(11, 18) aka 0.611111111111111
```

## Uniform Distribution

Imports and implicits (for all sections below)

```scala mdoc:silent:reset
import cats.implicits._
import spire.algebra._
import axle.probability._

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
```

Example

```scala mdoc
val X = uniformDistribution(List(2d, 4d, 4d, 4d, 5d, 5d, 7d, 9d))
```

## Standard Deviation

Example

```scala mdoc:silent
import axle.stats._

implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra
```

```scala mdoc
standardDeviation(X)
```

See also [Probability Model](#probability-model)

## Root-mean-square deviation

See the Wikipedia page on
[Root-mean-square deviation](https://en.wikipedia.org/wiki/Root-mean-square_deviation).

```scala mdoc:silent:reset
import cats.implicits._

import spire.algebra.Field
import spire.algebra.NRoot

import axle.stats._

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra
```

Given four numbers and an estimator function, compute the RMSD:

```scala mdoc:silent
val data = List(1d, 2d, 3d, 4d)
```

```scala mdoc
def estimator(x: Double): Double =
  x + 0.2

rootMeanSquareDeviation[List, Double](data, estimator)
```

## Reservoir Sampling

Reservoir Sampling is the answer to a common interview question.

```scala mdoc:silent:reset
import spire.random.Generator.rng
import spire.algebra.Field

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra

import axle.stats._
```

Demonstrate it uniformly sampling 15 of the first 100 integers

```scala mdoc
val sample = reservoirSampleK(15, LazyList.from(1), rng).drop(100).head
```

The mean of the sample should be in the ballpark of the mean of the entire list (50.5):

```scala mdoc
import axle.math.arithmeticMean

arithmeticMean(sample.map(_.toDouble))
```

Indeed it is.

## Future Work

Clarify imports starting with `uniformDistribution`
