# Randomness and Uncertainty

## Statistics

Common imports and implicits

```scala mdoc:silent:reset
import cats.implicits._
import spire.algebra._
import axle.probability._

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
```

### Uniform Distribution

Example

```scala mdoc
val X = uniformDistribution(List(2d, 4d, 4d, 4d, 5d, 5d, 7d, 9d))
```

### Standard Deviation

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
